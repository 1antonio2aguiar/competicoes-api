package codiub.competicoes.api.config;

public record PessoaApiResponseDto(
        Long id, String nome, String cpf, String sexo, String estadoCivil, String dataNascimento, String observacao
) {}