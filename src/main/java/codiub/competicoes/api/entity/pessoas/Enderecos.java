package codiub.competicoes.api.entity.pessoas;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(of = "id")
@Entity(name = "Enderecos")
@Table(name = "vw_enderecos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enderecos implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bairro;
    private Long logradouro;
    private Long numero;
    private String complemento;
    private Long cep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESSOA_ID")
    private Pessoas pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_ENDERECO_ID")
    private TiposEnderecos tiposEnderecos;
}

