package codiub.competicoes.api.repository.pessoas.tiposPessoas;

import codiub.competicoes.api.entity.pessoas.Paises;
import codiub.competicoes.api.entity.pessoas.Paises_;
import codiub.competicoes.api.entity.pessoas.TiposPessoas;
import codiub.competicoes.api.entity.pessoas.TiposPessoas_;
import codiub.competicoes.api.filter.pessoas.PaisesFilter;
import codiub.competicoes.api.filter.pessoas.TiposPessoasFilter;
import codiub.competicoes.api.repository.pessoas.paises.PaisesRepositoryQuery;
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

public class TiposPessoasRepositoryImpl implements TiposPessoasRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<TiposPessoas> filtrar(TiposPessoasFilter tiposPessoasFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TiposPessoas> criteria = builder.createQuery(TiposPessoas.class);
        Root<TiposPessoas> root = criteria.from(TiposPessoas.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(tiposPessoasFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<TiposPessoas> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(tiposPessoasFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<TiposPessoas> filtrar(TiposPessoasFilter tiposPessoasFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<TiposPessoas> criteria = builder.createQuery(TiposPessoas.class);
        Root<TiposPessoas> root = criteria.from(TiposPessoas.class);

        Predicate[] predicates = criarRestricoes(tiposPessoasFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<TiposPessoas> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            TiposPessoasFilter tiposPessoasFilter, CriteriaBuilder builder, Root<TiposPessoas> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(tiposPessoasFilter.getId() != null) {
            predicates.add(builder.equal(root.get(TiposPessoas_.ID), tiposPessoasFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(tiposPessoasFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(TiposPessoas_.NOME)),
                            "%" + tiposPessoasFilter.getNome().toLowerCase() + "%"));
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

    private Long total(TiposPessoasFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<TiposPessoas> root = criteria.from(TiposPessoas.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
