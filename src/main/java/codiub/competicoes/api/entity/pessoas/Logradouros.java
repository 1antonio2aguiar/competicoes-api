package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Logradouros")
@Table(name = "vw_logradouros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Logradouros implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String nomeReduzido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRITO_ID")
    private Distritos distritos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_LOGRADOURO_ID")
    private TiposLogradouros tiposLogradouros;
}

