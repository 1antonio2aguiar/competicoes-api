package codiub.competicoes.api.entity.seguranca;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Perfil")
@Table(name = "PERFIS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

}