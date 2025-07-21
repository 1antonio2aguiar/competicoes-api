package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Paises")
@Table(name = "vw_paises")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paises implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sigla;
    private String nacionalidade;
}

