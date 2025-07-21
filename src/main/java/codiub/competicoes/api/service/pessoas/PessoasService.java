package codiub.competicoes.api.service.pessoas;

import codiub.competicoes.api.DTO.pessoas.DadosPessoasRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import codiub.competicoes.api.repository.pessoas.PessoasRepository;
import codiub.competicoes.api.repository.pessoas.custon.PessoasCustonRepository;
import codiub.competicoes.api.repository.pessoas.custon.PessoasCustonRepository;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PessoasService {
    @Autowired private PessoasRepository pessoasRepository;
    @Autowired private PessoasCustonRepository pessoaCustonRepository;

    public PessoasService(PessoasRepository pessoasRepository) {
        this.pessoasRepository = pessoasRepository;
    }

    //Metodo filtrar
    public Page<DadosPessoasReduzidoRcd> pesquisar(PessoasFilter filter, Pageable pageable) {
        Page<Pessoas> pessoasPage = pessoasRepository.filtrar(filter, pageable);

        // Mapeia a lista de Pessoas para uma lista de DadosPessoasRcd usando o método de fábrica
        List<DadosPessoasReduzidoRcd> pessoasDTOList = pessoasPage.getContent().stream()
                .map(DadosPessoasReduzidoRcd::fromPessoas)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosPessoasRcd> com os dados mapeados
        return new PageImpl<>(pessoasDTOList, pageable, pessoasPage.getTotalElements());
    }

    public Page<DadosPessoasReduzidoRcd> pessoaNotInEquipes(PessoasFilter filter, Pageable pageable) {
        // Recupera somente pessoas que não pertecem a nenhuma equipe.
        Page<Pessoas> pessoasPage = pessoaCustonRepository.pessoaNotInEquipes(filter, pageable);

        // Mapeia a lista de Pessoas para uma lista de DadosPessoasRcd usando o método de fábrica
        List<DadosPessoasReduzidoRcd> pessoasDTOList = pessoasPage.getContent().stream()
                .map(DadosPessoasReduzidoRcd::fromPessoas)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosPessoasRcd> com os dados mapeados
        return new PageImpl<>(pessoasDTOList, pageable, pessoasPage.getTotalElements());
    }

    // Pessoas por id
    public DadosPessoasRcd findById(Long id) {
        Optional<Pessoas> pessoasOptional = pessoasRepository.findById(id);
        return pessoasOptional.map(DadosPessoasRcd::fromPessoas).orElse(null);
    }

    /*public Page<DadosPessoasRcd> findall(Pageable paginacao) {
        Page<Pessoas> pessoasPage = pessoasRepository.findAll(paginacao);
        List<DadosPessoasRcd> pessoasDTOList = pessoasPage.getContent().stream()
                .map(DadosPessoasRcd::fromPessoas) // Usa o método de fábrica
                .collect(Collectors.toList());
        return new PageImpl<>(pessoasDTOList, paginacao, pessoasPage.getTotalElements());
    }*/

}