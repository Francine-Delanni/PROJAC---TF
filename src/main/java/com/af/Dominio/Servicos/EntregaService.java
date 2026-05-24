
package com.af.Dominio.Servicos;

import com.af.Dominio.Dados.PedidoRepository;
import com.af.Dominio.Entidades.Pedido;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class EntregaService {
    private final PedidoRepository pedidoRepository;

    public EntregaService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public void iniciarTransporte(Long pedidoId) {
        pedidoRepository.updateStatus(pedidoId, Pedido.Status.TRANSPORTE);
    }

    public void marcarEntregue(Long pedidoId) {
        pedidoRepository.updateStatusAndDataEntrega(
            pedidoId,
            Pedido.Status.ENTREGUE,
            java.time.LocalDateTime.now()
        );
    }

    public List<Pedido> listarEntreguesEntre(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.pedidosEntreDuasDatas(Pedido.Status.ENTREGUE, inicio, fim);
    }

    public List<Pedido> listarEntreguesPorClienteEntre(String cpf, LocalDate inicio, LocalDate fim) {
        return pedidoRepository.pedidosEntreDuasDatasByCliente(cpf, Pedido.Status.ENTREGUE, inicio, fim);
    }
}
