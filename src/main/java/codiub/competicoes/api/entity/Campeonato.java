package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.campeonato.DadosInsertCampeonatoRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Campeonato")
@Table(name = "campeonatos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Campeonato {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "modalidade_id")
    private TiposModalidades modalidade;

    public Campeonato(DadosInsertCampeonatoRcd dados) {
        this.nome = dados.nome().toUpperCase();
        this.descricao = dados.descricao();
    }

    public Campeonato(Campeonato tipoModalidadeSalva) {
    }
}
