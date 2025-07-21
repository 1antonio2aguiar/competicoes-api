package codiub.competicoes.api.DTO.categoria;

import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.Optional;

public record DadosCategoriaRcd(
        long id,
        @NotBlank(message = "descricao.obrigatório")
        @Size(max = 500, min = 3)
        String descricao,
        Date dataIniCategoria,
        Date dataFinCategoria

) {
    public DadosCategoriaRcd(Categoria categoria) {
        this(categoria.getId(),
                categoria.getDescricao(),
                categoria.getDataIniCategoria(),
                categoria.getDataFinCategoria());
    }

    public static DadosCategoriaRcd fromCategoria(Categoria categoria) {
        return new DadosCategoriaRcd(
                categoria.getId(),
                categoria.getDescricao(),
                categoria.getDataIniCategoria(),
                categoria.getDataFinCategoria());
    }

    public DadosCategoriaRcd(Optional<Categoria> categoria) {
        this(categoria.get().getId(),
                categoria.get().getDescricao(),
                categoria.get().getDataIniCategoria(),
                categoria.get().getDataFinCategoria());
    }
    public String getDescricao(){
                return descricao.toUpperCase();
        }
}
