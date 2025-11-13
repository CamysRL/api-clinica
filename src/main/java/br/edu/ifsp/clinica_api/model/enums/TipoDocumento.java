package br.edu.ifsp.clinica_api.model.enums;

public enum TipoDocumento {
    RESULTADO_EXAME("resultado de exame"),
    PRESCRICAO("prescrição"),
    PEDIDO_EXAME("pedido de exame"),
    ATESTADO("atestado");

    private final String descricao;

    TipoDocumento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
