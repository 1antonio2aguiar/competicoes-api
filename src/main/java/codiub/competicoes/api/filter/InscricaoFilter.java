package codiub.competicoes.api.filter;
import lombok.Data;

@Data
public class InscricaoFilter {
    private Long id;
    private Integer serie;
    private Integer status;
    private Integer tipoInscricao;
    private Integer provaId;

    private ProvaFilter provaFilter = new ProvaFilter();
    private AtletaFilter atletaFilter = new AtletaFilter();
}
