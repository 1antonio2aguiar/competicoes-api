package codiub.competicoes.api.filter.pessoas;
import lombok.Data;

@Data
public class CepsFilter {
    private Long cep;
    private BairrosFilter bairrosFilter = new BairrosFilter();
    private LogradourosFilter logradourosFilter = new LogradourosFilter();
}
