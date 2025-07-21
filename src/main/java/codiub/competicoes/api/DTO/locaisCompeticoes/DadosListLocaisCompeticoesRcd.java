package codiub.competicoes.api.DTO.locaisCompeticoes;


import codiub.competicoes.api.entity.LocaisCompeticoes;

public record DadosListLocaisCompeticoesRcd(Long id, String nome, String endereco){

    public DadosListLocaisCompeticoesRcd(LocaisCompeticoes locaisCompeticoes){
        this(locaisCompeticoes.getId(),
            locaisCompeticoes.getNome(),
            locaisCompeticoes.getEndereco());
    }
}
