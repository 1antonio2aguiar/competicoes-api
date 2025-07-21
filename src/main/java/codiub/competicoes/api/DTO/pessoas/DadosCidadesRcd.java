package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Cidades;
import codiub.competicoes.api.entity.pessoas.Paises;

import java.util.Optional;

public record DadosCidadesRcd(
        long id,
        String nome,
        long estado,
        String estadoNome

) {
    public DadosCidadesRcd(Cidades cidades) {
        this(cidades.getId(),
             cidades.getNome(),
             cidades.getEstados().getId(),
                cidades.getEstados().getNome());
    }

    public DadosCidadesRcd(Optional<Cidades> cidades) {
        this(cidades.get().getId(),
                cidades.get().getNome(),
                cidades.get().getEstados().getId(),
                cidades.get().getEstados().getNome());
    }

    public String getNome(){
                return nome.toUpperCase();
        }
}
