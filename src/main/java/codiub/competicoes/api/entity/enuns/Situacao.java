package codiub.competicoes.api.entity.enuns;

public enum Situacao {
    ATIVO(0,"ATIVO"),
    INATIVO(1,"INATIVO"),
    BLOQUEADO(2,"BLOQUEADO");
    private final Integer codigo;
    private final String descricao;

    private Situacao(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }
    public String getDescricao() { return descricao; }

    // static pode chamar se que o obj seja instanciado.
    public static Situacao toSituacaoEnum(Integer codigo) {
        if(codigo == null) return null;

        for (Situacao sv : Situacao.values()) {
            if(codigo.equals(sv.getCodigo())) {
                return sv;
            }
        }
        throw new IllegalArgumentException("Situação inválida " + codigo);
    }

}
