package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.atletas.DadosAtletasReduzidoRcd;
import codiub.competicoes.api.DTO.atletas.DadosInsertAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosListAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosUpdateAtletaRcd;
import codiub.competicoes.api.DTO.equipe.DadosInsertEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosUpdateEquipeRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.AtletaFilter;
import codiub.competicoes.api.filter.EquipeFilter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtletaService {
    @Autowired private AtletaRepository atletaRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private EquipeRepository equipeRepository;
    @Autowired private PessoasRepository pessoasRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private AtletaCustonRepository atletaCustonRepository;

    public AtletaService(AtletaRepository atletaRepository) {
        this.atletaRepository = atletaRepository;
    }

    //Metodo filtrar
    @Transactional(readOnly = true)
    public Page<DadosListAtletasRcd> pesquisar(AtletaFilter filter, Pageable pageable) {
        Page<Atleta> atletaPage = atletaRepository.filtrar(filter, pageable);

        // Mapeia a lista de atletas para uma lista de DadosListAtletasRcd usando o método de fábrica
        List<DadosListAtletasRcd> atletaDTOList = atletaPage.getContent().stream()
                .map(DadosListAtletasRcd::fromAtleta)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosListAtletasRcd> com os dados mapeados
        return new PageImpl<>(atletaDTOList, pageable, atletaPage.getTotalElements());
    }
    @Transactional(readOnly = true)
    public Page<DadosAtletasReduzidoRcd> atletaNotInInscricoes(AtletaFilter filter, Pageable pageable) {
        // Recupera somente atletas que não pertecem a nenhuma inscricao.
        Page<Atleta> atletaPage = atletaCustonRepository.atletaNotInInscricoes(filter, pageable);

        // Mapeia a lista de atletas para uma lista de DadosAtletasReduzidoRcd usando o método de fábrica
        List<DadosAtletasReduzidoRcd> atletasDTOList = atletaPage.getContent().stream()
                .map(DadosAtletasReduzidoRcd::fromAtletas)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosAtletasRcd> com os dados mapeados
        return new PageImpl<>(atletasDTOList, pageable, atletaPage.getTotalElements());
    }

    // Atleta por id
    @Transactional(readOnly = true)
    public DadosListAtletasRcd findById(Long id) {
        Optional<Atleta> atletaOptional = atletaRepository.findById(id);
        return atletaOptional.map(DadosListAtletasRcd::fromAtleta).orElse(null);
    }
    @Transactional(readOnly = true)
    public Page<DadosListAtletasRcd> findall(Pageable paginacao) {
        Page<Atleta> atletaPage = atletaRepository.findAll(paginacao);
        List<DadosListAtletasRcd> atletaDTOList = atletaPage.getContent().stream()
                .map(DadosListAtletasRcd::fromAtleta) // Usa o método de fábrica
                .collect(Collectors.toList());
        return new PageImpl<>(atletaDTOList, paginacao, atletaPage.getTotalElements());
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

        //Busco a atleta
        Pessoas pessoa = pessoasRepository.findById(dados.pessoaId()).get();
        atleta.setPessoa(pessoa);

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