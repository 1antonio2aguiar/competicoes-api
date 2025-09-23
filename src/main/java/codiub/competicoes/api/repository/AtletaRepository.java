package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.filter.AtletaFilter;
import codiub.competicoes.api.repository.atleta.AtletaRepositoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AtletaRepository extends JpaRepository<Atleta, Long>, AtletaRepositoryQuery {

    @Query(
            // Adicionado o filtro por empresa_id na query nativa
            value = "SELECT a.pessoa_id FROM atletas a WHERE a.pessoa_id IN :pessoaIds AND a.empresa_id = :empresaId",
            nativeQuery = true
    )
    List<Long>
    findPessoaIdsCadastradosComoAtletas(@Param("pessoaIds") Collection<Long> pessoaIds, @Param("empresaId") Long empresaId);

    Page<Atleta> filtrarComPessoaIds(AtletaFilter filter, Set<Long> pessoaIds, Pageable pageable);

    boolean existsByPessoaId(Long pessoaId);
}
