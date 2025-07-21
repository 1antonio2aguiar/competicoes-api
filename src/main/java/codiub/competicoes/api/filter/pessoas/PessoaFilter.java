package codiub.competicoes.api.filter.pessoas;
import lombok.Data;

import java.util.Date;

@Data
public class PessoaFilter {
    private Long id;
    private String nome;
    private String cpf;
    private String cnpj;
    private Date dataNascimento;
}
