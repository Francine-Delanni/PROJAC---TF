
package com.af.Aplicacao;

import org.springframework.stereotype.Service;

import com.af.Dominio.Servicos.PedidoService;
import com.af.Dominio.Entidades.Pedido;

@Service
public class SolicitarStatusUC {
	private final PedidoService pedidoService;

	public SolicitarStatusUC(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	public Pedido.Status run(Long pedidoId, String cpf) {
		return pedidoService.status(pedidoId, cpf);
	}
}

