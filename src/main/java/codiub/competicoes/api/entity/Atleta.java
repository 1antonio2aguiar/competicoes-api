package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(of = "id")
@Entity(name = "Atleta")
@Table(name = "atletas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Atleta {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private Pessoas pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}

