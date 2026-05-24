
package com.af.Aplicacao;

import org.springframework.stereotype.Service;

import com.af.Dominio.Servicos.PedidoService;
import com.af.Aplicacao.Responses.PedidoResponse;

@Service
public class CancelarPedidoUC {
	private final PedidoService pedidoService;

	public CancelarPedidoUC(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	public PedidoResponse run(Long pedidoId) {
		return pedidoService.cancelar(pedidoId);
	}
}

