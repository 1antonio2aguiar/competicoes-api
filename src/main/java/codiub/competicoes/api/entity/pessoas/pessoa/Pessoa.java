package codiub.competicoes.api.entity.pessoas.pessoa;

import codiub.competicoes.api.entity.enuns.Situacao;
import codiub.competicoes.api.entity.pessoas.TiposPessoas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
//@Table(name = "pessoas_competicoes")
@Table(schema = "pessoas", name = "pessoas")
@Inheritance(strategy = InheritanceType.JOINED) // Estratégia de herança
@DiscriminatorColumn(name = "fisica_juridica",
        discriminatorType = DiscriminatorType.STRING,
        length = 1)
@Getter
@Setter
@NoArgsConstructor // Necessário para JPA
public abstract class Pessoa implements Serializable { // Tornar abstrata
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;
    //private String fisicaJuridica;

    // Este campo será gerenciado pelo @DiscriminatorColumn e @DiscriminatorValue
    // mas pode ser útil tê-lo se você precisa consultá-lo diretamente.
    // Se o JPA gerenciar completamente, ele pode ser inferido.
    // Para controle explícito, mantenha-o, mas pode ser redundante.
    //@Column(name = "fisica_juridica", length = 1, nullable = false, updatable = false, insertable = false)
    //private String fisicaJuridica; // O valor será 'F' ou 'J' (definido nos @DiscriminatorValue)

    @Column(length = 500)
    private String observacao;

    @Enumerated(EnumType.ORDINAL) // Ou EnumType.STRING se preferir salvar "ATIVO", "INATIVO"
    @Column(nullable = false)
    private Situacao situacao;

    // Relacionamento com TiposPessoas (se ainda necessário)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_pessoa_id") // Nome da coluna FK na tabela 'pessoas'
    private TiposPessoas tiposPessoas;

    // Construtor protegido para uso pelas subclasses (se necessário)
    protected Pessoa(String nome, String observacao, Situacao situacao, TiposPessoas tiposPessoas) {
        this.nome = nome;
        this.observacao = observacao;
        this.situacao = situacao;
        this.tiposPessoas = tiposPessoas;
    }
}