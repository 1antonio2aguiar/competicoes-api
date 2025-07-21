package codiub.competicoes.api.config;

public record PessoaApiInsertRequestDto(
        String nome, String observacao, Integer situacao, Long tipoPessoaId,
        String cpf, String sexo, Integer estadoCivil, String dataNascimento, // dataNascimento como String "YYYY-MM-DD"
        String nomeMae, String nomePai
) {}
