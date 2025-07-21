package codiub.competicoes.api.repository.pessoas.paises;

import codiub.competicoes.api.entity.pessoas.Paises;
import codiub.competicoes.api.entity.pessoas.Paises_;
import codiub.competicoes.api.filter.pessoas.PaisesFilter;
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

public class PaisesRepositoryImpl implements PaisesRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Paises> filtrar(PaisesFilter paisesFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Paises> criteria = builder.createQuery(Paises.class);
        Root<Paises> root = criteria.from(Paises.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(paisesFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Paises> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(paisesFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Paises> filtrar(PaisesFilter paisesFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Paises> criteria = builder.createQuery(Paises.class);
        Root<Paises> root = criteria.from(Paises.class);

        Predicate[] predicates = criarRestricoes(paisesFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Paises> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            PaisesFilter paisesFilter, CriteriaBuilder builder, Root<Paises> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(paisesFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Paises_.ID), paisesFilter.getId()));
        }

        // DESCRICAO
        if (StringUtils.hasLength(paisesFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Paises_.NOME)),
                            "%" + paisesFilter.getNome().toLowerCase() + "%"));
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

    private Long total(PaisesFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Paises> root = criteria.from(Paises.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
