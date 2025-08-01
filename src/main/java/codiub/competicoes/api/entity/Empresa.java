package codiub.competicoes.api.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Empresa")
@Table(name = "vw_empresas")
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
    private String cnpj;
    private String nome;
    private String sigla;
    private String logo;
    private char situacao;
    private char filial;
    private Long matriz;
}

