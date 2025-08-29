package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.atletas.DadosAtletasReduzidoRcd;
import codiub.competicoes.api.DTO.atletas.DadosInsertAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosListAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosUpdateAtletaRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.filter.AtletaFilter;
import codiub.competicoes.api.filter.pessoas.PessoaFisicaFilter;
import codiub.competicoes.api.repository.*;
import codiub.competicoes.api.repository.atleta.custon.AtletaCustonRepository;
import codiub.competicoes.api.repository.pessoas.PessoasRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AtletaService {
    @Autowired private AtletaRepository atletaRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private EquipeRepository equipeRepository;
    @Autowired private PessoasRepository pessoasRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private AtletaCustonRepository atletaCustonRepository;

    @Autowired
    private PessoaApiClient pessoaApiClient;

    public AtletaService(AtletaRepository atletaRepository, PessoaApiClient pessoaApiClient) {
        this.atletaRepository = atletaRepository;
        this.pessoaApiClient = pessoaApiClient;
    }

    @Transactional(readOnly = true)
    public Page<DadosPessoasReduzidoRcd> pesquisarPessoasDisponiveisParaAtleta(PessoaFisicaFilter filter, Pageable pageable) {
        String dataNascStr = null;
        if (filter.getDataNascimento() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dataNascStr = sdf.format(filter.getDataNascimento());
        }

        // 1. A chamada fica limpa e direta
        Page<DadosPessoasReduzidoRcd> responseDaApi = pessoaApiClient.filtrarPessoasFisica(
                filter.getId(),
                filter.getNome(),
                filter.getCpf(),
                dataNascStr,
                pageable
        );

        // 2. O restante do código funciona exatamente como antes
        List<DadosPessoasReduzidoRcd> candidatos = responseDaApi.getContent();
        if (candidatos.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> candidatoIds = candidatos.stream().map(DadosPessoasReduzidoRcd::id).collect(Collectors.toList());
        List<Long> idsJaCadastrados = atletaRepository.findPessoaIdsCadastradosComoAtletas(candidatoIds);
        List<DadosPessoasReduzidoRcd> pessoasDisponiveis = candidatos.stream()
                .filter(candidato -> !idsJaCadastrados.contains(candidato.id()))
                .collect(Collectors.toList());

        return new PageImpl<>(pessoasDisponiveis, pageable, responseDaApi.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<DadosAtletasReduzidoRcd> atletaNotInInscricoes(AtletaFilter filter, Pageable pageable) {
        Set<Long> idsDePessoasFiltradasPorNome = null;

        // --- PASSO DE PRÉ-FILTRAGEM (SE O FILTRO DE NOME EXISTIR) ---
        if (filter.getPessoaNome() != null && !filter.getPessoaNome().trim().isEmpty()) {

            // Chamamos o NOVO método, que já nos dá o tipo correto!
            ResponseEntity<List<DadosPessoasfjReduzRcd>> response = pessoaApiClient.pesquisarPessoasReduzidoPorTermo(filter.getPessoaNome(), false);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                idsDePessoasFiltradasPorNome = response.getBody().stream()
                        .map(DadosPessoasfjReduzRcd::id) // << Sem cast, limpo e seguro
                        .collect(Collectors.toSet());
            } else {
                idsDePessoasFiltradasPorNome = new HashSet<>();
            }

            if (idsDePessoasFiltradasPorNome.isEmpty()) {
                return Page.empty(pageable);
            }
        }

        // --- PASSO DE CONSULTA LOCAL ---
        // 2. Busca os atletas, passando a lista de IDs (se existir) para o repositório.
        Page<Atleta> atletaPage = atletaCustonRepository.atletaNotInInscricoes(filter, pageable, idsDePessoasFiltradasPorNome);
        List<Atleta> atletasDaPagina = atletaPage.getContent();

        if (atletasDaPagina.isEmpty()) {
            return Page.empty(pageable);
        }

        // --- PASSO DE ENRIQUECIMENTO (COMO ANTES) ---
        // 3. Coleta os 'pessoaId' da página de resultados para buscar os nomes completos.
        Set<Long> pessoaIdsParaEnriquecer = atletasDaPagina.stream()
                .map(Atleta::getPessoaId)
                .collect(Collectors.toSet());

        // 4. Faz a chamada para a 'pessoas-api' para obter os nomes.
        List<DadosPessoasfjReduzRcd> pessoasInfo = pessoaApiClient.findPessoasByIds(pessoaIdsParaEnriquecer);
        Map<Long, String> mapaIdParaNome = pessoasInfo.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, DadosPessoasfjReduzRcd::nome));

        // 5. Constrói o DTO final.
        List<DadosAtletasReduzidoRcd> atletasDTOList = atletasDaPagina.stream()
                .map(atleta -> new DadosAtletasReduzidoRcd(
                        atleta.getId(),
                        mapaIdParaNome.getOrDefault(atleta.getPessoaId(), "Nome não disponível"),
                        atleta.getEquipe().getNome()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(atletasDTOList, pageable, atletaPage.getTotalElements());
    }

    // O método de cima traz pessoas fisica e juridia
    //Metodo filtrar Aqui pega os dados do atleta(pessoa_id) em pessoas-api
    @Transactional(readOnly = true)
    public Page<DadosListAtletasRcd> pesquisar(AtletaFilter filter, Pageable pageable) {
        Set<Long> idsDePessoasFiltradasPorNome = null;

        // ETAPA 1: Se um nome de pessoa foi fornecido, busca os IDs correspondentes na API de Pessoas.
        if (StringUtils.hasText(filter.getPessoaNome())) {

            // Busca todas as pessoas correspondentes, sem paginação, para garantir um filtro completo.
            Page<DadosPessoasReduzidoRcd> paginaPessoas = pessoaApiClient.filtrarPessoasFisica(
                    null,
                    filter.getPessoaNome(),
                    null,
                    null,
                    Pageable.unpaged()
            );

            List<DadosPessoasReduzidoRcd> pessoasEncontradas = paginaPessoas.getContent();

            // Se nenhuma pessoa for encontrada na API externa, não haverá atletas para buscar.
            if (pessoasEncontradas.isEmpty()) {
                return Page.empty(pageable);
            }

            // Coleta os IDs para usar na consulta ao banco de dados local.
            idsDePessoasFiltradasPorNome = pessoasEncontradas.stream()
                    .map(DadosPessoasReduzidoRcd::id)
                    .collect(Collectors.toSet());
        }

        // ETAPA 2: Busca os atletas no banco de dados local, usando os filtros e os IDs de pessoa (se aplicável).
        Page<Atleta> atletaPage = atletaRepository.filtrarComPessoaIds(filter, idsDePessoasFiltradasPorNome, pageable);

        // Se a busca local por atletas não retornar resultados, encerra o processo.
        if (atletaPage.isEmpty()) {
            return Page.empty(pageable);
        }

        // ETAPA 3: Enriquece os dados dos atletas encontrados com informações da API de Pessoas.
        List<Atleta> atletasNaPagina = atletaPage.getContent();
        Set<Long> pessoaIdsParaEnriquecer = atletasNaPagina.stream()
                .map(Atleta::getPessoaId)
                .collect(Collectors.toSet());

        List<DadosPessoasReduzidoRcd> pessoasDtos = pessoaApiClient.findPessoasFisicaByIds(pessoaIdsParaEnriquecer);

        // Mapeia os dados das pessoas por ID para facilitar a combinação.
        Map<Long, DadosPessoasReduzidoRcd> pessoasMap = pessoasDtos.stream()
                .collect(Collectors.toMap(DadosPessoasReduzidoRcd::id, Function.identity()));

        // Converte as entidades 'Atleta' para o DTO de resposta, combinando com os dados das pessoas.
        List<DadosListAtletasRcd> dtoList = DadosListAtletasRcd.fromEntities(atletasNaPagina, pessoasMap);

        // Retorna a página final com o DTO de resposta e as informações de paginação corretas.
        return new PageImpl<>(dtoList, pageable, atletaPage.getTotalElements());
    }


    // Atleta por id
    @Transactional(readOnly = true)
    public DadosListAtletasRcd findById(Long id) {
        Atleta atleta = atletaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Atleta não encontrado. Id: " + id));

        Long pessoaId = atleta.getPessoaId();
        List<DadosPessoasReduzidoRcd> pessoaDtoList = pessoaApiClient.findPessoasFisicaByIds(Collections.singleton(pessoaId));
        DadosPessoasReduzidoRcd pessoaDto = pessoaDtoList.stream().findFirst().orElse(null);
        return DadosListAtletasRcd.fromAtleta(atleta, pessoaDto);
    }

    //Insert
    public Atleta insert(DadosInsertAtletasRcd dados) {
        Atleta atleta = new Atleta();

        BeanUtils.copyProperties(dados, atleta, "id");

        //Busco a empresa
        if (dados.empresaId() != null && dados.empresaId() != 0) {
            Empresa empresa = empresaRepository.findById(dados.empresaId()).get();
            atleta.setEmpresa(empresa);
        }

        //Busco a equipe
        Equipe equipe = equipeRepository.findById(dados.equipeId()).get();
        atleta.setEquipe(equipe);

        //Busco a categoria
        Categoria categoria = categoriaRepository.findById(dados.categoriaId()).get();
        atleta.setCategoria(categoria);

        return atletaRepository.save(atleta);
    }

    //Update
     public Atleta update(Long id, DadosUpdateAtletaRcd dados){
        Atleta atletaUpd = atletaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Atleta não cadastrado. Id: " + id));

        BeanUtils.copyProperties(dados, atletaUpd, "id");

         //Busco a equipe
         Equipe equipe = equipeRepository.findById(dados.equipeId()).get();
         atletaUpd.setEquipe(equipe);

         //Busco a categoria
         Categoria categoria = categoriaRepository.findById(dados.categoriaId()).get();
         atletaUpd.setCategoria(categoria);

        return atletaRepository.save(atletaUpd);
    }

    // Delete
    public void delete(Long id){
        Atleta atletaDel = atletaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("atleta não cadastrado. Id: " + id));
        try {
            atletaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}