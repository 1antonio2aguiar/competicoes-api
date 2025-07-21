package codiub.competicoes.api.entity.enuns;

public enum StatusVeiculo {
    LIVRE(0,"LIVRE"),
    MANUTENCAO(1,"EM MANUTENÇÃO"),
    TRANSITO(2,"EM TRANSITO"),
    BAIXADO(3,"BAIXADO");
    private final Integer codigo;
    private final String descricao;

    private StatusVeiculo(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }
    public String getDescricao() { return descricao; }

    // static pode chamar se que o obj seja instanciado.
    public static StatusVeiculo toStatusVeiculoEnum(Integer codigo) {
        if(codigo == null) return null;

        for (StatusVeiculo sv : StatusVeiculo.values()) {
            if(codigo.equals(sv.getCodigo())) {
                return sv;
            }
        }
        throw new IllegalArgumentException("Status veiculo inválido " + codigo);
    }

}
