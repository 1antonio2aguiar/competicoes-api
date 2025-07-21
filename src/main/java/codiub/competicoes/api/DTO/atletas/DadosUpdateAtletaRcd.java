package codiub.competicoes.api.DTO.atletas;

public record DadosUpdateAtletaRcd(
    Long equipeId,
    Long categoriaId,
    String observacao ) {

    public String getObservacao(){
        return observacao.toUpperCase();
    }
}
