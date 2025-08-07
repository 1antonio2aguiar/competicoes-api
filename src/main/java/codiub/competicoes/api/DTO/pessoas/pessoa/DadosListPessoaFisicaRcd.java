package codiub.competicoes.api.DTO.pessoas.pessoa;

import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica; // Importar a entidade correta
import codiub.competicoes.api.entity.pessoas.TiposPessoas;

import java.time.LocalDate;
import java.util.Optional;

public record DadosListPessoaFisicaRcd(
        Long id,
        String nome,
        String fisicaJuridica, // "F"
        String situacao, // Descrição do Enum
        Long tipoPessoaId,
        String tipoPessoaNome,
        String cpf,
        String sexo,
        String estadoCivil,
        LocalDate dataNascimento,
        String nomeMae,
        String nomePai,
        String observacao
) {
    // Método de fábrica para converter da entidade PessoaFisica para este DTO
    public static DadosListPessoaFisicaRcd fromPessoaFisica(PessoaFisica pf) {
        if (pf == null) {
            return null;
        }
        TiposPessoas tp = pf.getTiposPessoas(); // Para evitar NullPointerException
        String tipoPessoaNome = (tp != null) ? tp.getNome() : null;
        Long tipoPessoaId = (tp != null) ? tp.getId() : null;

        return new DadosListPessoaFisicaRcd(
                pf.getId(),
                pf.getNome(),
    "F",
                pf.getSituacao() != null ? pf.getSituacao().getDescricao() : null,
                pf.getTiposPessoas().getId(),
                pf.getTiposPessoas().getNome(),
                pf.getCpf(),
                pf.getSexo(),
                pf.getEstadoCivil() != null ? pf.getEstadoCivil().getDescricao() : null,
                pf.getDataNascimento(),
                pf.getNomeMae(),
                pf.getNomePai(),
                pf.getObservacao()
        );
    }

    // Opcional: Se você tiver um Optional<PessoaFisica>
    public static DadosListPessoaFisicaRcd fromOptionalPessoaFisica(Optional<PessoaFisica> pfOptional) {
        return pfOptional.map(DadosListPessoaFisicaRcd::fromPessoaFisica).orElse(null);
    }
}