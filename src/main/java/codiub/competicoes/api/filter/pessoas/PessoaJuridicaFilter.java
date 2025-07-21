package codiub.competicoes.api.filter.pessoas;
import lombok.Data;

@Data
public class PessoaJuridicaFilter {
    private Long id;
    private String nome;
    private String cnpj;
}
