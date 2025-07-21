package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TiposPessoas")
@Table(name = "vw_tipos_pessoas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposPessoas implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
}

