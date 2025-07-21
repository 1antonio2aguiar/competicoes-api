package codiub.competicoes.api.DTO.atletas;


import codiub.competicoes.api.DTO.categoria.DadosCategoriaRcd;
import codiub.competicoes.api.DTO.equipe.DadosEquipeReduzidoRcd;
import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Equipe;

import java.util.Optional;

public record DadosListAtletasRcd(
        Long id,
        String empresa,
        Long categoriaId,
        String pessoaNome,
        String equipeNome,
        String categoriaDescricao,
        DadosPessoasReduzidoRcd pessoa,
        DadosEquipeReduzidoRcd equipe,
        //DadosCategoriaRcd categoria,
        String observacao){

    public static DadosListAtletasRcd fromAtleta(Atleta atleta) {
        return new DadosListAtletasRcd(
                atleta.getId(),
                atleta.getEmpresa().getNome(),
                atleta.getCategoria().getId(),
                atleta.getPessoa().getNome(),
                atleta.getEquipe().getNome(),
                atleta.getCategoria().getDescricao(),
                atleta.getPessoa() != null ? codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd.fromPessoas(atleta.getPessoa()) : null,
                atleta.getEquipe() != null ? DadosEquipeReduzidoRcd.fromEquipes(atleta.getEquipe()) : null,
                //atleta.getCategoria() != null ? DadosCategoriaRcd.fromCategoria(atleta.getCategoria()) : null,
                atleta.getObservacao()
        );
    }
    public static DadosListAtletasRcd fromOptionalEquipe(Optional<Atleta> atletaOptional) {
        return atletaOptional.map(DadosListAtletasRcd::fromAtleta).orElse(null);
    }
}
