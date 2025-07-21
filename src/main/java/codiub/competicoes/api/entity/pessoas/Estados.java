package codiub.competicoes.api.entity.pessoas;

import codiub.competicoes.api.entity.LocaisCompeticoes;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Estados")
@Table(name = "vw_estados")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Estados implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String uf;

    @ManyToOne
    @JoinColumn(name = "PAIS_ID")
    private Paises paises;
}

