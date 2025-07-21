package codiub.competicoes.api.DTO.pessoas.pessoa;

import codiub.competicoes.api.entity.enuns.Situacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosInsertPessoaJuridicaRcd(
        // Campos da entidade Pessoa (pai)
        Long id, // Nulo para insert, preenchido para update

        @NotBlank(message = "Nome (Razão Social) é obrigatório")
        @Size(max = 200, message = "Nome (Razão Social) deve ter no máximo 200 caracteres")
        String nome,

        @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
        String observacao,

        @NotNull(message = "Situação é obrigatória")
        Situacao situacao,

        @NotNull(message = "Tipo de Pessoa é obrigatório")
        Long tipoPessoaId,

        // Campos da entidade DadosPessoaJuridica (filha)
        @NotBlank(message = "CNPJ é obrigatório")
        @Size(min = 14, max = 14, message = "CNPJ deve ter 14 caracteres")
        String cnpj,

        @Size(max = 100, message = "Nome Fantasia deve ter no máximo 100 caracteres")
        String nomeFantasia,

        @Size(max = 100, message = "Objeto Social deve ter no máximo 100 caracteres")
        String objetoSocial,

        @Size(max = 1, message = "Micro Empresa deve ter no máximo 1 caractere")
        String microEmpresa,

        Integer tipoEmpresa // Poderia ser um Enum específico também
) {
}