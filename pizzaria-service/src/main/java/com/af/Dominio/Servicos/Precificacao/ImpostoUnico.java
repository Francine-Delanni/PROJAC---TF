package com.af.Dominio.Servicos.Precificacao;

import org.springframework.stereotype.Service;

@Service
public class ImpostoUnico implements ServicoDeImpostos {
    @Override
    public double calcular(double base) {
        return base * 0.10;
    }
}
