package codiub.competicoes.api.DTO.tipoModalidade;

import codiub.competicoes.api.entity.TiposModalidades;

import jakarta.validation.constraints.NotBlank;

public record DadosUpdateTipoModalidadeRcd(
    @NotBlank(message = "nome.obrigatorio")
    String nome,
    String descricao ) {

    public DadosUpdateTipoModalidadeRcd(TiposModalidades tipoModalidade) {
        this(tipoModalidade.getNome(),
                tipoModalidade.getDescricao());
    }
    public String getNome(){
        return nome.toUpperCase();
    }
}
