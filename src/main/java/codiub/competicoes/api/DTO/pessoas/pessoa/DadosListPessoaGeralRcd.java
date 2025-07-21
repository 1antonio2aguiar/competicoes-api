package codiub.competicoes.api.DTO.pessoas.pessoa;


import codiub.competicoes.api.entity.pessoas.pessoa.Pessoa;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaJuridica;
import codiub.competicoes.api.entity.pessoas.TiposPessoas;

import java.time.LocalDate;
import java.util.Optional;

public record DadosListPessoaGeralRcd(
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
    /**
     * Método de fábrica para converter uma entidade Pessoa (que pode ser
     * PessoaFisica ou PessoaJuridica) para este DTO genérico.
     */
    public static DadosListPessoaGeralRcd fromPessoa(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        TiposPessoas tp = pessoa.getTiposPessoas();
        String tipoPessoaNome = (tp != null) ? tp.getNome() : null;
        Long tipoPessoaId = (tp != null) ? tp.getId() : null;
        String situacaoDesc = pessoa.getSituacao() != null ? pessoa.getSituacao().getDescricao() : null;

        // Campos específicos de Pessoa Física
        String cpf = null;
        String sexo = null;
        String estadoCivil = null;
        LocalDate dataNascimento = null;
        String nomeMae = null;
        String nomePai = null;

        // Campos específicos de Pessoa Jurídica
        String cnpj = null;
        String nomeFantasia = null;
        String objetoSocial = null;
        String microEmpresa = null;
        Integer tipoEmpresa = null;

        // Verifica o tipo real da instância e preenche os campos específicos
        if (pessoa instanceof PessoaFisica pf) { // Usa pattern matching (Java 16+)
            cpf = pf.getCpf();
            sexo = pf.getSexo();
            estadoCivil = pf.getEstadoCivil() != null ? pf.getEstadoCivil().getDescricao() : null;
            dataNascimento = pf.getDataNascimento();
            nomeMae = pf.getNomeMae();
            nomePai = pf.getNomePai();
        } else if (pessoa instanceof PessoaJuridica pj) { // Usa pattern matching
            cnpj = pj.getCnpj();
            nomeFantasia = pj.getNomeFantasia();
            objetoSocial = pj.getObjetoSocial();
            microEmpresa = pj.getMicroEmpresa();
            tipoEmpresa = pj.getTipoEmpresa();
        }
        // Para Java < 16, você faria o cast:
        // if (pessoa instanceof PessoaFisica) {
        //     PessoaFisica pf = (PessoaFisica) pessoa;
        //     // ... preenche campos de pf ...
        // } else if (pessoa instanceof PessoaJuridica) {
        //     PessoaJuridica pj = (PessoaJuridica) pessoa;
        //     // ... preenche campos de pj ...
        // }

        return new DadosListPessoaGeralRcd(
                pessoa.getId(),
                pessoa.getNome(),
                //pessoa.getFisicaJuridica(), // O valor 'F' ou 'J' vindo da entidade pai
                situacaoDesc,
                tipoPessoaId,
                tipoPessoaNome,
                pessoa.getObservacao(),
                // Pessoa Física
                cpf,
                sexo,
                estadoCivil,
                dataNascimento,
                nomeMae,
                nomePai,
                // Pessoa Jurídica
                cnpj,
                nomeFantasia,
                objetoSocial,
                microEmpresa,
                tipoEmpresa
        );
    }

    // Opcional: Se você tiver um Optional<Pessoa>
    public static DadosListPessoaGeralRcd fromOptionalPessoa(Optional<Pessoa> pessoaOptional) {
        return pessoaOptional.map(DadosListPessoaGeralRcd::fromPessoa).orElse(null);
    }
}