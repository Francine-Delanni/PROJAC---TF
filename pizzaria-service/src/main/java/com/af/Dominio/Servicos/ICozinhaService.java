package com.af.Dominio.Servicos;

import com.af.Dominio.Entidades.Pedido;

public interface ICozinhaService {
    void chegadaDePedido(Pedido p);
    void pedidoPronto();
}
