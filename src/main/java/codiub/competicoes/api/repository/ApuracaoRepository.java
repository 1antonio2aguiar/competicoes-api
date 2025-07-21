package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Apuracao;
import codiub.competicoes.api.entity.enuns.StatusApuracao;
import codiub.competicoes.api.entity.enuns.StatusInscricao;
import codiub.competicoes.api.repository.apuracao.ApuracaoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApuracaoRepository extends JpaRepository<Apuracao, Long>, ApuracaoRepositoryQuery {

    List<Apuracao> findByProvaIdAndStatusOrderByResultadoAsc(Long provaId, StatusApuracao status);

    int countByProvaId(Long provaId);

    @Modifying
    @Query(value = "UPDATE apuracoes SET status = :#{#status.ordinal()} WHERE id = :apuracaoId", nativeQuery = true)
    int updateStatusById(@Param("apuracaoId") Long apuracaoId, @Param("status") StatusApuracao status);
}
