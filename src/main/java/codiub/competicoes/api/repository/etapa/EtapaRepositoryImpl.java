package codiub.competicoes.api.repository.etapa;

import codiub.competicoes.api.entity.Campeonato_;
import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.entity.Etapa_;
import codiub.competicoes.api.filter.EtapaFilter;
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
import java.util.Objects;

public class EtapaRepositoryImpl implements EtapaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Etapa> filtrar(EtapaFilter etapaFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Etapa> criteria = builder.createQuery(Etapa.class);
        Root<Etapa> root = criteria.from(Etapa.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(etapaFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Etapa> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(etapaFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Etapa> filtrar(EtapaFilter etapaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Etapa> criteria = builder.createQuery(Etapa.class);
        Root<Etapa> root = criteria.from(Etapa.class);

        Predicate[] predicates = criarRestricoes(etapaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Etapa> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            EtapaFilter etapaFilter, CriteriaBuilder builder, Root<Etapa> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID DA ETAPA
        if(etapaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Etapa_.ID), etapaFilter.getId()));
        }

        // ID DO CAMPEONATO
        if(etapaFilter.getCampeonatoFilter().getId() != null) {
            predicates.add(builder.equal(root.get(Etapa_.CAMPEONATO).get(Campeonato_.ID), etapaFilter.getCampeonatoFilter().getId()));
        }

        // NOME
        if (StringUtils.hasLength(etapaFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Etapa_.NOME)),
                            "%" + etapaFilter.getNome().toLowerCase() + "%"));
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

    private Long total(EtapaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Etapa> root = criteria.from(Etapa.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
