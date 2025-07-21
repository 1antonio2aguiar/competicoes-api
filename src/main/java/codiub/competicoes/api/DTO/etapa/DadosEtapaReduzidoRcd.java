package codiub.competicoes.api.DTO.etapa;

import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Etapa;

public record DadosEtapaReduzidoRcd(
        Long id,
        String nome
) {
    public static DadosEtapaReduzidoRcd fromEtapas(Etapa dados) {
        return new DadosEtapaReduzidoRcd(
                dados.getId(),
                dados.getNome());
    }
}
