package com.af.Adaptadores.Entregas;

public class PedidoEntregaMessage {

    private Long pedidoId;
    private String cpf;
    private String enderecoEntrega;

    public PedidoEntregaMessage(Long pedidoId, String cpf, String enderecoEntrega) {
        this.pedidoId = pedidoId;
        this.cpf = cpf;
        this.enderecoEntrega = enderecoEntrega;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }
}
