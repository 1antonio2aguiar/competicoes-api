package codiub.competicoes.api.repository.parametro;

import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.filter.ParametroFilter;
import codiub.competicoes.api.filter.ProvaFilter;
import codiub.competicoes.api.repository.prova.ProvaRepositoryQuery;
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

public class ParametroRepositoryImpl implements ParametroRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Parametro> filtrar(ParametroFilter parametroFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Parametro> criteria = builder.createQuery(Parametro.class);
        Root<Parametro> root = criteria.from(Parametro.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(parametroFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Parametro> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(parametroFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Parametro> filtrar(ParametroFilter parametroFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Parametro> criteria = builder.createQuery(Parametro.class);
        Root<Parametro> root = criteria.from(Parametro.class);

        Predicate[] predicates = criarRestricoes(parametroFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Parametro> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            ParametroFilter parametroFilter, CriteriaBuilder builder, Root<Parametro> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID DO PARAMETRO
        if(parametroFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Parametro_.ID), parametroFilter.getId()));
        }

        // Filtra pelo ID da etapa
        if(parametroFilter.getEtapaId() != null) {
            predicates.add(builder.equal(root.get(Parametro_.ETAPA).get(Etapa_.ID),
                    parametroFilter.getEtapaId()));
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

    private Long total(ParametroFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Parametro> root = criteria.from(Parametro.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
