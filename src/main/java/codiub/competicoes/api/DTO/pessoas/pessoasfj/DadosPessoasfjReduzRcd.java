package codiub.competicoes.api.DTO.pessoas.pessoasfj;

import java.time.LocalDate;

public record DadosPessoasfjReduzRcd(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String cnpj

) {

}
