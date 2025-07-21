package codiub.competicoes.api.DTO.tiposVeiculos;

import codiub.competicoes.api.entity.TiposVeiculos;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record DadosTiposVeiculosRcd(Long id,String descricao) {
        public  DadosTiposVeiculosRcd(TiposVeiculos tiposVeiculos){
                this(tiposVeiculos.getId(),
                tiposVeiculos.getDescricao().toUpperCase());
        }
        public String getDescricao(){
                return descricao.toUpperCase();
        }
}
