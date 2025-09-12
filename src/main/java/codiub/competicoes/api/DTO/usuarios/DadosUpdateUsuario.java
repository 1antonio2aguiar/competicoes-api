package codiub.competicoes.api.DTO.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DadosUpdateUsuario(
        Long id,
        Long empresaId,
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        Boolean ativo,
        List<Long> perfisIds
        
) {
}