package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Ceps")
@Table(name = "vw_ceps")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ceps implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cep;
    private Long numero_ini;
    private Long numero_fin;
    private String identificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOGRADOURO_ID")
    private Logradouros logradouros;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BAIRRO_ID")
    private Bairros bairros;
}

