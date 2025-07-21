package codiub.competicoes.api.filter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import lombok.Data;

@Data
public class AtletaFilter {
    private Long id;
    private String categoria;
    private String pessoaNome;
    private String equipeNome;
}
