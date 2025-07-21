package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Pessoas;

import java.time.LocalDate;
import java.util.Date;

public record DadosPessoasReduzidoRcd(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento

) {
    public static DadosPessoasReduzidoRcd fromPessoas(Pessoas dados) {
        return new DadosPessoasReduzidoRcd(
                dados.getId(),
                dados.getNome(),
                dados.getDadosPessoaFisica().getCpf(),
                dados.getDadosPessoaFisica().getDataNascimento());
    }
}
