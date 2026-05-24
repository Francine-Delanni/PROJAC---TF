package com.af.Dominio.Servicos.Precificacao;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.af.Dominio.Dados.PedidoRepository;

@Service
public class DescontoFidelidade implements ServicoDeDescontos {
    private final PedidoRepository pedidoRepo;

    public DescontoFidelidade(PedidoRepository pedidoRepo) {
        this.pedidoRepo = pedidoRepo;
    }

    @Override
    public double calcular(String cpfCliente, double valorItens) {
        var agora = LocalDateTime.now();
        var de = agora.minusDays(20);

        boolean elegivel = pedidoRepo.countByClienteCpfEntre(cpfCliente, de, agora) > 3;
        return elegivel ? valorItens * 0.07 : 0.0;
    }

    
}
