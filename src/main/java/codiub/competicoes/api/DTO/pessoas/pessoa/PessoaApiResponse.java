package codiub.competicoes.api.DTO.pessoas.pessoa;

import java.time.LocalDate;

public record PessoaApiResponse( // Renomeado para clareza
         Long id,
         String nome,
         //String fisicaJuridica, // "F"
         String situacao, // Descrição do Enum
         Long tipoPessoaId,
         String tipoPessoaNome,
         String cpf,
         String sexo,
         String estadoCivil,
         LocalDate dataNascimento,
         String nomeMae,
         String nomePai,
         String observacao
) {}