package codiub.competicoes.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Empresa")
@Table(name = "EMPRESAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Empresa {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A anotação @Column é opcional quando o nome do campo é igual ao da coluna,
    // mas é uma boa prática ser explícito.
    @Column(name = "RAZAO_SOCIAL", nullable = false)
    private String razaoSocial;

    @Column(name = "ATIVIDADE", nullable = false)
    private String atividade;

    @Column(name = "TELEFONE", nullable = false)
    private String telefone;

    @Column(name = "CNPJ", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "INSCRICAO_ESTADUAL", nullable = false)
    private String inscricaoEstadual;

}

