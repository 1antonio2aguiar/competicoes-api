package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.tiposVeiculos.DadosTiposVeiculosRcd;
import codiub.competicoes.api.entity.TiposVeiculos;
import codiub.competicoes.api.repository.TiposVeiculosRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TiposVeiculosService {
    @Autowired
    private TiposVeiculosRepository tiposVeiculosRepository;

    // Exemplo
    /*public List<Fabricante> findAll(){
        return tiposVeiculosRepository.findAll();
    }*/

    // TiposVeiculos por id
    public TiposVeiculos findById(Long id){
        Optional<TiposVeiculos> obj = tiposVeiculosRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Tipo veiculos não encontrado! Id: " + id + " Tipo: " + TiposVeiculos.class.getName()));
    }

    //Insert
    public TiposVeiculos insert(DadosTiposVeiculosRcd dados){
        return tiposVeiculosRepository.save(new TiposVeiculos(dados));
    }

    // update
    public TiposVeiculos update(Long id, DadosTiposVeiculosRcd dados){
        TiposVeiculos tiposVeiculosUpd = tiposVeiculosRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tipo Veiculo não cadastrado. Id: " + id));
        BeanUtils.copyProperties(dados, tiposVeiculosUpd, "id");
        return tiposVeiculosRepository.save(tiposVeiculosUpd);
    }

    // Delete
    public void delete(Long id){
        TiposVeiculos tiposVeiculosDel = tiposVeiculosRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("TiposVeiculos não cadastrado. Id: " + id));
        try {
            tiposVeiculosRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}