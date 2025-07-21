package codiub.competicoes.api.filter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import lombok.Data;

@Data
public class ProvaFilter {
    private Long id;
    private String tipoNado;

    private EtapaFilter etapaFilter = new EtapaFilter();
    private CategoriaFilter categoriaFilter = new CategoriaFilter();
}
