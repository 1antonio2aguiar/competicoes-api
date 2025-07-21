package codiub.competicoes.api.DTO.equipe;

import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.entity.pessoas.Pessoas;

import java.time.LocalDate;

public record DadosEquipeReduzidoRcd(
        Long id,
        String nome
) {
    public static DadosEquipeReduzidoRcd fromEquipes(Equipe dados) {
        return new DadosEquipeReduzidoRcd(
                dados.getId(),
                dados.getNome());
    }
}
