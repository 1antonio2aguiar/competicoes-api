package codiub.competicoes.api.DTO.pessoas.pessoa;

import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica;

import java.time.LocalDate;

public record DadosPessoaFisicaReduzRcd(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento

) {
    public static DadosPessoaFisicaReduzRcd fromPessoaFisica(PessoaFisica dados) {
        return new DadosPessoaFisicaReduzRcd(
                dados.getId(),
                dados.getNome(),
                dados.getCpf(),
                dados.getDataNascimento());
    }
}
