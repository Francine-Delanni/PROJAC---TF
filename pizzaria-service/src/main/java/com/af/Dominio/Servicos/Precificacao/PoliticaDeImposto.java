package com.af.Dominio.Servicos.Precificacao;

import java.util.List;

public interface PoliticaDeImposto {

    String codigo();

    String nome();

    double calcular(double base);

    default List<String> codigos() {
        return List.of(codigo());
    }
}
