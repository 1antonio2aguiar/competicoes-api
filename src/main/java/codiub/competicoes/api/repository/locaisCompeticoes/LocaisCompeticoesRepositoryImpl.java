package codiub.competicoes.api.repository.locaisCompeticoes;
import codiub.competicoes.api.entity.LocaisCompeticoes;
import codiub.competicoes.api.entity.LocaisCompeticoes_;
import codiub.competicoes.api.filter.LocaisCompeticoesFilter;
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

public class LocaisCompeticoesRepositoryImpl implements LocaisCompeticoesRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<LocaisCompeticoes> filtrar(LocaisCompeticoesFilter locaisCompeticoesFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LocaisCompeticoes> criteria = builder.createQuery(LocaisCompeticoes.class);
        Root<LocaisCompeticoes> root = criteria.from(LocaisCompeticoes.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(locaisCompeticoesFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<LocaisCompeticoes> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(locaisCompeticoesFilter));
    }

    //Aqui da lista sem paginacao
    @Override
    public List<LocaisCompeticoes> filtrar(LocaisCompeticoesFilter locaisCompeticoesFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LocaisCompeticoes> criteria = builder.createQuery(LocaisCompeticoes.class);
        Root<LocaisCompeticoes> root = criteria.from(LocaisCompeticoes.class);

        Predicate[] predicates = criarRestricoes(locaisCompeticoesFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<LocaisCompeticoes> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            LocaisCompeticoesFilter locaisCompeticoesFilter, CriteriaBuilder builder, Root<LocaisCompeticoes> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(locaisCompeticoesFilter.getId() != null) {
            predicates.add(builder.equal(root.get(LocaisCompeticoes_.ID), locaisCompeticoesFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(locaisCompeticoesFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(LocaisCompeticoes_.NOME)),
                            "%" + locaisCompeticoesFilter.getNome().toLowerCase() + "%"));
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

    private Long total(LocaisCompeticoesFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<LocaisCompeticoes> root = criteria.from(LocaisCompeticoes.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}