package codiub.competicoes.api.entity.enuns;

public enum StatusResultado {
    Classificado(1, "Classificado"),
    Desclassifacado(2, "Desclassifacado"),
    Vencedor(3, "Vencedor");
    private final Integer codigo;
    private final String descricao;

    private StatusResultado(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
    public int getCodigo() {
        return codigo;
    }
    public String getDescricao() {
        return descricao;
    }

    // static pode chamar se que o obj seja instanciado.
    public static StatusResultado toStatusInscricaoEnum(Integer codigo) {
        if (codigo == null) return null;

        for (StatusResultado si : StatusResultado.values()) {
            if (codigo.equals(si.getCodigo())) {
                return si;
            }
        }
        throw new IllegalArgumentException("Status inscrição inválido " + codigo);
    }
}

