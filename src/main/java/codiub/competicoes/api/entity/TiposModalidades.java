package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TiposModalidades")
@Table(name = "tipos_modalidades")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposModalidades {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    public TiposModalidades(DadosInsertTipoModalidadeRcd dados) {
        this.nome = dados.nome().toUpperCase();
        this.descricao = dados.descricao();
    }

    public TiposModalidades(TiposModalidades tipoModalidadeSalva) {
    }

}

