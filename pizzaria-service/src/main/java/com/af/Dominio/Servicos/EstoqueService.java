package com.af.Dominio.Servicos;

public interface EstoqueService {

    boolean verificarDisponibilidade(Long produtoId, int quantidade);

    void baixarEstoque(Long produtoId, int quantidade);

    default boolean temEstoqueDisponivelParaCardapio(Long cardapioId) {
        return true;
    }
}
