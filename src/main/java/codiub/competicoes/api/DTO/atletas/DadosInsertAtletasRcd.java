package codiub.competicoes.api.DTO.atletas;

public record DadosInsertAtletasRcd(

        Long empresaId,
        Long pessoaId,
        Long equipeId,
        Long categoriaId,
        String observacao
) {
        public String getObservacao(){
                if (observacao != null) {
                        return observacao.toUpperCase();
                } else {
                        return "";
                }
        }
}
