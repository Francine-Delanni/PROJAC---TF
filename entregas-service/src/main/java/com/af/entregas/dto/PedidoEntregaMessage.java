package com.af.entregas.dto;

public class PedidoEntregaMessage {

    private Long pedidoId;
    private String cpf;
    private String enderecoEntrega;

    public PedidoEntregaMessage() {
    }

    public PedidoEntregaMessage(Long pedidoId, String cpf, String enderecoEntrega) {
        this.pedidoId = pedidoId;
        this.cpf = cpf;
        this.enderecoEntrega = enderecoEntrega;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
}
