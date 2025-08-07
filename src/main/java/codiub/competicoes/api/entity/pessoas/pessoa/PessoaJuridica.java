package codiub.competicoes.api.entity.pessoas.pessoa;

import codiub.competicoes.api.entity.enuns.Situacao;
import codiub.competicoes.api.entity.pessoas.TiposPessoas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// import lombok.Data;
// import lombok.EqualsAndHashCode;

@Entity
@Table(name = "vw_dados_pessoa_juridica") // Mapeia para a TABELA BASE 'dados_pessoa_juridica'
@DiscriminatorValue("J") // Valor que será armazenado na DiscriminatorColumn da tabela 'pessoas'
@Getter
@Setter
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = true)
public class PessoaJuridica extends Pessoa {
    private static final long serialVersionUID = 1L;

    // O @Id é herdado da classe Pessoas.

    @Column(length = 14, unique = true) // CNPJ deve ser único
    private String cnpj;

    @Column(name = "nome_fantasia", length = 100)
    private String nomeFantasia; // Java Naming Convention

    @Column(name = "objeto_social", length = 100)
    private String objetoSocial; // Java Naming Convention

    @Column(name = "micro_empresa", length = 1)
    private String microEmpresa;

    @Column(name = "tipo_empresa") // Mapeia para coluna tipo_empresa
    private Integer tipoEmpresa; // Tipo pode ser Integer ou um Enum específico

    // Construtor para criar instâncias (opcional, mas útil)
    public PessoaJuridica(String nome, String observacao, Situacao situacao, TiposPessoas tiposPessoas,
                          String cnpj, String nomeFantasia, String objetoSocial,
                          String microEmpresa, Integer tipoEmpresa) {
        super(nome, /*fisicaJuridica,*/ observacao, situacao, tiposPessoas); // Chama o construtor da classe pai
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.objetoSocial = objetoSocial;
        this.microEmpresa = microEmpresa;
        this.tipoEmpresa = tipoEmpresa;
        // this.setFisicaJuridica("J"); // O JPA cuida disso
    }
}