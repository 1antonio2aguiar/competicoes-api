package codiub.competicoes.api.repository.apuracao.custon;

import codiub.competicoes.api.DTO.apuracao.DadosListApuracaoAndInscricaoRcd;
import codiub.competicoes.api.filter.ApuracaoFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class ApuracaoCustonRepository {

    @PersistenceContext
    private EntityManager manager;

    public Page<DadosListApuracaoAndInscricaoRcd> apuracaoAndInscricao(ApuracaoFilter filter, Pageable pageable, Set<Long> pessoaIdsFiltrados) {
        Integer provaFilter = filter.getProvaId();
        Integer serieFilter = filter.getSerie();

        // Query principal SEM o JOIN com Pessoas
        StringBuilder queryBuilder = new StringBuilder("""
            SELECT
                i.id as inscricaoId,
                a.id as apuracaoId,
                i.prova_id as provaId,
                eq.nome as equipeNome,
                i.atleta_id as atletaId,
                i.serie as serie,
                i.baliza as baliza,
                COALESCE(a.resultado, 0) as resultado,
                i.tipo_inscricao as tipoInscricao
            FROM
                inscricoes i
            JOIN
                atletas at ON i.atleta_id = at.id
            JOIN
                equipes eq ON at.equipe_id = eq.id
            LEFT JOIN
                apuracoes a ON i.id = a.inscricao_id
            WHERE
                i.prova_id = :provaId AND i.status IN (0,1,2)
        """);

        // Query de contagem SEM o JOIN com Pessoas
        StringBuilder countQueryBuilder = new StringBuilder("""
            SELECT COUNT(i.id) FROM inscricoes i
            JOIN atletas at ON i.atleta_id = at.id
            WHERE i.prova_id = :provaId AND i.status IN (0,1,2)
        """);

        // Adiciona o filtro por pessoaId se ele for fornecido pelo service
        if (pessoaIdsFiltrados != null && !pessoaIdsFiltrados.isEmpty()) {
            queryBuilder.append(" AND at.pessoa_id IN :pessoaIds");
            countQueryBuilder.append(" AND at.pessoa_id IN :pessoaIds");
        }

        if (serieFilter != null && serieFilter > 0) {
            queryBuilder.append(" AND i.serie = :serieFilter ");
            countQueryBuilder.append(" AND i.serie = :serieFilter ");
        }

        queryBuilder.append(" ORDER BY i.serie, i.baliza ");

        Query query = manager.createNativeQuery(queryBuilder.toString());
        Query countQuery = manager.createNativeQuery(countQueryBuilder.toString());

        query.setParameter("provaId", provaFilter);
        countQuery.setParameter("provaId", provaFilter);

        if (pessoaIdsFiltrados != null && !pessoaIdsFiltrados.isEmpty()) {
            query.setParameter("pessoaIds", pessoaIdsFiltrados);
            countQuery.setParameter("pessoaIds", pessoaIdsFiltrados);
        }

        if (serieFilter != null && serieFilter > 0) {
            query.setParameter("serieFilter", serieFilter);
            countQuery.setParameter("serieFilter", serieFilter);
        }

        query.unwrap(NativeQuery.class)
                .setTupleTransformer((tuple, aliases) -> new DadosListApuracaoAndInscricaoRcd(
                        (Long) tuple[0], (Long) tuple[1], (Long) tuple[2], (String) tuple[3],
                        (Long) tuple[4], (Integer) tuple[5], (Integer) tuple[6], (Long) tuple[7],
                        (Integer) tuple[8]
                ));

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<DadosListApuracaoAndInscricaoRcd> resultados = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(resultados, pageable, total);
    }
}