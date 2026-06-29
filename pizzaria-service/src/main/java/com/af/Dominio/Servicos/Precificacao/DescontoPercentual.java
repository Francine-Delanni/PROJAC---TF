package com.af.Dominio.Servicos.Precificacao;

import org.springframework.stereotype.Component;

@Component
public class DescontoPercentual implements PoliticaDeDesconto {

    @Override
    public String codigo() {
        return "PERCENTUAL_5";
    }

    @Override
    public String nome() {
        return "Desconto percentual de 5%";
    }

    @Override
    public double calcular(String cpfCliente, double valorItens) {
        return valorItens * 0.05;
    }
}
