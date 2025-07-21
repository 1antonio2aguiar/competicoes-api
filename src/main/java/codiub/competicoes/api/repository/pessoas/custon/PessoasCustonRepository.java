package codiub.competicoes.api.repository.pessoas.custon;

import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Repository
public class PessoasCustonRepository {
    @PersistenceContext
    private EntityManager manager;

    public Page<Pessoas> pessoaNotInEquipes(PessoasFilter filter, Pageable pageable) {
        //System.err.println("FILTER " + filter.getDadosPessoaFisicaFilter().getDataNascimento());

        String nomeFilter = filter.getNome();
        String cpfFilter = filter.getDadosPessoaFisicaFilter().getCpf();


        Date dataNascimentoFilter = filter.getDadosPessoaFisicaFilter().getDataNascimento();
        LocalDate dataInicio = null;
        LocalDate dataFim = null;

        String queryStr = "SELECT p FROM Pessoas p WHERE NOT EXISTS (SELECT 1 FROM Atleta a WHERE a.pessoa.id = p.id)";
        String countQueryStr = "SELECT COUNT(p) FROM Pessoas p WHERE NOT EXISTS (SELECT 1 FROM Atleta a WHERE a.pessoa.id = p.id)";

        if (nomeFilter != null && !nomeFilter.isEmpty()) {
            queryStr += " AND UPPER(p.nome) LIKE UPPER(:nome)";
            countQueryStr += " AND UPPER(p.nome) LIKE UPPER(:nome)";
        }

        if (cpfFilter != null && !cpfFilter.isEmpty()) {
            queryStr += " AND UPPER(p.dadosPessoaFisica.cpf) LIKE UPPER(:cpf)"; // Correção: dadosPessoaFisica.cpf
            countQueryStr += " AND UPPER(p.dadosPessoaFisica.cpf) LIKE UPPER(:cpf)"; // Correção: dadosPessoaFisica.cpf
        }

        if (dataNascimentoFilter != null) {
            // Define o formato da data recebida do frontend
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            // Converte a data para LocalDate
            dataInicio = Instant.ofEpochMilli(dataNascimentoFilter.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dataFim = dataInicio.plusDays(1);

            queryStr += " AND p.dadosPessoaFisica.dataNascimento BETWEEN :dataInicio AND :dataFim";
            countQueryStr += " AND p.dadosPessoaFisica.dataNascimento BETWEEN :dataInicio AND :dataFim";
        }

        Query query = manager.createQuery(queryStr, Pessoas.class);
        Query countQuery = manager.createQuery(countQueryStr);

        if (nomeFilter != null && !nomeFilter.isEmpty()) {
            query.setParameter("nome", "%" + nomeFilter.toUpperCase() + "%");
            countQuery.setParameter("nome", "%" + nomeFilter.toUpperCase() + "%");
        }

        if (cpfFilter != null && !cpfFilter.isEmpty()) {
            query.setParameter("cpf", "%" + cpfFilter.toUpperCase() + "%");
            countQuery.setParameter("cpf", "%" + cpfFilter.toUpperCase() + "%");
        }

        if (dataNascimentoFilter != null) {
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            countQuery.setParameter("dataInicio", dataInicio);
            countQuery.setParameter("dataFim", dataFim);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Pessoas> pessoas = query.getResultList();
        Long total = (Long) countQuery.getSingleResult();

        return new PageImpl<>(pessoas, pageable, total);
    }
}
