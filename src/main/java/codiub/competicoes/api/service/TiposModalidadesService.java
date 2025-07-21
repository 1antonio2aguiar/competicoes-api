package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosUpdateTipoModalidadeRcd;
import codiub.competicoes.api.DTO.tiposVeiculos.DadosTiposVeiculosRcd;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.entity.TiposVeiculos;
import codiub.competicoes.api.repository.TiposModalidadesRepository;
import codiub.competicoes.api.repository.TiposVeiculosRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TiposModalidadesService {
    @Autowired
    private TiposModalidadesRepository tiposModalidadesRepository;

    // Exemplo
    /*public List<Fabricante> findAll(){
        return tiposVeiculosRepository.findAll();
    }*/

    // TiposVeiculos por id
    public TiposModalidades findById(Long id){
        Optional<TiposModalidades> obj = tiposModalidadesRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Tipo modalidade não encontrada! Id: " + id + " Tipo: " + TiposModalidades.class.getName()));
    }

    //Insert
    public TiposModalidades insert(DadosInsertTipoModalidadeRcd dados){
        return tiposModalidadesRepository.save(new TiposModalidades(dados));
    }

    // update
    public TiposModalidades update(Long id, DadosUpdateTipoModalidadeRcd dados){
        TiposModalidades tiposModalidadesUpd = tiposModalidadesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tipo Modalidade não cadastrada. Id: " + id));
        BeanUtils.copyProperties(dados, tiposModalidadesUpd, "id");
        return tiposModalidadesRepository.save(tiposModalidadesUpd);
    }

    // Delete
    public void delete(Long id){
        TiposModalidades tiposVeiculosDel = tiposModalidadesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tipo modalidade não cadastrada. Id: " + id));
        try {
            tiposModalidadesRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}