package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.categoria.DadosCategoriaRcd;
import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.repository.CategoriaRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Exemplo
    /*public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }*/

    // TiposVeiculos por id
    public Categoria findById(Long id){
        Optional<Categoria> obj = categoriaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Categoria não encontrada! Id: " + id + " Tipo: " + Categoria.class.getName()));
    }

    //Insert
    public Categoria insert(DadosCategoriaRcd dados){
        Categoria categoria = new Categoria();
        BeanUtils.copyProperties(dados, categoria, "id");

        Categoria categoriaInsert = categoriaRepository.save(categoria);
        return categoriaInsert;
    }

    // update
    public Categoria update(Long id, DadosCategoriaRcd dados){
        Categoria categoriaUpd = categoriaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Categoria não cadastrada. Id: " + id));
        BeanUtils.copyProperties(dados, categoriaUpd, "id");

        return categoriaRepository.save(categoriaUpd);
    }

    // Delete
    public void delete(Long id){
        Categoria categoriaDel = categoriaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Categoria não cadastrado. Id: " + id));
        try {
            categoriaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}