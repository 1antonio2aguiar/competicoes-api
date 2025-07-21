package codiub.competicoes.api.DTO.tiposNados;

import codiub.competicoes.api.entity.TipoNado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record DadosTipoNadoRcd(
        long id,
        @NotBlank(message = "descricao.obrigatório")
        @Size(max = 500, min = 3)
        String descricao
) {
    public DadosTipoNadoRcd(TipoNado tipoNado) {
        this(tipoNado.getId(),
                tipoNado.getDescricao());
    }

    public DadosTipoNadoRcd(Optional<TipoNado> tipoNado) {
        this(tipoNado.get().getId(),
                tipoNado.get().getDescricao());
    }

    public String getDescricao(){
                return descricao.toUpperCase();
        }
}
