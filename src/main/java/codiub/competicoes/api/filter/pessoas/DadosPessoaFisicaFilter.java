package codiub.competicoes.api.filter.pessoas;
import lombok.Data;

import java.util.Date;

@Data
public class DadosPessoaFisicaFilter {
    private String cpf;
    private Date dataNascimento;
}
