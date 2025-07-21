package codiub.competicoes.api.entity;

import codiub.competicoes.api.DTO.pontuacao.DadosPontuacaoRcd;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "id")
@Entity(name = "Pontuacao")
@Table(name = "pontuacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pontuacao {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String classificacao;
    private Long pontos;

    public Pontuacao(DadosPontuacaoRcd dados) {
        this.classificacao = dados.classificacao().toUpperCase();
    }

}

