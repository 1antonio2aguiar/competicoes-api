package codiub.competicoes.api.filter.pessoas;
import lombok.Data;

import java.util.Date;

@Data
public class PessoaFisicaFilter {
    private Long empresaId;
    private Long id;
    private String nome;
    private String cpf;
    private Date dataNascimento;
}
