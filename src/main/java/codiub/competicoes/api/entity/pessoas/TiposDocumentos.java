package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "TiposDocumentos")
@Table(name = "vw_tipos_documentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TiposDocumentos implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
}

