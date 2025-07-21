package codiub.competicoes.api.filter;
import lombok.Data;

@Data
public class CampeonatoFilter {
    private Long id;
    private String modalidade;
    private String nome;
    private String descricao;
}
