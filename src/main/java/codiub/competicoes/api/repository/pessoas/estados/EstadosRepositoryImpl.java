package codiub.competicoes.api.repository.pessoas.estados;

import codiub.competicoes.api.entity.pessoas.Estados;
import codiub.competicoes.api.entity.pessoas.Estados_;
import codiub.competicoes.api.filter.pessoas.EstadosFilter;
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

public class EstadosRepositoryImpl implements EstadosRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Estados> filtrar(EstadosFilter estadosFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Estados> criteria = builder.createQuery(Estados.class);
        Root<Estados> root = criteria.from(Estados.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(estadosFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Estados> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(estadosFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Estados> filtrar(EstadosFilter estadosFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Estados> criteria = builder.createQuery(Estados.class);
        Root<Estados> root = criteria.from(Estados.class);

        Predicate[] predicates = criarRestricoes(estadosFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Estados> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            EstadosFilter estadosFilter, CriteriaBuilder builder, Root<Estados> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(estadosFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Estados_.ID), estadosFilter.getId()));
        }

        // DESCRICAO
        if (StringUtils.hasLength(estadosFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Estados_.NOME)),
                            "%" + estadosFilter.getNome().toLowerCase() + "%"));
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

    private Long total(EstadosFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Estados> root = criteria.from(Estados.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
