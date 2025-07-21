package codiub.competicoes.api.entity;

import codiub.competicoes.api.entity.enuns.StatusApuracao;
import codiub.competicoes.api.entity.enuns.StatusInscricao;
import codiub.competicoes.api.entity.enuns.StatusResultado;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Apuracao")
@Table(name = "apuracoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Apuracao {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "resultado")
    private Long resultado; // Tipo Long para milissegundos
    private StatusApuracao status;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "prova_id")
    private Prova prova;

    @ManyToOne
    @JoinColumn(name = "inscricao_id")
    private Inscricoes inscricao;

    @OneToOne
    @JoinColumn(name = "atleta_id")
    private Atleta atleta;

    @ManyToOne
    @JoinColumn(name = "pontuacao_id")
    private Pontuacao pontuacao;
}

