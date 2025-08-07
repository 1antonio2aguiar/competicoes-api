package codiub.competicoes.api.DTO.pessoas.pessoa;

import java.time.LocalDate;

import codiub.competicoes.api.entity.enuns.Situacao; // Importe seus Enums
import jakarta.validation.constraints.*;

public record DadosInsertPessoaFisicaRcd(
        // Campos da entidade Pessoa (pai)
        Long id, // Nulo para insert, preenchido para update

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
        String nome,
        String fisicaJuridica,

        // fisicaJuridica será 'F', definido pelo tipo de DTO/endpoint

        @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
        String observacao,

        @NotNull(message = "Situação é obrigatória")
        Integer situacao, // Recebe o Enum Situacao

        @NotNull(message = "Tipo de Pessoa é obrigatório")
        Long tipoPessoaId, // ID da entidade TiposPessoas

        // Campos da entidade DadosPessoaFisica (filha)
        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 11, message = "CPF deve ter 11 caracteres")
        String cpf,

        @Size(max = 1, message = "Sexo deve ter no máximo 1 caractere")
        String sexo,

        @NotNull // Se o estado civil é obrigatório
        @Min(value = 0, message = "Código do estado civil deve ser no mínimo 0")
        @Max(value = 5, message = "Código do estado civil deve ser no máximo 5")
        Integer estadoCivil,

        @PastOrPresent(message = "Data de nascimento deve ser no passado ou presente")
        LocalDate dataNascimento,

        @Size(max = 100, message = "Nome da mãe deve ter no máximo 100 caracteres")
        String nomeMae,

        @Size(max = 100, message = "Nome do pai deve ter no máximo 100 caracteres")
        String nomePai
) {
    // Construtor canônico é gerado automaticamente pelo record.
    // Não são necessários métodos de fábrica como fromPessoa aqui, pois este DTO é para ENTRADA de dados.
}