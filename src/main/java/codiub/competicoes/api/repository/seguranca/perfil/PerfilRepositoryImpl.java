package codiub.competicoes.api.repository.seguranca.perfil;

import codiub.competicoes.api.entity.seguranca.Perfil;
import codiub.competicoes.api.entity.seguranca.Perfil_;
import codiub.competicoes.api.filter.PerfilFilter;
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

public class PerfilRepositoryImpl implements PerfilRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Perfil> filtrar(PerfilFilter perfilFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Perfil> criteria = builder.createQuery(Perfil.class);
        Root<Perfil> root = criteria.from(Perfil.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(perfilFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Perfil> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(perfilFilter));
    }

    @Override
    public List<Perfil> filtrar(PerfilFilter perfilFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Perfil> criteria = builder.createQuery(Perfil.class);
        Root<Perfil> root = criteria.from(Perfil.class);

        Predicate[] predicates = criarRestricoes(perfilFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Perfil> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            PerfilFilter perfilFilter, CriteriaBuilder builder, Root<Perfil> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if (perfilFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Perfil_.ID), perfilFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(perfilFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Perfil_.NOME)),
                            "%" + perfilFilter.getNome().toLowerCase() + "%"));
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

    private Long total(PerfilFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Perfil> root = criteria.from(Perfil.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
