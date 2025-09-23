package codiub.competicoes.api.repository.empresa;

import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.entity.Empresa_;
import codiub.competicoes.api.entity.Equipe_;
import codiub.competicoes.api.filter.EmpresaFilter;
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

public class EmpresaRepositoryImpl implements EmpresaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Empresa> criteria = builder.createQuery(Empresa.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(empresaFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Empresa> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(empresaFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Empresa> filtrar(EmpresaFilter empresaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Empresa> criteria = builder.createQuery(Empresa.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        Predicate[] predicates = criarRestricoes(empresaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Empresa> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            EmpresaFilter empresaFilter, CriteriaBuilder builder, Root<Empresa> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(empresaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Empresa_.ID), empresaFilter.getId()));
        }

        // DESCRICAO
        if (StringUtils.hasLength(empresaFilter.getRazaoSocial())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Empresa_.RAZAO_SOCIAL)),
                            "%" + empresaFilter.getRazaoSocial().toLowerCase() + "%"));
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

    private Long total(EmpresaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
