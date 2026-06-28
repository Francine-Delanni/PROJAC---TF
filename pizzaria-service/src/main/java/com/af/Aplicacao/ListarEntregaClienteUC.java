package com.af.Aplicacao;

import com.af.Dominio.Entidades.Pedido;
import com.af.Dominio.Servicos.EntregaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ListarEntregaClienteUC {

    private final EntregaService entregaService;

    public ListarEntregaClienteUC(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    public List<Pedido> listarPedidosEntregues(String cpf, LocalDate dataInicio, LocalDate dataFim) {
        return entregaService.listarEntreguesPorClienteEntre(cpf, dataInicio, dataFim);
    }
}