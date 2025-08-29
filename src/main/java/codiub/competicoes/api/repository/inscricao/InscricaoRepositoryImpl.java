package codiub.competicoes.api.repository.inscricao;

import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.pessoas.Pessoas_;
import codiub.competicoes.api.filter.InscricaoFilter;
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

public class InscricaoRepositoryImpl implements InscricaoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Inscricoes> filtrar(InscricaoFilter inscricaoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Inscricoes> criteria = builder.createQuery(Inscricoes.class);
        Root<Inscricoes> root = criteria.from(Inscricoes.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(inscricaoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Inscricoes> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(inscricaoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Inscricoes> filtrar(InscricaoFilter inscricaoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Inscricoes> criteria = builder.createQuery(Inscricoes.class);
        Root<Inscricoes> root = criteria.from(Inscricoes.class);

        Predicate[] predicates = criarRestricoes(inscricaoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Inscricoes> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            InscricaoFilter inscricaoFilter, CriteriaBuilder builder, Root<Inscricoes> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID INSCRICAO
        if(inscricaoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Inscricoes_.ID), inscricaoFilter.getId()));
        }

        // Filtra pelo ID do campeonato
        if(inscricaoFilter.getProvaFilter().getEtapaFilter().getCampeonatoFilter().getId() != null) {
            predicates.add(builder.equal(root.get(Inscricoes_.PROVA).get(Etapa_.CAMPEONATO).get(Campeonato_.ID),
                    inscricaoFilter.getProvaFilter().getEtapaFilter().getCampeonatoFilter().getId()));
        }

        // Filtra pelo ID da etapa
        if(inscricaoFilter.getProvaFilter().getEtapaFilter().getId() != null) {
            predicates.add(builder.equal(root.get(Inscricoes_.PROVA).get(Etapa_.ID),
                    inscricaoFilter.getProvaFilter().getEtapaFilter().getId()));
        }

        // Filtra pelo ID da prova
        if(inscricaoFilter.getProvaId() != null) {
            predicates.add(builder.equal(root.get(Inscricoes_.PROVA).get(Prova_.ID),
                    inscricaoFilter.getProvaId()));
        }

        // NOME DO ATLETA
        /*if (StringUtils.hasLength(inscricaoFilter.getAtletaFilter().getPessoaNome())) {
            predicates.add(
                    builder.like(
                        builder.lower(root.get(Inscricoes_.ATLETA)
                                .get(Atleta_.PESSOA)
                                .get(Pessoas_.NOME)),
                            "%" + inscricaoFilter.getAtletaFilter().getPessoaNome().toLowerCase() + "%"));
        }*/

        // Filtrar por prova e serie
        if(inscricaoFilter.getSerie() != null) {
            //System.err.println("Filter " + inscricaoFilter);
            predicates.add(builder.equal(root.get(Inscricoes_.SERIE),
                    inscricaoFilter.getSerie()));
        }

        // Filtrar por tipo de inscricao
        if(inscricaoFilter.getTipoInscricao() != null) {

            predicates.add(builder.equal(root.get(Inscricoes_.TIPO_INSCRICAO),
                    inscricaoFilter.getTipoInscricao()));
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

    private Long total(InscricaoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Inscricoes> root = criteria.from(Inscricoes.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
