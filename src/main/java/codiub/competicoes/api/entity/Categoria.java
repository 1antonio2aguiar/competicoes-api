package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.categoria.DadosCategoriaRcd;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@EqualsAndHashCode(of = "id")
@Entity(name = "Categoria")
@Table(name = "categorias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Categoria {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private Date dataIniCategoria;
    private Date dataFinCategoria;

    public Categoria(DadosCategoriaRcd dados) {
        this.descricao = dados.descricao().toUpperCase();
    }

}

