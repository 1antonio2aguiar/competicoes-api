package codiub.competicoes.api.DTO.tipoModalidade;

import codiub.competicoes.api.entity.TiposModalidades;
import java.util.Optional;

public record DadosListModalidadesRcd(
      Long id,
      String nome,
      String descricao
    ){

    public static DadosListModalidadesRcd fromTiposModalidades(TiposModalidades tpModalidade) {
        return new DadosListModalidadesRcd(
        tpModalidade.getId(),
        tpModalidade.getNome(),
        tpModalidade.getDescricao());
    }

    public static DadosListModalidadesRcd fromOptionalTiposModalidades(Optional<TiposModalidades> tpModalidades) {
        return tpModalidades.map(DadosListModalidadesRcd::fromTiposModalidades).orElse(null);
    }

}
