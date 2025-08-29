package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.etapa.DadosInsertEtapaRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosModalidadeRcd;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(of = "id")
@Entity(name = "Etapa")
@Table(name = "etapas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Etapa {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private Date dataEtapa;
    private Date dataInscricao;
    private String pontua;
    private String acumula;
    private Integer qtdBalizas;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;

    @ManyToOne
    @JoinColumn(name = "local_id")
    private LocaisCompeticoes localCompeticao;

    public Etapa(DadosInsertEtapaRcd dados) {
        this.nome = dados.nome().toUpperCase();
        this.descricao = dados.descricao();
    }

    public Etapa(Etapa etapasSalva) {
    }

}

