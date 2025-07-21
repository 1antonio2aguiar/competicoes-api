package codiub.competicoes.api.filter.pessoas;
import lombok.Data;

@Data
public class DistritosFilter {
    private Long id;
    private String nome;
    private CidadesFilter cidadesFilter = new CidadesFilter();
}
