package codiub.competicoes.api.DTO.atletas;

import codiub.competicoes.api.DTO.equipe.DadosEquipeReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.Atleta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// A assinatura do record está correta com o novo campo
public record DadosListAtletasRcd(
        Long id,
        String empresa,
        Long categoriaId,
        String categoriaDescricao,
        DadosPessoasReduzidoRcd pessoa,
        String pessoaNome, // NOVO CAMPO
        DadosEquipeReduzidoRcd equipe,
        String equipeNome,
        String observacao)
{

    public static List<DadosListAtletasRcd> fromEntities(List<Atleta> atletas, Map<Long, DadosPessoasReduzidoRcd> pessoasMap) {
        return atletas.stream()
                .map(atleta -> {
                    // Pega o DTO da pessoa para reutilização e clareza
                    DadosPessoasReduzidoRcd pessoaDto = pessoasMap.get(atleta.getPessoaId());

                    return new DadosListAtletasRcd(
                            atleta.getId(),
                            atleta.getEmpresa() != null ? atleta.getEmpresa().getRazaoSocial() : null,
                            atleta.getCategoria() != null ? atleta.getCategoria().getId() : null,
                            atleta.getCategoria() != null ? atleta.getCategoria().getDescricao() : null,
                            pessoaDto,
                            pessoaDto != null ? pessoaDto.nome() : null,
                            atleta.getEquipe() != null ? DadosEquipeReduzidoRcd.fromEquipes(atleta.getEquipe()) : null,
                            atleta.getEquipe().getNome(),
                            atleta.getObservacao()
                    );
                })
                .collect(Collectors.toList());
    }

    public static DadosListAtletasRcd fromAtleta(Atleta atleta, DadosPessoasReduzidoRcd pessoaDto) {
        return new DadosListAtletasRcd(
                atleta.getId(),
                atleta.getEmpresa() != null ? atleta.getEmpresa().getRazaoSocial() : null,
                atleta.getCategoria() != null ? atleta.getCategoria().getId() : null,
                atleta.getCategoria() != null ? atleta.getCategoria().getDescricao() : null,
                pessoaDto, // 1. Passa o objeto completo da pessoa
                pessoaDto != null ? pessoaDto.nome() : null, // 2. NOVO: Passa apenas o nome da pessoa
                atleta.getEquipe() != null ? DadosEquipeReduzidoRcd.fromEquipes(atleta.getEquipe()) : null,
                atleta.getEquipe().getNome(),
                atleta.getObservacao()
        );
    }
}