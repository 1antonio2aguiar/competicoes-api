package codiub.competicoes.api.DTO.etapa;

import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.entity.LocaisCompeticoes;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record DadosUpdateEtapaRcd(
    Long localCompeticao,
    @NotBlank(message = "nome.obrigatorio")
    String nome,
    Date dataEtapa,
    Date dataInscricao,
    String pontua,
    String acumula,
    String descricao,
    Integer qtdBalizas
) {

    public DadosUpdateEtapaRcd(Etapa etapa) {
        this(etapa.getLocalCompeticao().getId(),
            etapa.getNome(),
            etapa.getDataEtapa(),
            etapa.getDataInscricao(),
            etapa.getPontua(),
            etapa.getAcumula(),
            etapa.getDescricao(),
            etapa.getQtdBalizas()
        );
    }
    public String getNome(){
        return nome.toUpperCase();
    }
    public String getDescricao(){
        return descricao.toUpperCase();
    }
}
