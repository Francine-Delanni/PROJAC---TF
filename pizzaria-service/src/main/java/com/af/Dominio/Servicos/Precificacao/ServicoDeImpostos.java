package com.af.Dominio.Servicos.Precificacao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ServicoDeImpostos {

    private final Map<String, PoliticaDeImposto> politicasPorCodigo;
    private final String codigoAtivo;

    public ServicoDeImpostos(
            List<PoliticaDeImposto> politicas,
            @Value("${IMPOSTO_TIPO:${app.imposto.codigo:LEI_10}}") String codigoAtivo
    ) {
        this.politicasPorCodigo = indexarPoliticas(politicas);
        this.codigoAtivo = normalizar(codigoAtivo);

        if (!politicasPorCodigo.containsKey(this.codigoAtivo)) {
            throw new IllegalArgumentException("Politica de imposto nao encontrada: " + codigoAtivo);
        }
    }

    public double calcular(double base) {
        return politicasPorCodigo.get(codigoAtivo).calcular(base);
    }

    public String codigoAtivo() {
        return codigoAtivo;
    }

    private Map<String, PoliticaDeImposto> indexarPoliticas(List<PoliticaDeImposto> politicas) {
        Map<String, PoliticaDeImposto> porCodigo = new LinkedHashMap<>();

        for (PoliticaDeImposto politica : politicas) {
            for (String codigo : politica.codigos()) {
                porCodigo.put(normalizar(codigo), politica);
            }
        }

        return porCodigo;
    }

    private String normalizar(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
}
