package codiub.competicoes.api.repository.pessoas.pessoas;

import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.entity.pessoas.Pessoas_;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PessoasRepositoryImpl implements PessoasRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Pessoas> filtrar(PessoasFilter pessoasFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoas> criteria = builder.createQuery(Pessoas.class);
        Root<Pessoas> root = criteria.from(Pessoas.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(pessoasFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Pessoas> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(pessoasFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Pessoas> filtrar(PessoasFilter pessoasFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoas> criteria = builder.createQuery(Pessoas.class);
        Root<Pessoas> root = criteria.from(Pessoas.class);

        Predicate[] predicates = criarRestricoes(pessoasFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Pessoas> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            PessoasFilter pessoasFilter, CriteriaBuilder builder, Root<Pessoas> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(pessoasFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Pessoas_.ID), pessoasFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(pessoasFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Pessoas_.NOME)),
                            "%" + pessoasFilter.getNome().toLowerCase() + "%"));
        }

        // CPF
        /*if (StringUtils.hasLength(pessoasFilter.getDadosPessoaFisicaFilter().getCpf())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Pessoas_.PESSOA_FISICA).get(PessoaFisica_.CPF)),
                            "%" + pessoasFilter.getDadosPessoaFisicaFilter().getCpf() + "%"));
        }*/

        //DATA NASCIMENTO
        /*if(pessoasFilter.getDadosPessoaFisicaFilter().getDataNascimento() != null){
            // Deve vir do frontEnd no formato MM/dd/yyyy

            Date data = pessoasFilter.getDadosPessoaFisicaFilter().getDataNascimento();

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
                    builder.between(root.get(Pessoas_.PESSOA_FISICA).get(PessoaFisica_.DATA_NASCIMENTO), startDateLocalDate, endDateLocalDate));
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

    private Long total(PessoasFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pessoas> root = criteria.from(Pessoas.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
