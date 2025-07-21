package codiub.competicoes.api.DTO.empresa;


import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.entity.TiposModalidades;

import java.util.Optional;

public record DadosListEmpresaRcd(Long id, String cnpj, String nome, String sigla, String logo, Character situacao, Character filial, Long matriz){

    public DadosListEmpresaRcd(Empresa empresa){
        this(empresa.getId(),
            empresa.getCnpj(),
            empresa.getNome(),
            empresa.getSigla(),
            empresa.getLogo(),
            empresa.getSituacao(),
            empresa.getFilial(),
            empresa.getMatriz()
        );
    }
}
