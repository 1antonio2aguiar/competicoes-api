package codiub.competicoes.api.repository.atleta;

import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.pessoas.DadosPessoaFisica_;
import codiub.competicoes.api.entity.pessoas.Pessoas_;
import codiub.competicoes.api.filter.AtletaFilter;
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

public class AtletaRepositoryImpl implements AtletaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Atleta> filtrar(AtletaFilter atletaFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Atleta> criteria = builder.createQuery(Atleta.class);
        Root<Atleta> root = criteria.from(Atleta.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(atletaFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Atleta> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(atletaFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Atleta> filtrar(AtletaFilter atletaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Atleta> criteria = builder.createQuery(Atleta.class);
        Root<Atleta> root = criteria.from(Atleta.class);

        Predicate[] predicates = criarRestricoes(atletaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Atleta> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            AtletaFilter atletaFilter, CriteriaBuilder builder, Root<Atleta> root) {

        List<Predicate> predicates = new ArrayList<>();

        /*if(atletaFilter.getEmpresaFilter().getId() != null){
            predicates.add(builder.equal(root.get(Atleta_.empresa).get(Empresa_.ID), atletaFilter.getEmpresaFilter().getId()));
        }*/

        // ID
        if(atletaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Atleta_.ID), atletaFilter.getId()));
        }

        // NOME DO ATLETA
        if (StringUtils.hasLength(atletaFilter.getPessoaNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Atleta_.PESSOA).get(Pessoas_.NOME)),
                            "%" + atletaFilter.getPessoaNome() + "%"));
        }

        // CATEGORIA DO ATLETA
        if (StringUtils.hasLength(atletaFilter.getCategoria())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Atleta_.CATEGORIA).get(Categoria_.DESCRICAO)),
                            "%" + atletaFilter.getCategoria() + "%"));
        }

        // EQUIPE DO ATLETA
        if (StringUtils.hasLength(atletaFilter.getEquipeNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Atleta_.EQUIPE).get(Equipe_.NOME)),
                            "%" + atletaFilter.getEquipeNome() + "%"));
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

    private Long total(AtletaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Atleta> root = criteria.from(Atleta.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
