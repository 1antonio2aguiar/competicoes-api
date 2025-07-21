package codiub.competicoes.api.filter;
import lombok.Data;

@Data
public class EtapaFilter {
    private Long id;
    private String nome;
    private String descricao;

    private CampeonatoFilter campeonatoFilter = new CampeonatoFilter();
}
