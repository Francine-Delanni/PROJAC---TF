package com.af.Dominio.Servicos.Precificacao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImpostoUnico implements PoliticaDeImposto {

    @Override
    public String codigo() {
        return "LEI_10";
    }

    @Override
    public List<String> codigos() {
        return List.of("LEI_10", "PADRAO");
    }

    @Override
    public String nome() {
        return "Imposto padrao de 10%";
    }

    @Override
    public double calcular(double base) {
        return base * 0.10;
    }
}
