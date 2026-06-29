
package com.af.Aplicacao;

import org.springframework.stereotype.Service;

import com.af.Dominio.Servicos.PedidoService;
import com.af.Aplicacao.Responses.PedidoResponse;

@Service
public class PagamentoPedidoUC {
	private final PedidoService pedidoService;

	public PagamentoPedidoUC(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	public PedidoResponse run(Long pedidoId, String cpf) {
		return pedidoService.pagamento(pedidoId, cpf);
	}
}

