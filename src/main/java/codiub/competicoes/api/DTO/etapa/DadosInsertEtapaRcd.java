package codiub.competicoes.api.DTO.etapa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record DadosInsertEtapaRcd(
        Long empresa,
        Long campeonato,
        Long localCompeticao,
        @NotBlank(message = "nome.obrigatório")
        @Size(max = 100, min = 3)
        String nome,
        String descricao,
        Date dataEtapa,
        Date dataInscricao,
        String pontua,
        String acumula,
        Integer qtdBalizas
) {
        public String getNome(){
                return nome.toUpperCase();
        }
        public String getDescricao() {
                return (descricao == null) ? null : descricao.toUpperCase();
        }
}
