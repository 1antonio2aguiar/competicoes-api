package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(of = "id")
@Entity(name = "Documentos")
@Table(name = "vw_documentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Documentos implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero_documento;
    private String orgao_expedidor;
    private Date data_expedicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESSOA_ID")
    private Pessoas pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_DOCUMENTO_ID")
    private TiposDocumentos tiposDocumentos;
}

