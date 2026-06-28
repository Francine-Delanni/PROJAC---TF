package com.af.estoque.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estoque")
public class EstoqueProduto {

    @Id
    private Long produtoId;

    private String descricao;
    private int quantidade;

    public EstoqueProduto() {
    }

    public EstoqueProduto(Long produtoId, String descricao, int quantidade) {
        this.produtoId = produtoId;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
