package codiub.competicoes.api.DTO.empresa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosUpdateEmpresa(
        @NotNull // O ID é obrigatório para saber quem atualizar
        Long id,

        @NotBlank
        String cnpj,
        @NotBlank
        String razaoSocial,

        @NotBlank
        String atividade,

        @NotBlank
        String telefone,

        @NotBlank
        String inscricaoEstadual
) {
}