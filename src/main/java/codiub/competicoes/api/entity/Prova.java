package codiub.competicoes.api.entity;

import codiub.competicoes.api.entity.pessoas.Pessoas;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@EqualsAndHashCode(of = "id")
@Entity(name = "Prova")
@Table(name = "provas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Prova {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer distancia;
    private String genero;
    private String revezamento;
    private String medley;
    private String observacao;
    @Column(name = "indice_tecnico")
    private Long indiceTecnico; // Tipo Long para milissegundos
    @Column(name = "record")
    private Long record;
    private String tipoPiscina;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "etapa_id")
    private Etapa etapa;

    @ManyToOne
    @JoinColumn(name = "tipo_nado_id")
    private TipoNado tipoNado;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}

