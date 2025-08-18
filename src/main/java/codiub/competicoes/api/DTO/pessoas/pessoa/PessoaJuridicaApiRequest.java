package codiub.competicoes.api.DTO.pessoas.pessoa;

public record PessoaJuridicaApiRequest(
        String nome, // Razão Social
        Integer situacao,
        Long tipoPessoaId,
        String cnpj,
        String nomeFantasia,
        String objetoSocial,
        String microEmpresa,
        Integer tipoEmpresa,
        String observacao
) { }
