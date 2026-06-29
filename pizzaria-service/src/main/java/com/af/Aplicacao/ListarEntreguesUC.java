package com.af.Aplicacao;

import com.af.Dominio.Entidades.Pedido;
import com.af.Dominio.Servicos.EntregaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ListarEntreguesUC {

    private final EntregaService entregaService;

    public ListarEntreguesUC(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    public List<Pedido> listarPedidosEntregues(LocalDate dataInicio, LocalDate dataFim) {
        return entregaService.listarEntreguesEntre(dataInicio, dataFim);
    }
}
