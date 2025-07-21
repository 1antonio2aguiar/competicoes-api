package codiub.competicoes.api.DTO.locaisCompeticoes;

import codiub.competicoes.api.entity.LocaisCompeticoes;
import jakarta.validation.constraints.NotBlank;

public record DadosUpdateLocaisCompeticoesRcd(
    @NotBlank(message = "nome.obrigatorio")
    String nome,
    String endereco ) {

    public DadosUpdateLocaisCompeticoesRcd(LocaisCompeticoes locaisCompeticoes) {
        this(locaisCompeticoes.getNome(),
                locaisCompeticoes.getEndereco());
    }
    public String getNome(){
        return nome.toUpperCase();
    }
    public String getEndereco(){
        return endereco.toUpperCase();
    }
}
