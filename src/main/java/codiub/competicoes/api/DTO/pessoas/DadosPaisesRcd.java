package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Paises;

import java.util.Optional;

public record DadosPaisesRcd(
        long id,
        String nome,
        String sigla,
        String nacionalidade

) {
    public DadosPaisesRcd(Paises paises) {
        this(paises.getId(),
            paises.getNome(),
            paises.getSigla(),
            paises.getNacionalidade());
    }

    public DadosPaisesRcd(Optional<Paises> paises) {
        this(paises.get().getId(),
                paises.get().getNome(),
                paises.get().getSigla(),
                paises.get().getNacionalidade());
    }

    public String getNome(){
                return nome.toUpperCase();
        }
    public String getSigla(){
        return sigla.toUpperCase();
    }
    public String getNacionalidade() {
        return nacionalidade != null ? nacionalidade.toUpperCase() : null; // Retorna null se nacionalidade for null
    }
}
