package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.pontuacao.DadosPontuacaoRcd;
import codiub.competicoes.api.entity.Pontuacao;
import codiub.competicoes.api.repository.PontuacaoRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PontuacaoService {
    @Autowired
    private PontuacaoRepository pontuacaoRepository;

    // Exemplo
    /*public List<Pontuacao> findAll(){
        return pontuacaoRepository.findAll();
    }*/

    // TiposVeiculos por id
    public Pontuacao findById(Long id){
        Optional<Pontuacao> obj = pontuacaoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Pontuacao não encontrada! Id: " + id + " Tipo: " + Pontuacao.class.getName()));
    }

    //Insert
    public Pontuacao insert(DadosPontuacaoRcd dados){
        Pontuacao pontuacao = new Pontuacao();
        BeanUtils.copyProperties(dados, pontuacao, "id");

        Pontuacao pontuacaoInsert = pontuacaoRepository.save(pontuacao);
        return pontuacaoInsert;
    }

    // update
    public Pontuacao update(Long id, DadosPontuacaoRcd dados){
        Pontuacao pontuacaoUpd = pontuacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pontuacao não cadastrada. Id: " + id));
        BeanUtils.copyProperties(dados, pontuacaoUpd, "id");

        return pontuacaoRepository.save(pontuacaoUpd);
    }

    // Delete
    public void delete(Long id){
        Pontuacao pontuacaoDel = pontuacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pontuacao não cadastrado. Id: " + id));
        try {
            pontuacaoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}