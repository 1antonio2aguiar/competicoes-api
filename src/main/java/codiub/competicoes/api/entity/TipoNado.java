package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.categoria.DadosCategoriaRcd;
import codiub.competicoes.api.DTO.tiposNados.DadosTipoNadoRcd;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TipoNado")
@Table(name = "tipos_nados")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TipoNado {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;

    public TipoNado(DadosTipoNadoRcd dados) {
        this.descricao = dados.descricao().toUpperCase();
    }
}

