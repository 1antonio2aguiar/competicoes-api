package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Distritos")
@Table(name = "vw_distritos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Distritos implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CIDADES_ID")
    private Cidades cidades;
}

