package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TiposEndrecos")
@Table(name = "vw_tipos_enderecos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposEnderecos implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
}

