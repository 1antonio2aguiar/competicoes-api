package codiub.competicoes.api.DTO.inscricoes;

import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.Inscricoes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public record DadosListInscricoesRcd(
        Long id,
        Integer baliza,
        Integer serie,
        Integer status,
        String statusDescricao,
        Integer statusTipoInscricao,
        String statusTipoInscricaoDescricao,
        String observacao,
        Long atletaId,
        String atletaNome,
        Long provaId,
        Integer distancia,
        String genero,
        String revezamento,
        String medley,
        String tipoPiscina,
        Long etapaId,
        String etapaNome,
        Long campeonatoId,
        String campeonatoNome,
        String equipeNome,
        DadosPessoasReduzidoRcd atleta
) {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

    // Construtor "completo" (gerado pelo record)
    public DadosListInscricoesRcd(Long id, Integer baliza, Integer serie, Integer status,
      String statusDescricao, Integer statusTipoInscricao, String statusTipoInscricaoDescricao,
      String observacao, Long atletaId, String atletaNome, Long provaId, Integer distancia,
      String genero, String revezamento, String medley, String tipoPiscina, Long etapaId,
      String etapaNome, Long campeonatoId, String campeonatoNome, String equipeNome,DadosPessoasReduzidoRcd atleta) {
        this.id = id;
        this.baliza = baliza;
        this.serie = serie;
        this.status = status;
        this.statusDescricao = statusDescricao;
        this.statusTipoInscricao = statusTipoInscricao;
        this.statusTipoInscricaoDescricao  = statusTipoInscricaoDescricao;
        this.observacao = observacao;
        this.atletaId = atletaId;
        this.atletaNome = atletaNome;
        this.provaId = provaId;
        this.distancia = distancia;
        this.genero = genero;
        this.revezamento = revezamento;
        this.medley = medley;
        this.tipoPiscina = tipoPiscina;
        this.etapaId = etapaId;
        this.etapaNome = etapaNome;
        this.campeonatoId = campeonatoId;
        this.campeonatoNome = campeonatoNome;
        this.equipeNome = equipeNome;
        this.atleta = atleta;
    }


    // Método para formatar o tempo (mantido como estava)
    private static String formatTime(Long timeInMillis) {
        if (timeInMillis == null) {
            return null; // Ou retorne uma string vazia, se preferir ""
        }
        return sdf.format(new Date(timeInMillis));
    }

    // Método para criar o DTO a partir de uma entidade Inscricoes
    public static DadosListInscricoesRcd fromInscricao(Inscricoes inscricao) {
        return new DadosListInscricoesRcd(
                inscricao.getId(),
                inscricao.getBaliza(),
                inscricao.getSerie(),
                inscricao.getStatus().getCodigo(),
                inscricao.getStatus().getDescricao(),
                inscricao.getTipoInscricao() != null ? inscricao.getTipoInscricao().getCodigo() : null,
                inscricao.getTipoInscricao() != null ? inscricao.getTipoInscricao().getDescricao() : null,
                inscricao.getObservacao(),
                inscricao.getAtleta().getId(),
                inscricao.getAtleta().getPessoa().getNome(),
                inscricao.getProva().getId(),
                inscricao.getProva().getDistancia(),
                inscricao.getProva().getGenero(),
                inscricao.getProva().getRevezamento(),
                inscricao.getProva().getMedley(),
                inscricao.getProva().getTipoPiscina(),
                inscricao.getProva().getEtapa().getId(),
                inscricao.getProva().getEtapa().getNome(),
                inscricao.getProva().getEtapa().getCampeonato().getId(),
                inscricao.getProva().getEtapa().getCampeonato().getNome(),
                inscricao.getAtleta().getEquipe().getNome(),
                inscricao.getAtleta() != null ? codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd.fromPessoas(inscricao.getAtleta().getPessoa()) : null
        );
    }

    // Método para lidar com o Optional<Inscricoes>
    public static DadosListInscricoesRcd fromOptionalInscricao(Optional<Inscricoes> inscricaoOptional) {
        return inscricaoOptional.map(DadosListInscricoesRcd::fromInscricao).orElse(null);
    }
}