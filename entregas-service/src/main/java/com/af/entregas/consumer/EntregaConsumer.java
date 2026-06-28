package com.af.entregas.consumer;

import com.af.entregas.config.RabbitMQConfig;
import com.af.entregas.dto.PedidoEntregaMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EntregaConsumer {

    @RabbitListener(queues = RabbitMQConfig.FILA_ENTREGAS)
    public void receber(PedidoEntregaMessage mensagem) {
        System.out.println("Instância de entrega recebeu pedido: " + mensagem.getPedidoId());
        System.out.println("CPF: " + mensagem.getCpf());
        System.out.println("Endereço: " + mensagem.getEnderecoEntrega());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Pedido entregue pela instância atual: " + mensagem.getPedidoId());
    }
}
