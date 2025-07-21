package codiub.competicoes.api.repository.pontuacao;

import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.Categoria_;
import codiub.competicoes.api.entity.Pontuacao;
import codiub.competicoes.api.entity.Pontuacao_;
import codiub.competicoes.api.filter.CategoriaFilter;
import codiub.competicoes.api.filter.PontuacaoFilter;
import codiub.competicoes.api.repository.categoria.CategoriaRepositoryQuery;
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

public class PontuacaoRepositoryImpl implements PontuacaoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Pontuacao> filtrar(PontuacaoFilter pontuacaoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pontuacao> criteria = builder.createQuery(Pontuacao.class);
        Root<Pontuacao> root = criteria.from(Pontuacao.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(pontuacaoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Pontuacao> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(pontuacaoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Pontuacao> filtrar(PontuacaoFilter pontuacaoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pontuacao> criteria = builder.createQuery(Pontuacao.class);
        Root<Pontuacao> root = criteria.from(Pontuacao.class);

        Predicate[] predicates = criarRestricoes(pontuacaoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Pontuacao> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            PontuacaoFilter pontuacaoFilter, CriteriaBuilder builder, Root<Pontuacao> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(pontuacaoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Pontuacao_.ID), pontuacaoFilter.getId()));
        }

        // CLASSIFICACAO
        if (StringUtils.hasLength(pontuacaoFilter.getClassificacao())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Pontuacao_.CLASSIFICACAO)),
                            "%" + pontuacaoFilter.getClassificacao().toLowerCase() + "%"));
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

    private Long total(PontuacaoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pontuacao> root = criteria.from(Pontuacao.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
