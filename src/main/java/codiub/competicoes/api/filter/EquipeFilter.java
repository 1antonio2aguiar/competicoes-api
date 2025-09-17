package codiub.competicoes.api.filter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import lombok.Data;

@Data
public class EquipeFilter {
    private Long id;
    private Long empresaId;
    private String nome;
    private String agremiacao;
    private String modalidade;

    private EmpresaFilter empresaFilter = new EmpresaFilter();
    private PessoasFilter pessoasFilter = new PessoasFilter();
}
