package codiub.competicoes.api.DTO.provas;

import codiub.competicoes.api.entity.Prova;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public record DadosListProvasRcd(Long id,
     Integer distancia,
     String genero,
     String revezamento,
     String medley,
     String indiceTecnico,  // Agora String
     String record,
     String tipoPiscina,
     String empresa,
     Long etapaId,
     String etapaNome,
     Long tipoNadoId,
     String tipoNadoDescricao,
     Long categoriaId,
     String categoriaDescricao,
     String observacao,
    String campeonatoNome,
    Long campeonatoId
    ){
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
    public DadosListProvasRcd(
            Long id,
            Integer distancia,
            String genero,
            String revezamento,
            String medley,
            Long indiceTecnicoMillis, // Recebe Long
            Long recordMillis, // Recebe Long
            String tipoPiscina,
            String empresa,
            Long etapaId,
            String etapaNome,
            Long tipoNadoId,
            String tipoNadoDescricao,
            Long categoriaId,
            String categoriaDescricao,
            String observacao,
            String campeonatoNome,
            Long campeonatoId

    ) {
        this(
                id,
                distancia,
                genero,
                revezamento,
                medley,
                formatTime(indiceTecnicoMillis), // Formata indiceTecnico
                formatTime(recordMillis),  // Formata record
                tipoPiscina,
                empresa,
                etapaId,
                etapaNome,
                tipoNadoId,
                tipoNadoDescricao,
                categoriaId,
                categoriaDescricao,
                observacao,
                campeonatoNome,
                campeonatoId

        );
    }
    private static String formatTime(Long timeInMillis) {
        if (timeInMillis == null) {
            return null; // Ou retorne uma string vazia, se preferir ""
        }
        return sdf.format(new Date(timeInMillis));
    }

    public static DadosListProvasRcd fromProva(Prova prova) {
        return new DadosListProvasRcd(
                prova.getId(),
                prova.getDistancia(),
                prova.getGenero(),
                prova.getRevezamento(),
                prova.getMedley(),
                prova.getIndiceTecnico(),
                prova.getRecord(),
                prova.getTipoPiscina(),
                prova.getEmpresa().getRazaoSocial(),
                prova.getEtapa().getId(),
                prova.getEtapa().getNome(),
                prova.getTipoNado().getId(),
                prova.getTipoNado().getDescricao(),
                prova.getCategoria().getId(),
                prova.getCategoria().getDescricao(),
                prova.getObservacao(),
                prova.getEtapa().getCampeonato().getNome(),
                prova.getEtapa().getCampeonato().getId());
    }

    public static DadosListProvasRcd fromOptionalProva(Optional<Prova> atletaProva) {
        return atletaProva.map(DadosListProvasRcd::fromProva).orElse(null);
    }

}
