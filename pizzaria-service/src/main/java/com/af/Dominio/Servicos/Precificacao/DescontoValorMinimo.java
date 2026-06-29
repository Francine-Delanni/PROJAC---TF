package com.af.Dominio.Servicos.Precificacao;

import org.springframework.stereotype.Component;

@Component
public class DescontoValorMinimo implements PoliticaDeDesconto {

    private static final double VALOR_MINIMO = 100.0;
    private static final double DESCONTO = 10.0;

    @Override
    public String codigo() {
        return "VALOR_MINIMO";
    }

    @Override
    public String nome() {
        return "Desconto de R$ 10,00 para pedidos a partir de R$ 100,00";
    }

    @Override
    public double calcular(String cpfCliente, double valorItens) {
        return valorItens >= VALOR_MINIMO ? DESCONTO : 0.0;
    }
}
