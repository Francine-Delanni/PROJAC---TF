package com.af.Dominio.Dados;

import java.util.List;

import com.af.Dominio.Entidades.CabecalhoCardapio;
import com.af.Dominio.Entidades.Cardapio;
import com.af.Dominio.Entidades.Produto;

public interface CardapioRepository {
    List<CabecalhoCardapio> cardapiosDisponiveis();
    Cardapio recuperaPorId(long id);
    List<Produto> indicacoesDoChef();
}

