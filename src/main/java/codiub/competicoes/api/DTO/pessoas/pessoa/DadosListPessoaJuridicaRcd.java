package codiub.competicoes.api.DTO.pessoas.pessoa;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaJuridica;
import codiub.competicoes.api.entity.pessoas.TiposPessoas;

import java.util.Optional;

public record DadosListPessoaJuridicaRcd(
        Long id,
        String nome, // Razão Social
        String fisicaJuridica, // "J"
        String situacao,
        Long tipoPessoaId,
        String tipoPessoaNome,
        String cnpj,
        String nomeFantasia,
        String objetoSocial,
        String microEmpresa,
        Integer tipoEmpresa,
        String observacao
) {
    public static DadosListPessoaJuridicaRcd fromPessoaJuridica(PessoaJuridica pj) {
        if (pj == null) {
            return null;
        }
        TiposPessoas tp = pj.getTiposPessoas();
        String tipoPessoaNome = (tp != null) ? tp.getNome() : null;
        Long tipoPessoaId = (tp != null) ? tp.getId() : null;

        return new DadosListPessoaJuridicaRcd(
                pj.getId(),
                pj.getNome(),
                "J", // ou pj.getFisicaJuridica()
                pj.getSituacao() != null ? pj.getSituacao().getDescricao() : null,
                tipoPessoaId,
                tipoPessoaNome,
                pj.getCnpj(),
                pj.getNomeFantasia(),
                pj.getObjetoSocial(),
                pj.getMicroEmpresa(),
                pj.getTipoEmpresa(),
                pj.getObservacao()
        );
    }

    public static DadosListPessoaJuridicaRcd fromOptionalPessoaJuridica(Optional<PessoaJuridica> pjOptional) {
        return pjOptional.map(DadosListPessoaJuridicaRcd::fromPessoaJuridica).orElse(null);
    }
}