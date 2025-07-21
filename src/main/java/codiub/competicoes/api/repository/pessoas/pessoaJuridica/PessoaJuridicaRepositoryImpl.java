package codiub.competicoes.api.repository.pessoas.pessoaJuridica;

import codiub.competicoes.api.entity.pessoas.pessoa.PessoaJuridica;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaJuridica_;
import codiub.competicoes.api.filter.pessoas.PessoaJuridicaFilter;
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

public class PessoaJuridicaRepositoryImpl implements PessoaJuridicaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<PessoaJuridica> filtrar(PessoaJuridicaFilter filter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PessoaJuridica> criteria = builder.createQuery(PessoaJuridica.class);
        Root<PessoaJuridica> root = criteria.from(PessoaJuridica.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<PessoaJuridica> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(filter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<PessoaJuridica> filtrar(PessoaJuridicaFilter pessoaJuridicaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PessoaJuridica> criteria = builder.createQuery(PessoaJuridica.class);
        Root<PessoaJuridica> root = criteria.from(PessoaJuridica.class);

        Predicate[] predicates = criarRestricoes(pessoaJuridicaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<PessoaJuridica> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            PessoaJuridicaFilter pessoaJuridicaFilter, CriteriaBuilder builder, Root<PessoaJuridica> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(pessoaJuridicaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(PessoaJuridica_.ID), pessoaJuridicaFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(pessoaJuridicaFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(PessoaJuridica_.NOME)),
                            "%" + pessoaJuridicaFilter.getNome().toLowerCase() + "%"));
        }

        // CPF
        /*if (StringUtils.hasLength(pessoasFilter.getDadosPessoaJuridicaJuridicaFilter().getCpf())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(PessoaJuridicas_.PESSOA_Juridica).get(PessoaJuridicaJuridica_.CPF)),
                            "%" + pessoasFilter.getDadosPessoaJuridicaJuridicaFilter().getCpf() + "%"));
        }*/

        //DATA NASCIMENTO
        /*if(pessoasFilter.getDadosPessoaJuridicaJuridicaFilter().getDataNascimento() != null){
            // Deve vir do frontEnd no formato MM/dd/yyyy

            Date data = pessoasFilter.getDadosPessoaJuridicaJuridicaFilter().getDataNascimento();

            Date startDate = DateUtils.truncate(data, Calendar.DATE);
            Date endDate = DateUtils.addSeconds(DateUtils.addDays(startDate, 1), -1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Note o formato
            LocalDate startDateLocalDate = Instant.ofEpochMilli(startDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate endDateLocalDate = Instant.ofEpochMilli(endDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            endDateLocalDate = startDateLocalDate.plusDays(1);

            predicates.add(
                    builder.between(root.get(PessoaJuridicas_.PESSOA_Juridica).get(PessoaJuridicaJuridica_.DATA_NASCIMENTO), startDateLocalDate, endDateLocalDate));
        }*/

        return predicates.toArray(new Predicate[predicates.size()]);
    }
    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(PessoaJuridicaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<PessoaJuridica> root = criteria.from(PessoaJuridica.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
