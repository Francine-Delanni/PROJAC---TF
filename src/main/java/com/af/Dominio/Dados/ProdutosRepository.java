package com.af.Dominio.Dados;

import java.util.List;

import com.af.Dominio.Entidades.Produto;

public interface ProdutosRepository {
    Produto recuperaProdutoPorid(long id);
    List<Produto> recuperaProdutosCardapio(long id);
}

