package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.tiposNados.DadosTipoNadoRcd;
import codiub.competicoes.api.entity.TipoNado;
import codiub.competicoes.api.repository.TipoNadoRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TipoNadoService {
    @Autowired
    private TipoNadoRepository tipoNadoRepository;

    // Exemplo
    /*public List<TipoNado> findAll(){
        return tipoNadoRepository.findAll();
    }*/

    // tipos nados por id
    public TipoNado findById(Long id){
        Optional<TipoNado> obj = tipoNadoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Tipo Nado não encontrado! Id: " + id + " Tipo: " + TipoNado.class.getName()));
    }

    //Insert
    public TipoNado insert(DadosTipoNadoRcd dados){
        TipoNado tipoNado = new TipoNado();
        BeanUtils.copyProperties(dados, tipoNado, "id");

        TipoNado tipoNadoInsert = tipoNadoRepository.save(tipoNado);
        return tipoNadoInsert;
    }

    // update
    public TipoNado update(Long id, DadosTipoNadoRcd dados){
        TipoNado tipoNadoUpd = tipoNadoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tipo Nado não cadastrado. Id: " + id));
        BeanUtils.copyProperties(dados, tipoNadoUpd, "id");

        return tipoNadoRepository.save(tipoNadoUpd);
    }

    // Delete
    public void delete(Long id){
        TipoNado tipoNadoDel = tipoNadoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tipo Nado não cadastrado. Id: " + id));
        try {
            tipoNadoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}