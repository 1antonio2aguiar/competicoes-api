package codiub.competicoes.api.entity.enuns;

public enum StatusApuracao {
    Classificado(0, "Classificado"),
    Desclassifacado(1, "Desclassifacado"),
    Vencedor(2, "Vencedor"),
    Apurado(3, "Apurado"),
    Eliminado(4, "Eliminado");
    private final Integer codigo;
    private final String descricao;

    private StatusApuracao(int codigo, String descricao) {
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
    public static StatusApuracao toStatusApuracaoEnum(Integer codigo) {
        if (codigo == null) return null;

        for (StatusApuracao sa : StatusApuracao.values()) {
            if (codigo.equals(sa.getCodigo())) {
                return sa;
            }
        }
        throw new IllegalArgumentException("Status apuração inválido " + codigo);
    }
}

