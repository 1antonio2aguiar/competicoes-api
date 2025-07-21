package codiub.competicoes.api.repository.atleta.custon;

import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.filter.AtletaFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AtletaCustonRepository {

    @PersistenceContext
    private EntityManager manager;

    public Page<Atleta> atletaNotInInscricoes(AtletaFilter filter, Pageable pageable) {
        String nomeFilter = filter.getPessoaNome();

        String queryStr = "SELECT a FROM Atleta a " +
                "JOIN FETCH a.pessoa p " + // Carrega os dados da pessoa junto
                "JOIN FETCH a.equipe e " +  // Carrega os dados da equipe junto
                "WHERE NOT EXISTS (SELECT i FROM Inscricao i WHERE i.atleta = a)";

        String countQueryStr = "SELECT COUNT(a) FROM Atleta a " +
                "WHERE NOT EXISTS (SELECT i FROM Inscricao i WHERE i.atleta = a)";

        if (nomeFilter != null && !nomeFilter.isEmpty()) {
            queryStr += " AND UPPER(a.pessoa.nome) LIKE UPPER(:nome)";
            countQueryStr += " AND UPPER(a.pessoa.nome) LIKE UPPER(:nome)";
        }

        Query query = manager.createQuery(queryStr, Atleta.class); // Especifica Atleta.class
        Query countQuery = manager.createQuery(countQueryStr);

        if (nomeFilter != null && !nomeFilter.isEmpty()) {
            query.setParameter("nome", "%" + nomeFilter.toUpperCase() + "%");
            countQuery.setParameter("nome", "%" + nomeFilter.toUpperCase() + "%");
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Atleta> atletas = query.getResultList();
        Long total = (Long) countQuery.getSingleResult();

        return new PageImpl<>(atletas, pageable, total);
    }
}