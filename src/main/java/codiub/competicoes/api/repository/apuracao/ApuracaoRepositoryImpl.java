package codiub.competicoes.api.repository.apuracao;

import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.pessoas.Pessoas_;
import codiub.competicoes.api.filter.ApuracaoFilter;
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

public class ApuracaoRepositoryImpl implements ApuracaoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Apuracao> filtrar(ApuracaoFilter apuracaoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Apuracao> criteria = builder.createQuery(Apuracao.class);
        Root<Apuracao> root = criteria.from(Apuracao.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(apuracaoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Apuracao> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(apuracaoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Apuracao> filtrar(ApuracaoFilter apuracaoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Apuracao> criteria = builder.createQuery(Apuracao.class);
        Root<Apuracao> root = criteria.from(Apuracao.class);

        Predicate[] predicates = criarRestricoes(apuracaoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Apuracao> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            ApuracaoFilter apuracaoFilter, CriteriaBuilder builder, Root<Apuracao> root) {

        List<Predicate> predicates = new ArrayList<>();

        /*if(apuracaoFilter.getEmpresaFilter().getId() != null){
            predicates.add(builder.equal(root.get(Apuracao_.empresa).get(Empresa_.ID), apuracaoFilter.getEmpresaFilter().getId()));
        }*/

        // ID
        if(apuracaoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Apuracao_.ID), apuracaoFilter.getId()));
        }

        // Filtra pelo ID da prova
        if(apuracaoFilter.getProvaId() != null) {
            predicates.add(builder.equal(root.get(Inscricoes_.PROVA).get(Prova_.ID),
                    apuracaoFilter.getProvaId()));
        }

        // NOME DO ATLETA
        /*if (StringUtils.hasLength(apuracaoFilter.getAtletaFilter().getPessoaNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Apuracao_.ATLETA)
                                    .get(Atleta_.PESSOA).get(Pessoas_.NOME)),
                            "%" + apuracaoFilter.getAtletaFilter().getPessoaNome() + "%"));
        }*/

        // Filtrar por prova e serie
        if(apuracaoFilter.getSerie() != null) {
            //System.err.println("Filter " + inscricaoFilter);
            predicates.add(builder.equal(root.get(Inscricoes_.SERIE),
                    apuracaoFilter.getSerie()));
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

    private Long total(ApuracaoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Apuracao> root = criteria.from(Apuracao.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
