package codiub.competicoes.api.DTO.pessoas.pessoa;
import java.time.LocalDate;

public record PessoaApiRequest(
        String nome,
        String observacao,
        Integer situacao, // Código do Enum
        Long tipoPessoaId,
        String cpf,
        String sexo,
        Integer estadoCivil, // Código do Enum
        String dataNascimento, // Mantenha como String "YYYY-MM-DD" para envio via JSON
        String nomeMae,
        String nomePai
) {}