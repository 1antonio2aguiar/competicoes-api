package codiub.competicoes.api.repository.seguranca.usuario;

import codiub.competicoes.api.entity.Empresa_;
import codiub.competicoes.api.entity.Etapa_;
import codiub.competicoes.api.entity.Inscricoes_;
import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.entity.seguranca.Usuario_;
import codiub.competicoes.api.filter.UsuarioFilter;
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

public class UsuarioRepositoryImpl implements UsuarioRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Usuario> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Usuario> filtrar(UsuarioFilter usuarioFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Usuario> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            UsuarioFilter usuarioFilter, CriteriaBuilder builder, Root<Usuario> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(usuarioFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Usuario_.ID), usuarioFilter.getId()));
        }

        // EMPRESA ID
        if(usuarioFilter.getEmpresaId() != null) {
            predicates.add(builder.equal(root.get(Usuario_.EMPRESA).get(Empresa_.ID), usuarioFilter.getEmpresaId()));
        }

        // DESCRICAO
        if (StringUtils.hasLength(usuarioFilter.getNome())) {
            predicates.add(
                builder.like(
                    builder.lower(root.get(Usuario_.NOME)),
                    "%" + usuarioFilter.getNome().toLowerCase() + "%"));
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

    private Long total(UsuarioFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
