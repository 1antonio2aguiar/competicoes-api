package codiub.competicoes.api.DTO.empresa;


import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.entity.TiposModalidades;

import java.util.Optional;

public record DadosListEmpresaRcd(
    Long id,
    String cnpj,
    String RazaoSocial,
    String Atividade,
    String telefone,
    String InscricaoEstadual){

    public DadosListEmpresaRcd(Empresa empresa){
        this(empresa.getId(),
            empresa.getCnpj(),
            empresa.getRazaoSocial(),
            empresa.getAtividade(),
            empresa.getTelefone(),
            empresa.getInscricaoEstadual()
        );
    }
}
