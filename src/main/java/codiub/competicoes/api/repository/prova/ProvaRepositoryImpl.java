package codiub.competicoes.api.repository.prova;

import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.filter.ProvaFilter;
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

public class ProvaRepositoryImpl implements ProvaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Prova> filtrar(ProvaFilter provaFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Prova> criteria = builder.createQuery(Prova.class);
        Root<Prova> root = criteria.from(Prova.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(provaFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Prova> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(provaFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Prova> filtrar(ProvaFilter provaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Prova> criteria = builder.createQuery(Prova.class);
        Root<Prova> root = criteria.from(Prova.class);

        Predicate[] predicates = criarRestricoes(provaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Prova> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            ProvaFilter provaFilter, CriteriaBuilder builder, Root<Prova> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID ETAPA
        if(provaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Prova_.ID), provaFilter.getId()));
        }

        //EMPRESA
        if(provaFilter.getEmpresaId() != null) {
            predicates.add(builder.equal(root.get(Etapa_.EMPRESA).get(Empresa_.ID), provaFilter.getEmpresaId()));
        }

        // Filtra pelo ID do campeonato
        if(provaFilter.getEtapaFilter().getCampeonatoFilter().getId() != null) {
            predicates.add(builder.equal(root.get(Prova_.ETAPA).get(Etapa_.CAMPEONATO).get(Campeonato_.ID),
                    provaFilter.getEtapaFilter().getCampeonatoFilter().getId()));
        }

        // Filtra pelo ID da etpa
        if(provaFilter.getEtapaFilter().getId() != null) {
            predicates.add(builder.equal(root.get(Prova_.ETAPA).get(Etapa_.ID),
                    provaFilter.getEtapaFilter().getId()));
        }

        // NOME DA ETAPA
        if (StringUtils.hasLength(provaFilter.getEtapaFilter().getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Prova_.ETAPA).get(Etapa_.NOME)),
                            "%" + provaFilter.getEtapaFilter().getNome() + "%"));
        }

        // TIPO NADO
        if (StringUtils.hasLength(provaFilter.getTipoNado())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Prova_.TIPO_NADO).get(TipoNado_.DESCRICAO)),
                            "%" + provaFilter.getTipoNado() + "%"));
        }

        // CATEGORIA
        if (StringUtils.hasLength(provaFilter.getCategoriaFilter().getDescricao())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Prova_.CATEGORIA).get(Categoria_.DESCRICAO)),
                            "%" + provaFilter.getCategoriaFilter().getDescricao() + "%"));
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

    private Long total(ProvaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Prova> root = criteria.from(Prova.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
