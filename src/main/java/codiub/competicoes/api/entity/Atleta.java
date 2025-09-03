package codiub.competicoes.api.entity;

import codiub.competicoes.api.entity.pessoas.Pessoas;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@EqualsAndHashCode(of = "id")
@Entity(name = "Atleta")
@Table(name = "atletas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@FilterDef(name = "filtroEmpresa", parameters = @ParamDef(name = "empresaId", type = Long.class))
@Filter(name = "filtroEmpresa", condition = "empresa_id = :empresaId")
public class Atleta {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String observacao;

    @Column(name = "pessoa_id")
    private Long pessoaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}

