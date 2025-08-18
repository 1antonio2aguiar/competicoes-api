package codiub.competicoes.api.service.pessoas;

import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasGeralRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import codiub.competicoes.api.repository.AtletaRepository;
import codiub.competicoes.api.repository.InscricaoRepository;
import codiub.competicoes.api.repository.pessoas.PessoasRepository;
import codiub.competicoes.api.repository.pessoas.custon.PessoasCustonRepository;
import codiub.competicoes.api.repository.pessoas.custon.PessoasCustonRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
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
    //@Autowired private PessoasCustonRepository pessoaCustonRepository;

    @Autowired
    private PessoaApiClient pessoaApiClient;

    @Autowired
    private AtletaRepository atletaRepository;

    public void delete(Long id) {
        // 1. REGRA DE NEGÓCIO LOCAL: Verificar se a pessoa possui inscrições
        // O método existsByPessoaId é mágico no Spring Data JPA
        boolean temAtletas = atletaRepository.existsByPessoaId(id);

        if (temAtletas) {
            // Se tiver, bloqueia a exclusão e informa o motivo.
            // É uma má prática deletar uma pessoa que tem um histórico importante no sistema.
            throw new DatabaseException("Não é possível excluir esta pessoa, pois ela possui inscrições em competições.");
        }

        // 2. ORQUESTRAÇÃO: Se a validação passar, chama o outro microsserviço
        // para executar a exclusão. O try-catch aqui é para tratar possíveis
        // erros de comunicação ou se a pessoa não for encontrada na outra API.
        try {
            pessoaApiClient.deletePessoa(id);
        } catch (Exception e) {
            // Aqui você pode tratar erros específicos do Feign, como 404 (Not Found)
            // e traduzir para uma exceção do seu sistema.
            throw new RuntimeException("Falha ao tentar excluir a pessoa na API de Pessoas. Causa: " + e.getMessage());
        }
    }

    public PessoasService(PessoasRepository pessoasRepository) {
        this.pessoasRepository = pessoasRepository;
    }


}