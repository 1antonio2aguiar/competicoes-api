package codiub.competicoes.api.filter.pessoas;
import codiub.competicoes.api.filter.CampeonatoFilter;
import lombok.Data;

import java.sql.Date;

@Data
public class PessoasFilter {
    private Long id;
    private String nome;

    private DadosPessoaFisicaFilter dadosPessoaFisicaFilter = new DadosPessoaFisicaFilter();

}
