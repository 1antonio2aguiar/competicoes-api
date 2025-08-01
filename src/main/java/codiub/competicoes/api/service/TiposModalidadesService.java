package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.provas.DadosListProvasRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosListModalidadesRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosModalidadeRcd;
import codiub.competicoes.api.entity.Prova;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.ProvaFilter;
import codiub.competicoes.api.filter.TiposModalidadesFilter;
import codiub.competicoes.api.repository.ProvaRepository;
import codiub.competicoes.api.repository.TiposModalidadesRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TiposModalidadesService {
    @Autowired
    private TiposModalidadesRepository tiposModalidadesRepository;

    public TiposModalidadesService(TiposModalidadesRepository tiposModalidadesRepository) {
        this.tiposModalidadesRepository = tiposModalidadesRepository;
    }
    public Page<DadosListModalidadesRcd> findall(Pageable paginacao) {
        Page<TiposModalidades> tpModalidadePage = tiposModalidadesRepository.findAll(paginacao);
        List<DadosListModalidadesRcd> modalidadeDTOList = tpModalidadePage.getContent().stream()
                .map(DadosListModalidadesRcd::fromTiposModalidades) // Usa o método de fábrica
                .collect(Collectors.toList());
        return new PageImpl<>(modalidadeDTOList, paginacao, tpModalidadePage.getTotalElements());
    }

    public Page<DadosListModalidadesRcd> pesquisar(TiposModalidadesFilter filter, Pageable pageable) {
        Page<TiposModalidades> tpModalidadePage = tiposModalidadesRepository.filtrar(filter, pageable);

        // Mapeia a lista de modalidades para uma lista de DadosListModalidadeRcd usando o método de fábrica
        List<DadosListModalidadesRcd> modalidadeDTOList = tpModalidadePage.getContent().stream()
                .map(DadosListModalidadesRcd::fromTiposModalidades)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosListProvasRcd> com os dados mapeados
        return new PageImpl<>(modalidadeDTOList, pageable, tpModalidadePage.getTotalElements());
    }

    // Tipos modalidade por id
    public DadosListModalidadesRcd findById(Long id) {
        Optional<TiposModalidades> tpModalidadeOptional = tiposModalidadesRepository.findById(id);
        return tpModalidadeOptional.map(DadosListModalidadesRcd::fromTiposModalidades).orElse(null);
    }

    //Insert
    public TiposModalidades insert(DadosModalidadeRcd dados){
        TiposModalidades tpm = new TiposModalidades();

        BeanUtils.copyProperties(dados, tpm, "id");
        return tiposModalidadesRepository.save(tpm);
    }

    // update
    public TiposModalidades update(Long id, DadosModalidadeRcd dados){
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