package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Cidades")
@Table(name = "vw_cidades")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cidades implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTADO_ID")
    private Estados estados;
}

