package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.inscricoes.DadosInsertInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosListInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosUpdateInscricoesRcd;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.enuns.StatusInscricao;
import codiub.competicoes.api.entity.enuns.StatusTipoInscricao;
import codiub.competicoes.api.filter.InscricaoFilter;
import codiub.competicoes.api.repository.*;
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
public class InscricaoService {
    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private ProvaRepository provaRepository;
    @Autowired private AtletaRepository atletaRepository;
    @Autowired private PontuacaoRepository pontuacaoRepository;
    public InscricaoService(InscricaoRepository inscricaoRepository) {
        this.inscricaoRepository = inscricaoRepository;
    }

    //Metodo filtrar
    @Transactional(readOnly = true)
    public Page<DadosListInscricoesRcd> pesquisar(InscricaoFilter filter, Pageable pageable) {
        //System.err.println("Acho que é aqui " + filter);
        Page<Inscricoes> inscricaoPage = inscricaoRepository.filtrar(filter, pageable);

        // Mapeia a lista de inscricoes para uma lista de DadosListInscricoesRcd usando o método de fábrica
        List<DadosListInscricoesRcd> inscricaoDTOList = inscricaoPage.getContent().stream()
                .map(DadosListInscricoesRcd::fromInscricao)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosListInscricoesRcd> com os dados mapeados
        return new PageImpl<>(inscricaoDTOList, pageable, inscricaoPage.getTotalElements());
    }

    // Inscricao por id
    @Transactional(readOnly = true)
    public DadosListInscricoesRcd findById(Long id) {
        Optional<Inscricoes> inscricaoOptional = inscricaoRepository.findById(id);
        return inscricaoOptional.map(DadosListInscricoesRcd::fromInscricao).orElse(null);
    }
    @Transactional(readOnly = true)
    public Page<DadosListInscricoesRcd> findall(Pageable paginacao) {
        Page<Inscricoes> inscricaoPage = inscricaoRepository.findAll(paginacao);
        List<DadosListInscricoesRcd> inscricaoDTOList = inscricaoPage.getContent().stream()
                .map(DadosListInscricoesRcd::fromInscricao) // Usa o método de fábrica
                .collect(Collectors.toList());
        return new PageImpl<>(inscricaoDTOList, paginacao, inscricaoPage.getTotalElements());
    }

    //Insert
    public Inscricoes insert(DadosInsertInscricoesRcd dados){
        Inscricoes inscricao = new Inscricoes();

        BeanUtils.copyProperties(dados, inscricao, "id");

        //Busco a empresa
        if(dados.empresaId() != null && dados.empresaId() != 0) {
            Empresa empresa = empresaRepository.findById(dados.empresaId()).get();
            inscricao.setEmpresa(empresa);
        }
        //Busco a prova
        Prova prova = provaRepository.findById(dados.provaId()).get();
        inscricao.setProva(prova);

        //Busco o atleta
        Atleta atleta = atletaRepository.findById(dados.atletaId()).get();
        inscricao.setAtleta(atleta);

        inscricao.setStatus(StatusInscricao.toStatusInscricaoEnum(dados.status()));
        inscricao.setTipoInscricao(StatusTipoInscricao.toStatusTipoInscricaoEnum(dados.statusTipoInscricao()));

        //System.err.println("dados " + inscricao);

        return inscricaoRepository.save(inscricao);
    }

    //Update
     public Inscricoes update(Long id, DadosUpdateInscricoesRcd dados){
         Inscricoes inscricaoUpd = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Inscrição não cadastrada. Id: " + id));

        BeanUtils.copyProperties(dados, inscricaoUpd, "id");
         inscricaoUpd.setStatus(StatusInscricao.toStatusInscricaoEnum(dados.status()));
         inscricaoUpd.setTipoInscricao(StatusTipoInscricao.toStatusTipoInscricaoEnum(dados.statusTipoInscricao()));

         //Pontuação
         //Pontuacao pontuacao = pontuacaoRepository.findById(dados.pontuacaoId()).get();
         //inscricaoUpd.setPontuacao(pontuacao);

         //dados.getResultadoAsLong().ifPresent(inscricaoUpd::setResultado);

         return inscricaoRepository.save(inscricaoUpd);
    }

    // Delete
    public void delete(Long id){
        Inscricoes inscricaoDel = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Inscrição não cadastrada. Id: " + id));
        try {
            inscricaoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}