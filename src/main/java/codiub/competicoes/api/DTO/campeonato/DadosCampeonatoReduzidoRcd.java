package codiub.competicoes.api.DTO.campeonato;

import codiub.competicoes.api.entity.Campeonato;

import java.time.LocalDate;

public record DadosCampeonatoReduzidoRcd(
        Long id,
        String nome
) {
    public static DadosCampeonatoReduzidoRcd fromCampeonatos(Campeonato dados) {
        return new DadosCampeonatoReduzidoRcd(
                dados.getId(),
                dados.getNome());
    }
}
