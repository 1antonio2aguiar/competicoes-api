package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.Pontuacao;
import codiub.competicoes.api.repository.categoria.CategoriaRepositoryQuery;
import codiub.competicoes.api.repository.pontuacao.PontuacaoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PontuacaoRepository extends JpaRepository<Pontuacao, Long>, PontuacaoRepositoryQuery {

}
