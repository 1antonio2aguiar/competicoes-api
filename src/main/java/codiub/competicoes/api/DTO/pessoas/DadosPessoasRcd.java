package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.entity.pessoas.Logradouros;
import codiub.competicoes.api.entity.pessoas.Pessoas;

import java.time.LocalDate;
import java.util.Optional;

public record DadosPessoasRcd(
        Long id,
        String nome,
        String fisica_juridica,
        String situacao ,
        Long tipoPessoa,
        String tipoPessoaNome,
        String cpf,
        String sexo,
        String estadoCivil,
        LocalDate dataNascimento
) {
    public static DadosPessoasRcd fromPessoas(Pessoas pessoas) {
        return new DadosPessoasRcd(pessoas.getId(),
                pessoas.getNome(),
                pessoas.getFisica_juridica(),
                pessoas.getSituacao().getDescricao(),
                pessoas.getTiposPessoas().getId() ,
                pessoas.getTiposPessoas().getNome(),
                pessoas.getDadosPessoaFisica().getCpf(),
                pessoas.getDadosPessoaFisica().getSexo(),
                pessoas.getDadosPessoaFisica().getEstadoCivil(),
                pessoas.getDadosPessoaFisica().getDataNascimento());
    }

    public DadosPessoasRcd(Optional<Pessoas> pessoas) {
        this(pessoas.get().getId(),
                pessoas.get().getNome(),
                pessoas.get().getFisica_juridica(),
                pessoas.get().getSituacao().getDescricao(),
                pessoas.get().getTiposPessoas().getId(),
                pessoas.get().getTiposPessoas().getNome(),
                pessoas.get().getDadosPessoaFisica().getCpf(),
                pessoas.get().getDadosPessoaFisica().getSexo(),
                pessoas.get().getDadosPessoaFisica().getEstadoCivil(),
                pessoas.get().getDadosPessoaFisica().getDataNascimento());
    }

    public static DadosPessoasRcd fromOptionalEquipe(Optional<Pessoas> pessoasOptional) {
        return pessoasOptional.map(DadosPessoasRcd::fromPessoas).orElse(null);
    }

}
