package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Bairros")
@Table(name = "vw_bairros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bairros implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String nomeAbreviado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRITO_ID")
    private Distritos distritos;
}

