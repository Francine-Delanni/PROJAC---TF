package com.af.Adaptadores.Entregas;

import com.af.Config.RabbitMQConfig;
import com.af.Dominio.Entidades.Cliente;
import com.af.Dominio.Entidades.Pedido;
import com.af.Dominio.Servicos.EntregaPublisher;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoEntregaPublisher implements EntregaPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PedidoEntregaPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicarPedidoPago(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        var mensagem = new PedidoEntregaMessage(
                pedido.getId(),
                cliente != null ? cliente.getCpf() : null,
                cliente != null ? cliente.getEndereco() : null
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_ENTREGAS, mensagem, this::semCabecalhosDeTipoJava);
    }

    private Message semCabecalhosDeTipoJava(Message message) {
        message.getMessageProperties().getHeaders().remove("__TypeId__");
        message.getMessageProperties().getHeaders().remove("__ContentTypeId__");
        message.getMessageProperties().getHeaders().remove("__KeyTypeId__");
        return message;
    }
}
