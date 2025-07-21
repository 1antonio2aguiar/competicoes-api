package codiub.competicoes.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Parametro")
@Table(name = "parametros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Parametro {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numero;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "etapa_id")
    private Etapa etapa;

}

