package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.etapa.DadosInsertEtapaRcd;
import codiub.competicoes.api.DTO.etapa.DadosUpdateEtapaRcd;
import codiub.competicoes.api.DTO.parametro.DadosListParametroRcd;
import codiub.competicoes.api.DTO.provas.DadosListProvasRcd;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.filter.ParametroFilter;
import codiub.competicoes.api.filter.ProvaFilter;
import codiub.competicoes.api.repository.*;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import codiub.competicoes.api.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParametroService {
    @Autowired private EtapaRepository etapasRepository;
    @Autowired private ParametroRepository parametroRepository;

    // filtrar parametro por etapa
    public Page<DadosListParametroRcd> pesquisar(ParametroFilter filter, Pageable pageable) {
        Page<Parametro> parametroPage = parametroRepository.filtrar(filter, pageable);

        // Mapeia a lista de provas para uma lista de DadosListProvasRcd usando o método de fábrica
        List<DadosListParametroRcd> parametroDTOList = parametroPage.getContent().stream()
                .map(DadosListParametroRcd::fromParametro)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosListParametroRcd> com os dados mapeados
        return new PageImpl<>(parametroDTOList, pageable, parametroPage.getTotalElements());
    }

}