package codiub.competicoes.api.DTO.tipoModalidade;

import codiub.competicoes.api.entity.TiposModalidades;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.Optional;

public record DadosModalidadeRcd(
        long id,
        @NotBlank(message = "nome.obrigatório")
        @Size(max = 500, min = 3)
        String nome,
        String descricao

) {
    public String getDescricao(){
                return descricao.toUpperCase();
        }
    public String getNome(){
        return nome.toUpperCase();
    }
}
