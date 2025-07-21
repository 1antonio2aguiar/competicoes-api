package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TiposLogradouros")
@Table(name = "vw_tipos_logradouros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposLogradouros implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private String sigla;
}

