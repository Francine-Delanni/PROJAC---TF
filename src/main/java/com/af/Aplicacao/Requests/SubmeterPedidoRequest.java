package com.af.Aplicacao.Requests;

import java.util.List;

public class SubmeterPedidoRequest {
    public String cpf;
    public String nome;
    public String celular;
    public String endereco;
    public String email;
    public List<Item> itens;

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    

    public List<Item> getItens() { return itens; }
    public void setItens(List<Item> itens) { this.itens = itens; }


    public static class Item {
        public long produtoId;
        public int quantidade;
        public long getProdutoId() {
            return produtoId;
        }
        public void setProdutoId(long produtoId) {
            this.produtoId = produtoId;
        }
        public int getQuantidade() {
            return quantidade;
        }
        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        
    }


}
