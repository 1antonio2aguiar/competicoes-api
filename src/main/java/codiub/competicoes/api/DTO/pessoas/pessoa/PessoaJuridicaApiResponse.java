package codiub.competicoes.api.DTO.pessoas.pessoa;
public record PessoaJuridicaApiResponse(
        Long id,
        String nome, // Razão Social
        String fisicaJuridica, // "J"
        String situacao,
        Long tipoPessoaId,
        String tipoPessoaNome,
        String cnpj,
        String nomeFantasia,
        String objetoSocial,
        String microEmpresa,
        Integer tipoEmpresa,
        String observacao

) { }
