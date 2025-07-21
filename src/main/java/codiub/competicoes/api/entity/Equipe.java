package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Equipe")
@Table(name = "equipes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Equipe {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sigla;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    // O mesmo para agremiacao_id
    @Column(name = "agremiacao_id", nullable = false)
    private Long agremiacaoId;

    // Referência para Modalidade (1 para 1)
    @ManyToOne
    @JoinColumn(name = "modalidade_id", nullable = false)
    private TiposModalidades modalidade;

    @Column(name = "tecnico_id")
    private Long tecnicoId;

    @Column(name = "assistente_tecnico_id")
    private Long assistenteTecnicoId;

    public Equipe(DadosInsertTipoModalidadeRcd dados) {
        this.nome = dados.nome().toUpperCase();
        this.sigla = dados.descricao();
    }
    public Equipe(Equipe tipoModalidadeSalva) {
    }

}

