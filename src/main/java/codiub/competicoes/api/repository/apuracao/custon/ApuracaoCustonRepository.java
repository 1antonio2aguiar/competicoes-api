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

@Repository
public class ApuracaoCustonRepository {

    @PersistenceContext
    private EntityManager manager;

    public Page<DadosListApuracaoAndInscricaoRcd> apuracaoAndInscricao(ApuracaoFilter filter, Pageable pageable) {
        Integer provaFilter = filter.getProvaId();
        Integer serieFilter = filter.getSerie();
        String nomeAtletaFilter = filter.getAtletaNome();

        StringBuilder queryBuilder = new StringBuilder("""
            SELECT
                i.id as inscricaoId,
                a.id as apuracaoId,          
                i.prova_id as provaId,
                eq.nome as equipeNome,
                i.atleta_id as atletaId,
                pes.nome as atletaNome,
                i.serie as serie,
                i.baliza as baliza,
                COALESCE(a.resultado, 0) as resultado, 
                i.tipo_inscricao as tipoInscricao 
            FROM
                Inscricoes i
            JOIN
                Atletas at ON i.atleta_id = at.id   
            JOIN
                Pessoas pes ON at.pessoa_id = pes.id 
            JOIN
                Equipes eq ON at.equipe_id = eq.id 
            LEFT JOIN
                Apuracoes a ON i.id = a.inscricao_id 
            WHERE
                i.prova_id = :provaId and 
                i.status in (0,1,2) 
        """); // A cláusula WHERE inicial filtra por provaId

        // --- Adicionar Filtros Condicionais à Cláusula WHERE ---
        if (serieFilter != null && serieFilter > 0) {
            queryBuilder.append(" AND i.serie = :serieFilter ");
        }
        if (nomeAtletaFilter != null && !nomeAtletaFilter.isBlank()) {
            queryBuilder.append(" AND LOWER(pes.nome) LIKE LOWER(:nomeAtletaFilter) ");
        }


        StringBuilder countQueryBuilder = new StringBuilder("""
            SELECT COUNT(i.id)  -- Conta as inscrições que correspondem aos filtros
            FROM
                Inscricoes i
            JOIN
                Pessoas pes ON i.atleta_id = pes.id
            JOIN
                Atletas at ON i.atleta_id = at.id
            JOIN
                Equipes eq ON at.equipe_id = eq.id
            LEFT JOIN
                Apuracoes a ON i.id = a.inscricao_id -- Mesmo LEFT JOIN
            WHERE
                i.prova_id = :provaId and 
                i.status in (0,1,2) 
        """);

        // Adiciona os mesmos filtros condicionais à query de contagem
        if (serieFilter != null && serieFilter > 0) {
            countQueryBuilder.append(" AND i.serie = :serieFilter ");
        }
        if (nomeAtletaFilter != null && !nomeAtletaFilter.isBlank()) {
            countQueryBuilder.append(" AND LOWER(pes.nome) LIKE LOWER(:nomeAtletaFilter) ");
        }

        // --- Adicionar Ordenação à Query Principal ---
        // Certifique-se que as colunas de ordenação existem (ex: i.serie, i.baliza)
        queryBuilder.append(" ORDER BY i.serie, i.baliza ");


        String querySql = queryBuilder.toString();
        String countQuerySql = countQueryBuilder.toString();

        // --- Criar Queries (Principal e Contagem) ---
        Query query = manager.createNativeQuery(querySql);
        Query countQuery = manager.createNativeQuery(countQuerySql);

        query.setParameter("provaId", provaFilter);
        countQuery.setParameter("provaId", provaFilter);

        // Série (condicional)
        if (serieFilter != null && serieFilter > 0) {
            query.setParameter("serieFilter", serieFilter);
            countQuery.setParameter("serieFilter", serieFilter);
        }

        // Nome Atleta (condicional)
        if (nomeAtletaFilter != null && !nomeAtletaFilter.isBlank()) {
            String likePattern = "%" + nomeAtletaFilter + "%";
            query.setParameter("nomeAtletaFilter", likePattern);
            countQuery.setParameter("nomeAtletaFilter", likePattern);
        }

        query.unwrap(NativeQuery.class) // Desembrulha para NativeQuery do Hibernate
                .setTupleTransformer((tuple, aliases) -> {
                    // Mapeia manualmente os resultados do array 'tuple' para a DTO
                    // A ordem e os tipos devem corresponder ao SELECT
                    return new DadosListApuracaoAndInscricaoRcd(
                            (Long) tuple[0], // inscricaoId
                            (Long) tuple[1], // apuracaoId
                            (Long) tuple[2], // provaId
                            (String) tuple[3], // equipeNome
                            (Long) tuple[4], // atletaId
                            (String) tuple[5], // atletaNome
                            (Integer) tuple[6], // serie
                            (Integer) tuple[7], // baliza
                            (Long) tuple[8], // resultado
                            (Integer) tuple[9] // tipoInscricao
                    );
                });

        // --- Aplicar Paginação ---
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // --- Executar Queries ---
        @SuppressWarnings("unchecked")
        List<DadosListApuracaoAndInscricaoRcd> resultados = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        // --- Retornar Página ---
        return new PageImpl<>(resultados, pageable, total);
    }
}