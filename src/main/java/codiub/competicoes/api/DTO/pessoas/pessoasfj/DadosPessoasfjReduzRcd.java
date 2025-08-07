package codiub.competicoes.api.DTO.pessoas.pessoasfj;

import java.time.LocalDate;

public record DadosPessoasfjReduzRcd(
        Long id,
        String nome,
        String fisicaJuridica,
        String cpf,
        LocalDate dataNascimento,
        String cnpj

) {

}
