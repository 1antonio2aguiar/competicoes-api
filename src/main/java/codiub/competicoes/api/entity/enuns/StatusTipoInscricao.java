package codiub.competicoes.api.entity.enuns;

public enum StatusTipoInscricao {
    Calssificatoria(0, "Classificatória"),
    Semifinal(1, "Semifinal"),
    Final(2, "Final");
    private final Integer codigo;
    private final String descricao;

    private StatusTipoInscricao(int codigo, String descricao) {
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
    public static StatusTipoInscricao toStatusTipoInscricaoEnum(Integer codigo) {
        if (codigo == null) return null;

        for (StatusTipoInscricao si : StatusTipoInscricao.values()) {
            if (codigo.equals(si.getCodigo())) {
                return si;
            }
        }
        throw new IllegalArgumentException("Status tipo inscrição inválido " + codigo);
    }
}

