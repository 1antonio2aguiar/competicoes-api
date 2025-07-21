package codiub.competicoes.api.filter.pessoas;
import lombok.Data;

@Data
public class BairrosFilter {
    private Long id;
    private String nome;
    private DistritosFilter distritosFilter = new DistritosFilter();
}
