package codiub.competicoes.api.filter;
import lombok.Data;

@Data
public class ApuracaoFilter {
    private Long id;
    private Integer provaId;
    private Integer serie;
    private String atletaNome;

    private ProvaFilter provaFilter = new ProvaFilter();
    private AtletaFilter atletaFilter = new AtletaFilter();
}
