package codiub.competicoes.api.repository.atleta;

import codiub.competicoes.api.entity.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

        // ID
        if(atletaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Atleta_.ID), atletaFilter.getId()));
        }

        // CATEGORIA DO ATLETA
        if (StringUtils.hasLength(atletaFilter.getCategoria())) {
            // Join com Categoria para buscar pela descrição
            Join<Atleta, Categoria> categoriaJoin = root.join(Atleta_.CATEGORIA, JoinType.LEFT);
            predicates.add(
                    builder.like(
                            builder.lower(categoriaJoin.get(Categoria_.DESCRICAO)),
                            "%" + atletaFilter.getCategoria().toLowerCase() + "%"));
        }

        // EQUIPE DO ATLETA
        if (StringUtils.hasLength(atletaFilter.getEquipeNome())) {
            // Join com Equipe para buscar pelo nome
            Join<Atleta, Equipe> equipeJoin = root.join(Atleta_.EQUIPE, JoinType.LEFT);
            predicates.add(
                    builder.like(
                            builder.lower(equipeJoin.get(Equipe_.NOME)),
                            "%" + atletaFilter.getEquipeNome().toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[0]);
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

    /*
     * ========================================================================
     * AQUI ESTÁ A CORREÇÃO PRINCIPAL
     * Este método agora combina os filtros de Atleta (categoria, equipe)
     * com o filtro de IDs de pessoa.
     * ========================================================================
     */
    public Page<Atleta> filtrarComPessoaIds(AtletaFilter filter, Set<Long> pessoaIds, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Atleta> criteria = builder.createQuery(Atleta.class);
        Root<Atleta> root = criteria.from(Atleta.class);

        // 1. Reutiliza os predicados já existentes para equipe, categoria, etc.
        //    Convertemos para List para poder adicionar mais filtros.
        List<Predicate> predicates = new ArrayList<>(Arrays.asList(criarRestricoes(filter, builder, root)));

        // 2. Adiciona a lógica de filtro por NOME (IDs da API de Pessoas)
        if (pessoaIds != null) {
            if (pessoaIds.isEmpty()) {
                // Se o nome foi pesquisado mas não retornou nenhum ID, a busca não deve retornar nada.
                // Adicionamos uma condição impossível (WHERE FALSE) para garantir um resultado vazio.
                predicates.add(builder.disjunction());
            } else {
                // Se IDs foram encontrados, adiciona o filtro `pessoaId IN (...)`
                predicates.add(root.get(Atleta_.PESSOA_ID).in(pessoaIds));
            }
        }

        // 3. Aplica os predicados na consulta
        criteria.where(predicates.toArray(new Predicate[0]));

        // 4. Aplica ordenação
        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);
        criteria.orderBy(orders);

        // 5. Cria e executa a query com paginação
        TypedQuery<Atleta> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);
        List<Atleta> atletas = query.getResultList();

        // 6. Calcula o total de registros com os mesmos filtros para a paginação
        Long total = totalFiltradoComPessoaIds(filter, pessoaIds);

        return new PageImpl<>(atletas, pageable, total);
    }

    /**
     * Método auxiliar para contar o total de registros aplicando TODOS os filtros,
     * incluindo os IDs de pessoa. É essencial para a paginação correta.
     */
    private Long totalFiltradoComPessoaIds(AtletaFilter filter, Set<Long> pessoaIds) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Atleta> root = criteria.from(Atleta.class);

        // Reutiliza a mesma lógica de criação de predicados do método de busca
        List<Predicate> predicates = new ArrayList<>(Arrays.asList(criarRestricoes(filter, builder, root)));

        if (pessoaIds != null) {
            if (pessoaIds.isEmpty()) {
                predicates.add(builder.disjunction()); // WHERE FALSE
            } else {
                predicates.add(root.get(Atleta_.PESSOA_ID).in(pessoaIds));
            }
        }

        criteria.where(predicates.toArray(new Predicate[0]));
        criteria.select(builder.count(root));

        return manager.createQuery(criteria).getSingleResult();
    }
}