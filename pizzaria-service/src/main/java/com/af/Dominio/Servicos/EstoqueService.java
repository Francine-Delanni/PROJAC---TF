package com.af.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    public boolean verificarDisponibilidade(Long ingredienteId, int quantidade) {
        return true;
    }

    public void baixarEstoque(Long ingredienteId, int quantidade) {
        System.out.printf("Baixando do estoque (FAKE): ingrediente %d, quantidade %d%n",
                ingredienteId, quantidade);
    }

    public boolean temEstoqueDisponivelParaCardapio(Long cardapioId) {
        System.out.printf("Verificando estoque do cardápio %d (FAKE): sempre disponível.%n", cardapioId);
        return true;
    }
}
