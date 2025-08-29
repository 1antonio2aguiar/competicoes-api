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
import java.util.Set;

@Repository
public class AtletaCustonRepository {

    // --- ESTA É A LINHA QUE ESTAVA FALTANDO ---
    @PersistenceContext
    private EntityManager manager;
    // ------------------------------------------

    public Page<Atleta> atletaNotInInscricoes(AtletaFilter filter, Pageable pageable, Set<Long> pessoaIdsFiltrados) {
        Long provaId = filter.getProvaId();

        if (provaId == null) {
            throw new IllegalArgumentException("O ID da prova (provaId) é obrigatório para esta consulta.");
        }

        StringBuilder queryStrBuilder = new StringBuilder(
                "SELECT a FROM Atleta a " +
                        "JOIN FETCH a.equipe e " +
                        "WHERE NOT EXISTS (" +
                        "  SELECT i FROM Inscricao i " +
                        "  WHERE i.atleta = a AND i.prova.id = :provaId" +
                        ")"
        );

        StringBuilder countQueryStrBuilder = new StringBuilder(
                "SELECT COUNT(a) FROM Atleta a " +
                        "WHERE NOT EXISTS (" +
                        "  SELECT i FROM Inscricao i " +
                        "  WHERE i.atleta = a AND i.prova.id = :provaId" +
                        ")"
        );

        if (pessoaIdsFiltrados != null && !pessoaIdsFiltrados.isEmpty()) {
            queryStrBuilder.append(" AND a.pessoaId IN :pessoaIds");
            countQueryStrBuilder.append(" AND a.pessoaId IN :pessoaIds");
        }

        Query query = manager.createQuery(queryStrBuilder.toString(), Atleta.class);
        Query countQuery = manager.createQuery(countQueryStrBuilder.toString());

        query.setParameter("provaId", provaId);
        countQuery.setParameter("provaId", provaId);

        if (pessoaIdsFiltrados != null && !pessoaIdsFiltrados.isEmpty()) {
            query.setParameter("pessoaIds", pessoaIdsFiltrados);
            countQuery.setParameter("pessoaIds", pessoaIdsFiltrados);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Atleta> atletas = query.getResultList();
        Long total = (Long) countQuery.getSingleResult();

        return new PageImpl<>(atletas, pageable, total);
    }
}