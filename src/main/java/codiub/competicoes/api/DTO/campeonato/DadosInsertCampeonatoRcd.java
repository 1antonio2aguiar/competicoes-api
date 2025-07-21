package codiub.competicoes.api.DTO.campeonato;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosInsertCampeonatoRcd(

        Long empresa,
        Long modalidade,
        @NotBlank(message = "nome.obrigatório")
        @Size(max = 100, min = 3)
        String nome,
        String descricao
) {
        public String getNome(){
                return nome.toUpperCase();
        }
}
