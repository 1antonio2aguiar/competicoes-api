package codiub.competicoes.api.repository.inscricao.custon;

import codiub.competicoes.api.entity.Inscricoes;
import codiub.competicoes.api.filter.InscricaoFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class InscricaoCustonRepository {
    @PersistenceContext
    private EntityManager manager;
    /*public Page<Inscricoes> inscricoesPorProva(InscricaoFilter filter, Pageable pageable) {
        Inte nomeFilter = filter.getPessoaNome();
    }*/
}
