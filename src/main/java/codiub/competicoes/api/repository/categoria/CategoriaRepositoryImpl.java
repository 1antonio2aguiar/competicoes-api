package codiub.competicoes.api.repository.categoria;

import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.Categoria_;
import codiub.competicoes.api.filter.CategoriaFilter;
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

public class CategoriaRepositoryImpl implements CategoriaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Categoria> filtrar(CategoriaFilter categotiaFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Categoria> criteria = builder.createQuery(Categoria.class);
        Root<Categoria> root = criteria.from(Categoria.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(categotiaFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Categoria> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(categotiaFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Categoria> filtrar(CategoriaFilter categotiaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Categoria> criteria = builder.createQuery(Categoria.class);
        Root<Categoria> root = criteria.from(Categoria.class);

        Predicate[] predicates = criarRestricoes(categotiaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Categoria> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            CategoriaFilter categotiaFilter, CriteriaBuilder builder, Root<Categoria> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(categotiaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Categoria_.ID), categotiaFilter.getId()));
        }

        // DESCRICAO
        if (StringUtils.hasLength(categotiaFilter.getDescricao())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Categoria_.DESCRICAO)),
                            "%" + categotiaFilter.getDescricao().toLowerCase() + "%"));
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

    private Long total(CategoriaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Categoria> root = criteria.from(Categoria.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
