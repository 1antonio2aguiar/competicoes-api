package codiub.competicoes.api.DTO.pessoas.pessoa;

import codiub.competicoes.api.entity.pessoas.TiposPessoas;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaJuridica;

import java.util.Optional;

public record DadosPessoaJuridicaReqRespRcd(
        Long id,
        String nome, // Razão Social
        String fisicaJuridica, // "J"
        Integer situacao,
        Long tipoPessoaId,
        String tipoPessoaNome,
        String cnpj,
        String nomeFantasia,
        String objetoSocial,
        String microEmpresa,
        Integer tipoEmpresa,
        String observacao
) { }
