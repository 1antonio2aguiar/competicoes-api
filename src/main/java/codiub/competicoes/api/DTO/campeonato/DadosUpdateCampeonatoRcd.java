package codiub.competicoes.api.DTO.campeonato;

import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.TiposModalidades;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosUpdateCampeonatoRcd(
    @NotBlank(message = "nome.obrigatorio")
    String nome,
    String descricao ) {

    public String getNome(){
        return nome.toUpperCase();
    }
}
