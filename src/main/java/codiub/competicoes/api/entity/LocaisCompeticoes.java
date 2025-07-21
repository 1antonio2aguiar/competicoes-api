package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.locaisCompeticoes.DadosInsertLocaisCompeticoesRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "LocaisCompeticoes")
@Table(name = "locais_competicoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocaisCompeticoes {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public LocaisCompeticoes(DadosInsertLocaisCompeticoesRcd dados) {
        this.nome = dados.nome().toUpperCase();
        this.endereco = dados.endereco();
    }

    public LocaisCompeticoes(LocaisCompeticoes locaisCompeticoes) {
    }

}

