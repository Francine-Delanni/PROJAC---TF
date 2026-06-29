package com.af.Dominio.Dados;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import com.af.Dominio.Entidades.Pedido;

public interface PedidoRepository {

    Pedido save(Pedido pedido);

    Optional<Pedido> findById(long id);

    long countByClienteCpfEntre(String cpf, LocalDateTime de, LocalDateTime ate);

    void updateStatus(long id, Pedido.Status status);

    void updateStatusAndDataEntrega(long id, Pedido.Status status, java.time.LocalDateTime dataEntrega);

    void removeById(long id);

    List<Pedido> pedidosEntreDuasDatas(Pedido.Status status, java.time.LocalDate inicio, java.time.LocalDate fim);

    List<Pedido> pedidosEntreDuasDatasByCliente(String cpf, Pedido.Status status, java.time.LocalDate inicio, java.time.LocalDate fim);
}
