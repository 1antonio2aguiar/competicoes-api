package codiub.competicoes.api.DTO.tipoModalidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosInsertTipoModalidadeRcd(
        @NotBlank(message = "nome.obrigatório")
        @Size(max = 40, min = 3)
        String nome,
        String descricao
) {
        public String getNome(){
                return nome.toUpperCase();
        }
}
