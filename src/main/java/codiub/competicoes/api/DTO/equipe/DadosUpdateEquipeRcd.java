package codiub.competicoes.api.DTO.equipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosUpdateEquipeRcd(
        Long id, // Incluído para identificar a equipe a ser atualizada
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, min = 3, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,
        @Size(max = 5, message = "A sigla deve ter no máximo 5 caracteres")
        String sigla,
        Long empresaId,
        Long modalidadeId,
        Long agremiacaoId,
        Long tecnicoId,
        Long assistenteTecnicoId
) {
        public String getNome(){
                return nome.toUpperCase();
        }

        public String getSigla() {
                return sigla != null ? sigla.toUpperCase() : null;
        }
}