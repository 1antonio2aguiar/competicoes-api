package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.etapa.DadosInsertEtapaRcd;
import codiub.competicoes.api.DTO.etapa.DadosUpdateEtapaRcd;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.repository.*;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import codiub.competicoes.api.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EtapaService {
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private CampeonatoRepository campeonatoRepository;
    @Autowired private EtapaRepository etapasRepository;
    @Autowired private LocaisCompeticoesRepository locaisCompeticoesRepository;

    // Exemplo
    /*public List<Campeonato> findAll(){
        return campeonatoRepository.findAll();
    }*/

    // Etapas por id
    public Etapa findById(Long id){
        Optional<Etapa> obj = etapasRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Etapa não encontrada! Id: " + id + " Tipo: " + Etapa.class.getName()));
    }

    //Insert
    public Etapa insert(DadosInsertEtapaRcd dados){
        //System.err.println("Chegou no service " + dados);
        Etapa etapa = new Etapa();
        BeanUtils.copyProperties(dados, etapa, "id");

        //Busco a empresa
        Empresa empresa = empresaRepository.findById(dados.empresa()).get();
        etapa.setEmpresa(empresa);

        //Busco o campeonato
        Campeonato campeonato = campeonatoRepository.findById(dados.campeonato()).get();
        etapa.setCampeonato(campeonato);

        //Busco o local
        LocaisCompeticoes local = locaisCompeticoesRepository.findById(dados.localCompeticao()).get();
        etapa.setLocalCompeticao(local);

        Date dataFormatada = DataUtils.formatarData(dados.dataEtapa().toString());
        etapa.setDataEtapa(dataFormatada);

        dataFormatada = DataUtils.formatarData(dados.dataInscricao().toString());
        etapa.setDataInscricao(dataFormatada);

        System.err.println("Etapa " + etapa);

        return etapasRepository.save(etapa);
    }

    // update
    public Etapa update(Long id, DadosUpdateEtapaRcd dados){

        Etapa etapaUpd = etapasRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Etapa não cadastrada. Id: " + id));
        BeanUtils.copyProperties(dados, etapaUpd, "id");

        //Busco o local
        LocaisCompeticoes local = locaisCompeticoesRepository.findById(dados.localCompeticao()).get();
        etapaUpd.setLocalCompeticao(local);

        return etapasRepository.save(etapaUpd);
    }

    // Delete
    public void delete(Long id){
        Etapa etapaDel = etapasRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Etapa não cadastrada. Id: " + id));
        try {
            etapasRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}