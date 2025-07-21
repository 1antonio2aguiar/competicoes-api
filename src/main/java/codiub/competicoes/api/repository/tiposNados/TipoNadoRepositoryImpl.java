package codiub.competicoes.api.repository.tiposNados;

import codiub.competicoes.api.entity.TipoNado;
import codiub.competicoes.api.entity.TipoNado_;
import codiub.competicoes.api.filter.TipoNadoFilter;
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

public class TipoNadoRepositoryImpl implements TipoNadoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<TipoNado> filtrar(TipoNadoFilter tipoNadoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TipoNado> criteria = builder.createQuery(TipoNado.class);
        Root<TipoNado> root = criteria.from(TipoNado.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(tipoNadoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<TipoNado> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(tipoNadoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<TipoNado> filtrar(TipoNadoFilter tipoNadoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TipoNado> criteria = builder.createQuery(TipoNado.class);
        Root<TipoNado> root = criteria.from(TipoNado.class);

        Predicate[] predicates = criarRestricoes(tipoNadoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<TipoNado> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            TipoNadoFilter tipoNadoFilter, CriteriaBuilder builder, Root<TipoNado> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(tipoNadoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(TipoNado_.ID), tipoNadoFilter.getId()));
        }

        // FILTRAR POR TIPO DE NADO
        if (StringUtils.hasLength(tipoNadoFilter.getDescricao())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(TipoNado_.DESCRICAO)),
                            "%" + tipoNadoFilter.getDescricao().toLowerCase() + "%"));
        }

        // DESCRICAO
        if (StringUtils.hasLength(tipoNadoFilter.getDescricao())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(TipoNado_.DESCRICAO)),
                            "%" + tipoNadoFilter.getDescricao().toLowerCase() + "%"));
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

    private Long total(TipoNadoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<TipoNado> root = criteria.from(TipoNado.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
