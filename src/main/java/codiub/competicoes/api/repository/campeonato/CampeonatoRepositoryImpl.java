package codiub.competicoes.api.repository.campeonato;

import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Campeonato_;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.CampeonatoFilter;
import codiub.competicoes.api.filter.TiposModalidadesFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CampeonatoRepositoryImpl implements CampeonatoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Campeonato> filtrar(CampeonatoFilter campeonatoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Campeonato> criteria = builder.createQuery(Campeonato.class);
        Root<Campeonato> root = criteria.from(Campeonato.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(campeonatoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Campeonato> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(campeonatoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Campeonato> filtrar(CampeonatoFilter campeonatoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Campeonato> criteria = builder.createQuery(Campeonato.class);
        Root<Campeonato> root = criteria.from(Campeonato.class);

        Predicate[] predicates = criarRestricoes(campeonatoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Campeonato> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            CampeonatoFilter campeonatoFilter, CriteriaBuilder builder, Root<Campeonato> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(campeonatoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Campeonato_.ID), campeonatoFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(campeonatoFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Campeonato_.NOME)),
                            "%" + campeonatoFilter.getNome().toLowerCase() + "%"));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }
    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(CampeonatoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Campeonato> root = criteria.from(Campeonato.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
