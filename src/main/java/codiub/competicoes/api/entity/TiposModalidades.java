package codiub.competicoes.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TiposModalidades")
@Table(name = "tipos_modalidades")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposModalidades {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

}

