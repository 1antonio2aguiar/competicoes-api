package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Contatos")
@Table(name = "vw_contatos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Contatos implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESSOA_ID")
    private Pessoas pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_CONTATO_ID")
    private TiposContatos tiposContatos;
}

