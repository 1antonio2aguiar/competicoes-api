package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TiposEstadosCivis")
@Table(name = "vw_tipos_estados_civis")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposEstadosCivis implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
}

