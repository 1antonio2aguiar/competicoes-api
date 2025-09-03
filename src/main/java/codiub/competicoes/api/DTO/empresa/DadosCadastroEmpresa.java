package codiub.competicoes.api.DTO.empresa;

public record DadosCadastroEmpresa(
        String razaoSocial,
        String atividade,
        String telefone,
        String cnpj,
        String inscricaoEstadual
) {
}
