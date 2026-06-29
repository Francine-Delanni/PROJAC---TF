package com.af.Dominio.Servicos;

import com.af.Dominio.Entidades.Pedido;

public interface EntregaPublisher {

    void publicarPedidoPago(Pedido pedido);
}
