package codiub.competicoes.api.DTO.pessoas.pessoasfj;

import java.time.LocalDate;

public record DadosPessoasGeralRcd(
        // Campos Comuns (da entidade Pessoa)
        Long id,
        String nome,
        //String fisicaJuridica, // "F" ou "J"
        String situacao,       // Descrição do Enum
        Long tipoPessoaId,
        String tipoPessoaNome,
        String observacao,

        // Campos Específicos de Pessoa Física (podem ser null se for Pessoa Jurídica)
        String cpf,
        String sexo,
        String estadoCivil,
        LocalDate dataNascimento,
        String nomeMae,
        String nomePai,

        // Campos Específicos de Pessoa Jurídica (podem ser null se for Pessoa Física)
        String cnpj,
        String nomeFantasia,
        String objetoSocial,
        String microEmpresa,
        Integer tipoEmpresa
) {

}