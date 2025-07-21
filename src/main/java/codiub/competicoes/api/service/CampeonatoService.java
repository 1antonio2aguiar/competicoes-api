package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.campeonato.DadosInsertCampeonatoRcd;
import codiub.competicoes.api.DTO.campeonato.DadosUpdateCampeonatoRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosUpdateTipoModalidadeRcd;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.repository.CampeonatoRepository;
import codiub.competicoes.api.repository.EmpresaRepository;
import codiub.competicoes.api.repository.TiposModalidadesRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CampeonatoService {
    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private TiposModalidadesRepository tiposModalidadesRepository;

    // Exemplo
    /*public List<Campeonato> findAll(){
        return campeonatoRepository.findAll();
    }*/

    // TiposVeiculos por id
    public Campeonato findById(Long id){
        Optional<Campeonato> obj = campeonatoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Campeonato não encontrado! Id: " + id + " Tipo: " + Campeonato.class.getName()));
    }

    //Insert
    public Campeonato insert(DadosInsertCampeonatoRcd dados){
        Campeonato campeonato = new Campeonato();
        BeanUtils.copyProperties(dados, campeonato, "id");

        //Busco a empresa
        Empresa empresa = empresaRepository.findById(dados.empresa()).get();
        campeonato.setEmpresa(empresa);

        //Busco a modalidade
        TiposModalidades modalidade = tiposModalidadesRepository.findById(dados.modalidade()).get();
        campeonato.setModalidade(modalidade);

        Campeonato campeonatoInsert = campeonatoRepository.save(campeonato);
        return campeonatoInsert;
    }

    // update
    public Campeonato update(Long id, DadosUpdateCampeonatoRcd dados){
        Campeonato campeonatoUpd = campeonatoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Campeonato não cadastrado. Id: " + id));
        BeanUtils.copyProperties(dados, campeonatoUpd, "id");

        return campeonatoRepository.save(campeonatoUpd);
    }

    // Delete
    public void delete(Long id){
        Campeonato tiposVeiculosDel = campeonatoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Campeonato não cadastrado. Id: " + id));
        try {
            campeonatoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}