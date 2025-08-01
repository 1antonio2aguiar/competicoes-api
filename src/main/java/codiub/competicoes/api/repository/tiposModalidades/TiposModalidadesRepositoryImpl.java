package codiub.competicoes.api.repository.tiposModalidades;

import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.entity.TiposModalidades_;
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

public class TiposModalidadesRepositoryImpl implements TiposModalidadesRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<TiposModalidades> filtrar(TiposModalidadesFilter tiposModalidadesFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
	    CriteriaQuery<TiposModalidades> criteria = builder.createQuery(TiposModalidades.class);
	    Root<TiposModalidades> root = criteria.from(TiposModalidades.class);

	    List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

	    Predicate[] predicates = criarRestricoes(tiposModalidadesFilter, builder, root);
	    criteria.where(predicates).orderBy(orders);

	    TypedQuery<TiposModalidades> query = manager.createQuery(criteria);
	    adicionarRestricoesDePaginacao(query, pageable);

	    return new PageImpl<>(query.getResultList(), pageable, total(tiposModalidadesFilter));
    }

    //Aqui da lista sem paginacao
    @Override
    public List<TiposModalidades> filtrar(TiposModalidadesFilter tiposModalidadesFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TiposModalidades> criteria = builder.createQuery(TiposModalidades.class);
        Root<TiposModalidades> root = criteria.from(TiposModalidades.class);

        Predicate[] predicates = criarRestricoes(tiposModalidadesFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<TiposModalidades> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            TiposModalidadesFilter tiposModalidadesFilter, CriteriaBuilder builder, Root<TiposModalidades> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(tiposModalidadesFilter.getId() != null) {
            predicates.add(builder.equal(root.get(TiposModalidades_.ID), tiposModalidadesFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(tiposModalidadesFilter.getNome())) {
            //System.err.println("Paasou por aqui!  "+ tiposModalidadesFilter);
            predicates.add(
                    builder.like(
                            builder.lower(root.get(TiposModalidades_.NOME)),
                            "%" + tiposModalidadesFilter.getNome().toLowerCase() + "%"));
        }

        // DESCRIÇÃO
        if (StringUtils.hasLength(tiposModalidadesFilter.getDescricao())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(TiposModalidades_.DESCRICAO)),
                            "%" + tiposModalidadesFilter.getDescricao().toLowerCase() + "%"));
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

    private Long total(TiposModalidadesFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<TiposModalidades> root = criteria.from(TiposModalidades.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
