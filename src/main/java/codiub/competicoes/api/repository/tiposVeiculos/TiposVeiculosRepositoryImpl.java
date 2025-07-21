package codiub.competicoes.api.repository.tiposVeiculos;

import codiub.competicoes.api.entity.TiposVeiculos;
import codiub.competicoes.api.entity.TiposVeiculos_;
import codiub.competicoes.api.filter.TiposVeiculosFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TiposVeiculosRepositoryImpl implements TiposVeiculosRepositoryQuery{
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<TiposVeiculos> filtrar(TiposVeiculosFilter tiposVeiculosFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
	    CriteriaQuery<TiposVeiculos> criteria = builder.createQuery(TiposVeiculos.class);
	    Root<TiposVeiculos> root = criteria.from(TiposVeiculos.class);

	    List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

	    Predicate[] predicates = criarRestricoes(tiposVeiculosFilter, builder, root);
	    criteria.where(predicates).orderBy(orders);

	    TypedQuery<TiposVeiculos> query = manager.createQuery(criteria);
	    adicionarRestricoesDePaginacao(query, pageable);

	    return new PageImpl<>(query.getResultList(), pageable, total(tiposVeiculosFilter));

    }

    private Predicate[] criarRestricoes(
            TiposVeiculosFilter tiposVeiculosFilter, CriteriaBuilder builder, Root<TiposVeiculos> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(tiposVeiculosFilter.getId() != null) {
            predicates.add(builder.equal(root.get(TiposVeiculos_.ID), tiposVeiculosFilter.getId()));
        }

        // DESCRICAO
        if (StringUtils.hasLength(tiposVeiculosFilter.getDescricao())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(TiposVeiculos_.DESCRICAO)),
                            "%" + tiposVeiculosFilter.getDescricao().toLowerCase() + "%"));
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

    private Long total(TiposVeiculosFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<TiposVeiculos> root = criteria.from(TiposVeiculos.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
