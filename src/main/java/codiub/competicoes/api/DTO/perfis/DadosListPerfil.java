package codiub.competicoes.api.DTO.perfis;

import codiub.competicoes.api.entity.seguranca.Perfil;

public record DadosListPerfil(
        Long id,
        String nome
) {
    public DadosListPerfil(Perfil perfil) {
        this(perfil.getId(), perfil.getNome());
    }
}