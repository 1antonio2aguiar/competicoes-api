package codiub.competicoes.api.repository.equipe;

import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.filter.EquipeFilter;
import codiub.competicoes.api.filter.PontuacaoFilter;
import codiub.competicoes.api.repository.pontuacao.PontuacaoRepositoryQuery;
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

public class EquipeRepositoryImpl implements EquipeRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Equipe> filtrar(EquipeFilter equipeFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Equipe> criteria = builder.createQuery(Equipe.class);
        Root<Equipe> root = criteria.from(Equipe.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(equipeFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Equipe> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(equipeFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Equipe> filtrar(EquipeFilter equipeFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Equipe> criteria = builder.createQuery(Equipe.class);
        Root<Equipe> root = criteria.from(Equipe.class);

        Predicate[] predicates = criarRestricoes(equipeFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Equipe> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            EquipeFilter equipeFilter, CriteriaBuilder builder, Root<Equipe> root) {

        List<Predicate> predicates = new ArrayList<>();

        if(equipeFilter.getEmpresaFilter().getId() != null){
            predicates.add(builder.equal(root.get(Equipe_.empresa).get(Empresa_.ID), equipeFilter.getEmpresaFilter().getId()));
        }

        // ID
        if(equipeFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Equipe_.ID), equipeFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(equipeFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Equipe_.NOME)),
                            "%" + equipeFilter.getNome().toLowerCase() + "%"));
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

    private Long total(EquipeFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Equipe> root = criteria.from(Equipe.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
