package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.campeonato.DadosInsertCampeonatoRcd;
import codiub.competicoes.api.DTO.locaisCompeticoes.DadosInsertLocaisCompeticoesRcd;
import codiub.competicoes.api.DTO.locaisCompeticoes.DadosUpdateLocaisCompeticoesRcd;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.entity.LocaisCompeticoes;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.repository.EmpresaRepository;
import codiub.competicoes.api.repository.LocaisCompeticoesRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocaisCompeticoesService {
    @Autowired
    private LocaisCompeticoesRepository locaisCompeticoesRepository;
    @Autowired private EmpresaRepository empresaRepository;

    // Exemplo
    /*public List<Fabricante> findAll(){
        return tiposVeiculosRepository.findAll();
    }*/

    // locais competicoes por id
    public LocaisCompeticoes findById(Long id){
        Optional<LocaisCompeticoes> obj = locaisCompeticoesRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Local competicao não encontrada! Id: " + id + " Tipo: " + LocaisCompeticoes.class.getName()));
    }

    //Insert
    public LocaisCompeticoes insert(DadosInsertLocaisCompeticoesRcd dados){
        LocaisCompeticoes locaisCompeticoes = new LocaisCompeticoes();
        BeanUtils.copyProperties(dados, locaisCompeticoes, "id");

        //Busco a empresa
        Empresa empresa = empresaRepository.findById(dados.empresa()).get();
        locaisCompeticoes.setEmpresa(empresa);

        LocaisCompeticoes lcInsert = locaisCompeticoesRepository.save(locaisCompeticoes);
        return lcInsert;
    }

    // update
    public LocaisCompeticoes update(Long id, DadosUpdateLocaisCompeticoesRcd dados){
        LocaisCompeticoes locaisCompeticoesUpd = locaisCompeticoesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Local competicao não cadastrado. Id: " + id));
        BeanUtils.copyProperties(dados, locaisCompeticoesUpd, "id");
        return locaisCompeticoesRepository.save(locaisCompeticoesUpd);
    }

    // Delete
    public void delete(Long id){
        LocaisCompeticoes tiposVeiculosDel = locaisCompeticoesRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Local competicao não cadastrado. Id: " + id));
        try {
            locaisCompeticoesRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}