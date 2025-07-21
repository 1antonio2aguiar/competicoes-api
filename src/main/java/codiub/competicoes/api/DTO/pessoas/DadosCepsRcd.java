package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Ceps;
import codiub.competicoes.api.entity.pessoas.Logradouros;

import java.util.Optional;

public record DadosCepsRcd(
        long id,
        long cep,
        long numero_ini,
        long numero_fin,
        String identificacao,
        long bairro,
        String bairroNome,
        long logradouro,
        String logradouroNome

) {
    public DadosCepsRcd(Ceps ceps) {
        this(ceps.getId(),
            ceps.getCep(),
            ceps.getNumero_ini(),
            ceps.getNumero_fin(),
            ceps.getIdentificacao(),
            ceps.getBairros().getId(),
            ceps.getBairros().getNome(),
            ceps.getLogradouros().getId(),
            ceps.getLogradouros().getNome());
    }

    public DadosCepsRcd(Optional<Ceps> ceps) {
        this(ceps.get().getId(),
            ceps.get().getCep(),
            ceps.get().getNumero_ini(),
            ceps.get().getNumero_fin(),
            ceps.get().getIdentificacao(),
            ceps.get().getBairros().getId(),
            ceps.get().getBairros().getNome(),
            ceps.get().getLogradouros().getId(),
            ceps.get().getLogradouros().getNome());
    }
}
