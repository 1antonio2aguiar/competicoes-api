package codiub.competicoes.api.DTO.provas;

import codiub.competicoes.api.utils.TimeConversionUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Optional;

public record DadosInsertProvasRcd(
        Long empresaId,
        Long etapaId,
        Long tipoNadoId,
        Long categoriaId,
        Integer distancia,
        String genero,
        String revezamento,
        String medley,
        String indiceTecnico,
        String record,
        String tipoPiscina,
        String observacao
) {
        public String getObservacao(){
                if (observacao != null) {
                        return observacao.toUpperCase();
                } else {
                        return "";
                }
        }

        public Optional<Long> getIndiceTecnicoAsLong() {
                return TimeConversionUtils.convertStringToLong(this.indiceTecnico);
        }
        public Optional<Long> getRecordAsLong() {
                return TimeConversionUtils.convertStringToLong(this.record);
        }
}


/*

    @ManyToOne
    @JoinColumn(name = "campeonato_id")
    private Campeonato campeonato;

    @ManyToOne
    @JoinColumn(name = "etapa_id")
    private Etapa etapa;

    @ManyToOne
    @JoinColumn(name = "tipo_nado_id")
    private TipoNado tipoNado;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
 */