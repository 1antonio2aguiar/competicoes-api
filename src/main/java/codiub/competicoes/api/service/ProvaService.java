package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.provas.DadosInsertProvasRcd;
import codiub.competicoes.api.DTO.provas.DadosListProvasRcd;
import codiub.competicoes.api.DTO.provas.DadosUpdateProvasRcd;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.filter.ProvaFilter;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvaService {
    @Autowired private ProvaRepository provaRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private CampeonatoRepository campeonatoRepository;
    @Autowired private EtapaRepository etapaRepository;
    @Autowired private TipoNadoRepository tipoNadoRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    public ProvaService(ProvaRepository provaRepository) {
        this.provaRepository = provaRepository;
    }

    //Metodo filtrar
    public Page<DadosListProvasRcd> pesquisar(ProvaFilter filter, Pageable pageable) {
        Page<Prova> provaPage = provaRepository.filtrar(filter, pageable);

        // Mapeia a lista de provas para uma lista de DadosListProvasRcd usando o método de fábrica
        List<DadosListProvasRcd> provaDTOList = provaPage.getContent().stream()
                .map(DadosListProvasRcd::fromProva)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosListProvasRcd> com os dados mapeados
        return new PageImpl<>(provaDTOList, pageable, provaPage.getTotalElements());
    }

    // Prova por id
    public DadosListProvasRcd findById(Long id) {
        Optional<Prova> provaOptional = provaRepository.findById(id);
        return provaOptional.map(DadosListProvasRcd::fromProva).orElse(null);
    }

    public Page<DadosListProvasRcd> findall(Pageable paginacao) {
        Page<Prova> provaPage = provaRepository.findAll(paginacao);
        List<DadosListProvasRcd> provaDTOList = provaPage.getContent().stream()
                .map(DadosListProvasRcd::fromProva) // Usa o método de fábrica
                .collect(Collectors.toList());
        return new PageImpl<>(provaDTOList, paginacao, provaPage.getTotalElements());
    }

    //Insert
    public Prova insert(DadosInsertProvasRcd dados){
        Prova prova = new Prova();

        BeanUtils.copyProperties(dados, prova, "id");

        //Busco a empresa
        if(dados.empresaId() != null && dados.empresaId() != 0) {
            Empresa empresa = empresaRepository.findById(dados.empresaId()).get();
            prova.setEmpresa(empresa);
        }

        //Busco a etapa
        Etapa etapa = etapaRepository.findById(dados.etapaId()).get();
        prova.setEtapa(etapa);

        //Tipo nado
        TipoNado tipoNado = tipoNadoRepository.findById(dados.tipoNadoId()).get();
        prova.setTipoNado(tipoNado);

        //Busco a categoria
        Categoria categoria = categoriaRepository.findById(dados.categoriaId()).get();
        prova.setCategoria(categoria);

        dados.getIndiceTecnicoAsLong().ifPresent(prova::setIndiceTecnico);
        dados.getRecordAsLong().ifPresent(prova::setRecord);

        return provaRepository.save(prova);
    }

    //Update
     public Prova update(Long id, DadosUpdateProvasRcd dados){
        Prova provaUpd = provaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Prova não cadastrada. Id: " + id));

        BeanUtils.copyProperties(dados, provaUpd, "id");

        //Tipo nado
        TipoNado tipoNado = tipoNadoRepository.findById(dados.tipoNadoId()).get();
        provaUpd.setTipoNado(tipoNado);

        //Busco a categoria
        Categoria categoria = categoriaRepository.findById(dados.categoriaId()).get();
        provaUpd.setCategoria(categoria);

        dados.getIndiceTecnicoAsLong().ifPresent(provaUpd::setIndiceTecnico);
        dados.getRecordAsLong().ifPresent(provaUpd::setRecord);

        return provaRepository.save(provaUpd);
    }

    // Delete
    public void delete(Long id){
        Prova provaDel = provaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Prova não cadastrada. Id: " + id));
        try {
            provaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}