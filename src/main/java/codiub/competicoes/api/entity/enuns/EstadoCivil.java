package codiub.competicoes.api.entity.enuns;

public enum EstadoCivil {
    AMAZIADO(0,"AMAZIADO(A)"),
    CASADO(1,"CASADO(A)"),
    SOLTEIRO(2,"SOLTEIRO(A)"),
    UNIAOESTAVEL(3,"UNIAO ESTÁVEL"),
    VIUVO(4,"VIUVO(A)"),
    OUTRO(5,"OUTRO");
    private final Integer codigo;
    private final String descricao;

    private EstadoCivil(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }
    public String getDescricao() { return descricao; }

    // static pode chamar se que o obj seja instanciado.
    public static EstadoCivil toEstadocivilEnum(Integer codigo) {
        if(codigo == null) return null;

        for (EstadoCivil sv : EstadoCivil.values()) {
            if(codigo.equals(sv.getCodigo())) {
                return sv;
            }
        }
        throw new IllegalArgumentException("Estado Civíl inválido " + codigo);
    }

}
