package com.af.Dominio.Servicos.Precificacao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DescontoSemDesconto implements PoliticaDeDesconto {

    @Override
    public String codigo() {
        return "SEM_DESCONTO";
    }

    @Override
    public List<String> codigos() {
        return List.of("SEM_DESCONTO", "NENHUM");
    }

    @Override
    public String nome() {
        return "Sem desconto";
    }

    @Override
    public double calcular(String cpfCliente, double valorItens) {
        return 0.0;
    }
}
