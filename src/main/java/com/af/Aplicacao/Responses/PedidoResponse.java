package com.af.Aplicacao.Responses;

import com.af.Dominio.Entidades.Pedido; // <- enum interno

import java.math.BigDecimal;
import java.util.List;

public class PedidoResponse {
    private Long id;
    private Pedido.Status status;       // <- usa o enum do domínio
    private BigDecimal somaItens;
    private BigDecimal desconto;
    private BigDecimal impostos;
    private BigDecimal total;
    private List<Item> itens;
    private List<Pedido.Status> historicoStatus;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido.Status getStatus() { return status; }
    public void setStatus(Pedido.Status status) { this.status = status; }

    public BigDecimal getSomaItens() { return somaItens; }
    public void setSomaItens(BigDecimal somaItens) { this.somaItens = somaItens; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public BigDecimal getImpostos() { return impostos; }
    public void setImpostos(BigDecimal impostos) { this.impostos = impostos; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<Item> getItens() { return itens; }
    public void setItens(List<Item> itens) { this.itens = itens; }

    public List<Pedido.Status> getHistoricoStatus() { return historicoStatus; }
    public void setHistoricoStatus(List<Pedido.Status> historicoStatus) { this.historicoStatus = historicoStatus; }

    public static class Item {
        private Long produtoId;
        private String descricao;
        private int quantidade;
        private BigDecimal precoUnitario;

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    }
}