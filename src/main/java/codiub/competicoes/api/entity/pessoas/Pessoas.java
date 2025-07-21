package codiub.competicoes.api.entity.pessoas;

import codiub.competicoes.api.entity.enuns.Situacao;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Pessoas")
@Table(name = "vw_pessoas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pessoas implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String fisica_juridica;
    private String observacao;
    private Situacao situacao;

    @ManyToOne
    @JoinColumn(name = "TIPO_PESSOA_ID")
    private TiposPessoas tiposPessoas;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DadosPessoaFisica dadosPessoaFisica;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DadosPessoaJuridica dadosPessoaJuridica;
}

