package com.af.Dominio.Servicos.Precificacao;

import java.util.List;

public interface PoliticaDeDesconto {

    String codigo();

    String nome();

    double calcular(String cpfCliente, double valorItens);

    default List<String> codigos() {
        return List.of(codigo());
    }
}
