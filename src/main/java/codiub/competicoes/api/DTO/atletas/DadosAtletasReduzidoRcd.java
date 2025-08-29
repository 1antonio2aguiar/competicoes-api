package codiub.competicoes.api.DTO.atletas;

import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.entity.pessoas.Pessoas;

import java.time.LocalDate;

public record DadosAtletasReduzidoRcd(
        Long id,
        String nome,
        String equipeNome
) {

}
