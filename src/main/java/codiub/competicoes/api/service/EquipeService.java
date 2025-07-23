package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.equipe.DadosInsertEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosUpdateEquipeRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.EquipeFilter;
import codiub.competicoes.api.repository.EmpresaRepository;
import codiub.competicoes.api.repository.EquipeRepository;
import codiub.competicoes.api.repository.TiposModalidadesRepository;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipeService {
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private EquipeRepository equipeRepository;
    @Autowired private TiposModalidadesRepository modalidadesRepository;
    @Autowired
    private PessoaApiClient pessoaApiClient;

    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    //Metodo filtrar
    public Page<DadosListEquipeRcd> pesquisar(EquipeFilter filter, Pageable pageable) {
        Page<Equipe> equipePage = equipeRepository.filtrar(filter, pageable);
        List<Equipe> equipes = equipePage.getContent();

        // Coleta todos os IDs de pessoas necessários em uma única vez
        Set<Long> pessoaIds = new HashSet<>();
        equipes.forEach(equipe -> {
            pessoaIds.add(equipe.getAgremiacaoId());
            if (equipe.getTecnicoId() != null) pessoaIds.add(equipe.getTecnicoId());
            if (equipe.getAssistenteTecnicoId() != null) pessoaIds.add(equipe.getAssistenteTecnicoId());
        });

        // Faz uma ÚNICA chamada à API de pessoas para buscar todos os dados necessários
        List<DadosPessoasfjReduzRcd> pessoasDtos = pessoaApiClient.findPessoasByIds(pessoaIds);

        // Usa o novo método de fábrica para construir os DTOs
        List<DadosListEquipeRcd> dtoList = DadosListEquipeRcd.fromEntities(equipes, pessoasDtos);

        return new PageImpl<>(dtoList, pageable, equipePage.getTotalElements());
    }

    // Equipe por id
    public DadosListEquipeRcd findById(Long id) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Equipe não encontrada. Id: " + id));

        // Busca os dados das pessoas na API remota
        DadosPessoasfjReduzRcd agremiacaoDto = pessoaApiClient.findPessoaById(equipe.getAgremiacaoId());

        DadosPessoasfjReduzRcd tecnicoDto = null;
        if (equipe.getTecnicoId() != null) {
            tecnicoDto = pessoaApiClient.findPessoaById(equipe.getTecnicoId());
        }

        DadosPessoasfjReduzRcd assistenteDto = null;
        if (equipe.getAssistenteTecnicoId() != null) {
            assistenteDto = pessoaApiClient.findPessoaById(equipe.getAssistenteTecnicoId());
        }

        // Usa o novo método de fábrica para um único item
        return DadosListEquipeRcd.fromEntity(equipe, agremiacaoDto, tecnicoDto, assistenteDto);
    }

    public Page<DadosListEquipeRcd> findall(Pageable paginacao) {
        // Reutiliza a lógica do pesquisar sem filtro
        return pesquisar(new EquipeFilter(), paginacao);
    }

    //Insert
    public Equipe insert(DadosInsertEquipeRcd dados){
        Equipe equipe = new Equipe();

        BeanUtils.copyProperties(dados, equipe, "id");

        //Busco a empresa
        if(dados.empresaId() != null && dados.empresaId() != 0) {
            Empresa empresa = empresaRepository.findById(dados.empresaId()).get();
            equipe.setEmpresa(empresa);
        }

        //Busco a modalidade
        TiposModalidades modalidade = modalidadesRepository.findById(dados.modalidadeId()).get();
        equipe.setModalidade(modalidade);

        // Os IDs das pessoas (agremiacaoId, tecnicoId, etc.) já foram atualizados
        // pelo BeanUtils. É só salvar.

        return equipeRepository.save(equipe);
    }

    //Update
    public Equipe update(Long id, DadosUpdateEquipeRcd dados){
        Equipe equipeUpd = equipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Equipe não cadastrada. Id: " + id));

        BeanUtils.copyProperties(dados, equipeUpd, "id");

        //Busco a empresa
        if(dados.empresaId() != null && dados.empresaId() != 0){
            Empresa empresa = empresaRepository.findById(dados.empresaId()).get();
            equipeUpd.setEmpresa(empresa);
        }

        //Busco a modalidade
        TiposModalidades modalidade = modalidadesRepository.findById(dados.modalidadeId()).get();
        equipeUpd.setModalidade(modalidade);

        // Os IDs das pessoas (agremiacaoId, tecnicoId, etc.) já foram atualizados
        // pelo BeanUtils. É só salvar.

        return equipeRepository.save(equipeUpd);
    }

    // Delete
    public void delete(Long id){
        Equipe etapaDel = equipeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Equipe não cadastrada. Id: " + id));
        try {
            equipeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}