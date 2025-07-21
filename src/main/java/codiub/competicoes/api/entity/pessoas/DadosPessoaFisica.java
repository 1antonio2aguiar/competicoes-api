package codiub.competicoes.api.entity.pessoas;

import codiub.competicoes.api.entity.enuns.Situacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(of = "id")
@Entity(name = "DadosPessoaFisica")
@Table(name = "vw_dados_pessoa_fisica")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DadosPessoaFisica implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MapsId("id") // Usa o mesmo ID da classe Pessoas
    private Long id;

    @Column(length = 11, unique = true)
    private String cpf;

    @Column(length = 1)
    private String sexo;

    @Column(name = "estado_civil", length = 1)
    private String estadoCivil;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "nome_mae", length = 100)
    private String nomeMae;

    @Column(name = "nome_pai", length = 100)
    private String nomePai;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false) //evita a criação de uma chave estrangeira repetida
    private Pessoas pessoa;
}

