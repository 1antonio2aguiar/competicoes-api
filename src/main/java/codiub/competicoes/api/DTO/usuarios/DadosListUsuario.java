package codiub.competicoes.api.DTO.usuarios;

import codiub.competicoes.api.entity.seguranca.Usuario;
import java.util.List;
import java.util.stream.Collectors;

public record DadosListUsuario(
        Long id,
        String nome,
        String email,
        Boolean ativo,
        Long empresaId,
        String razaoSocial,
        List<String> perfis
) {
    public DadosListUsuario(Usuario usuario) {
        this(
            usuario.getId(),

            usuario.getNome(),
            usuario.getEmail(),
            usuario.getAtivo(),
            (usuario.getEmpresa() != null) ? usuario.getEmpresa().getId() : 0,
            (usuario.getEmpresa() != null) ? usuario.getEmpresa().getRazaoSocial() : "N/A",
            usuario.getPerfis().stream().map(perfil -> perfil.getNome()).collect(Collectors.toList())
        );
    }
}