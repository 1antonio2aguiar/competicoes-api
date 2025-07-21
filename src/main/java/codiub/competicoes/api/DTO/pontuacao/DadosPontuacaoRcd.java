package codiub.competicoes.api.DTO.pontuacao;

import codiub.competicoes.api.entity.Pontuacao;

import java.util.Optional;

public record DadosPontuacaoRcd(
        long id,
        String classificacao,
        long pontos
) {
    public DadosPontuacaoRcd(Pontuacao pontuacao) {
        this(pontuacao.getId(),
                pontuacao.getClassificacao(),
                pontuacao.getPontos());
    }
    public DadosPontuacaoRcd(Optional<Pontuacao> pontuacao) {
        this(pontuacao.get().getId(),
                pontuacao.get().getClassificacao(),
                pontuacao.get().getPontos());
    }
    public String getClassificacao(){
                return classificacao.toUpperCase();
        }
}
