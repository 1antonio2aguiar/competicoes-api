package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Inscricoes;
import codiub.competicoes.api.entity.enuns.StatusInscricao;
import codiub.competicoes.api.entity.enuns.StatusTipoInscricao;
import codiub.competicoes.api.repository.inscricao.InscricaoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface InscricaoRepository extends JpaRepository<Inscricoes, Long>, InscricaoRepositoryQuery {

    // Esse aqui esta so de exemplo
    int countByProvaIdAndStatusIn(Long provaId, Collection<Integer> statuses);

    int countByProvaIdAndStatusInAndTipoInscricao(Long provaId, Collection<Integer> statuses, StatusTipoInscricao tipoInscricao);

    @Modifying
    @Query(value = "UPDATE inscricoes SET status = :#{#status.ordinal()} WHERE id = :inscricaoId", nativeQuery = true)
    int updateStatusById(@Param("inscricaoId") Long inscricaoId, @Param("status") StatusInscricao status);

}
