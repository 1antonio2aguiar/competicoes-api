package codiub.competicoes.api.DTO.locaisCompeticoes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosInsertLocaisCompeticoesRcd(
        Long empresa,
        @NotBlank(message = "nome.obrigatório")
        @Size(max = 100, min = 3)
        String nome,
        String endereco
) {
        public String getNome(){
                return nome.toUpperCase();
        }
        public String getEndereco(){
                return endereco.toUpperCase();
        }
}
