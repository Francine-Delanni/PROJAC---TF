package com.af.Dominio.Servicos.Precificacao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ServicoDeDescontos {

    private final Map<String, PoliticaDeDesconto> politicasPorCodigo;
    private volatile String codigoAtivo;

    public ServicoDeDescontos(
            List<PoliticaDeDesconto> politicas,
            @Value("${app.desconto.codigo:FIDELIDADE}") String codigoInicial
    ) {
        this.politicasPorCodigo = indexarPoliticas(politicas);
        selecionar(codigoInicial);
    }

    public double calcular(String cpfCliente, double valorItens) {
        return politicasPorCodigo.get(codigoAtivo).calcular(cpfCliente, valorItens);
    }

    public List<DescontoDisponivel> listar() {
        return politicasPorCodigo.values().stream()
                .distinct()
                .map(politica -> new DescontoDisponivel(
                        politica.codigo(),
                        politica.nome(),
                        politica.codigo().equals(codigoAtivo)
                ))
                .toList();
    }

    public DescontoDisponivel selecionar(String codigo) {
        String codigoNormalizado = normalizar(codigo);
        PoliticaDeDesconto politica = politicasPorCodigo.get(codigoNormalizado);

        if (politica == null) {
            throw new IllegalArgumentException("Politica de desconto nao encontrada: " + codigo);
        }

        codigoAtivo = normalizar(politica.codigo());
        return new DescontoDisponivel(politica.codigo(), politica.nome(), true);
    }

    public String codigoAtivo() {
        return codigoAtivo;
    }

    private Map<String, PoliticaDeDesconto> indexarPoliticas(List<PoliticaDeDesconto> politicas) {
        Map<String, PoliticaDeDesconto> porCodigo = new LinkedHashMap<>();

        for (PoliticaDeDesconto politica : politicas) {
            for (String codigo : politica.codigos()) {
                porCodigo.put(normalizar(codigo), politica);
            }
        }

        return porCodigo;
    }

    private String normalizar(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }

    public record DescontoDisponivel(String codigo, String nome, boolean ativo) {
    }
}
