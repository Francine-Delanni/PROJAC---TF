package com.af.Aplicacao;

import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;

import com.af.Aplicacao.Responses.PedidoResponse;
import com.af.Dominio.Servicos.PedidoService;
import com.af.Aplicacao.Requests.SubmeterPedidoRequest;

@Service
public class SubmeterPedidoUC {
     private PedidoService pedidoService;

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    public PedidoResponse run(SubmeterPedidoRequest request) {
        return pedidoService.submeter(request);
    }
}
