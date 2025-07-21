package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.tiposVeiculos.DadosTiposVeiculosRcd;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tipos_veiculos")
@Entity(name = "TiposVeiculos")
@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposVeiculos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;

    public TiposVeiculos(DadosTiposVeiculosRcd dados) {
        this.descricao = dados.descricao().toUpperCase();
    }

}

