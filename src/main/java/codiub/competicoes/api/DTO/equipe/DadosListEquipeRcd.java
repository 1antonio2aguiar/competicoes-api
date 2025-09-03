package codiub.competicoes.api.DTO.equipe;

import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.entity.Equipe;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public record DadosListEquipeRcd(
        Long id,
        String empresa,
        DadosPessoasfjReduzRcd agremiacao,
        String nome,
        String sigla,
        Long modalidadeId,
        String modalidadeNome,
        DadosPessoasfjReduzRcd tecnico,
        DadosPessoasfjReduzRcd assistenteTecnico
){
    public static List<DadosListEquipeRcd> fromEntities(List<Equipe> equipes, List<DadosPessoasfjReduzRcd> pessoasDtos) {
        // Cria um mapa para acesso rápido às pessoas por ID.
        Map<Long, DadosPessoasfjReduzRcd> pessoasMap = pessoasDtos.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, Function.identity()));

        // Mapeia cada equipe para o DTO, buscando os dados da pessoa no mapa.
        return equipes.stream()
                .map(equipe -> new DadosListEquipeRcd(
                        equipe.getId(),
                        equipe.getEmpresa() != null ? equipe.getEmpresa().getRazaoSocial() : null,
                        pessoasMap.get(equipe.getAgremiacaoId()), // Busca agremiacao no mapa
                        equipe.getNome(),
                        equipe.getSigla(),
                        equipe.getModalidade() != null ? equipe.getModalidade().getId() : null,
                        equipe.getModalidade() != null ? equipe.getModalidade().getNome() : null,
                        pessoasMap.get(equipe.getTecnicoId()), // Busca tecnico no mapa
                        pessoasMap.get(equipe.getAssistenteTecnicoId()) // Busca assistente no mapa
                ))
                .collect(Collectors.toList());
    }
    // Método de fábrica para lidar com Optional<Equipe>
    public static DadosListEquipeRcd fromEntity(Equipe equipe, DadosPessoasfjReduzRcd agremiacao, DadosPessoasfjReduzRcd tecnico, DadosPessoasfjReduzRcd assistenteTecnico) {
        return new DadosListEquipeRcd(
                equipe.getId(),
                equipe.getEmpresa() != null ? equipe.getEmpresa().getRazaoSocial() : null,
                agremiacao,
                equipe.getNome(),
                equipe.getSigla(),
                equipe.getModalidade() != null ? equipe.getModalidade().getId() : null,
                equipe.getModalidade() != null ? equipe.getModalidade().getNome() : null,
                tecnico,
                assistenteTecnico
        );
    }

    public static DadosListEquipeRcd fromEntityAndMap(Equipe equipe, Map<Long, DadosPessoasfjReduzRcd> pessoasMap) {
        return new DadosListEquipeRcd(
                equipe.getId(),
                equipe.getEmpresa() != null ? equipe.getEmpresa().getRazaoSocial() : null,
                pessoasMap.get(equipe.getAgremiacaoId()),
                equipe.getNome(),
                equipe.getSigla(),
                equipe.getModalidade() != null ? equipe.getModalidade().getId() : null,
                equipe.getModalidade() != null ? equipe.getModalidade().getNome() : null,
                pessoasMap.get(equipe.getTecnicoId()),
                pessoasMap.get(equipe.getAssistenteTecnicoId())
        );
    }

}
