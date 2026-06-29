package com.af.Dominio.Servicos.Precificacao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImpostoReduzido implements PoliticaDeImposto {

    @Override
    public String codigo() {
        return "REDUZIDO";
    }

    @Override
    public List<String> codigos() {
        return List.of("REDUZIDO", "LEI_5");
    }

    @Override
    public String nome() {
        return "Imposto reduzido de 5%";
    }

    @Override
    public double calcular(double base) {
        return base * 0.05;
    }
}
