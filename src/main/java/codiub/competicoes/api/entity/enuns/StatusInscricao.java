package codiub.competicoes.api.entity.enuns;

public enum StatusInscricao {
    Inscrito(0, "Inscrito"),
    Confirmado(1, "Confirmado"),
    Presente(2, "Presente"),
    Ausente(3, "Ausente"),
    Desclassificado_DSQ(4, "Desclassificado (DSQ)"),
    Desistiu_WD(5, "Desistiu (WD)"),
    Apurado(6, "Apurado");
    private final Integer codigo;
    private final String descricao;

    private StatusInscricao(int codigo, String descricao) {
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
    public static StatusInscricao toStatusInscricaoEnum(Integer codigo) {
        if (codigo == null) return null;

        for (StatusInscricao si : StatusInscricao.values()) {
            if (codigo.equals(si.getCodigo())) {
                return si;
            }
        }
        throw new IllegalArgumentException("Status inscrição inválido " + codigo);
    }
}

