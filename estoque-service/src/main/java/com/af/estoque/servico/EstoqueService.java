package com.af.estoque.servico;

import com.af.estoque.dominio.EstoqueProduto;
import com.af.estoque.repositorio.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository repository;

    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
    }

    public List<EstoqueProduto> listar() {
        return repository.findAll();
    }

    public boolean disponivel(Long produtoId, int quantidade) {
        return repository.findById(produtoId)
                .map(produto -> produto.getQuantidade() >= quantidade)
                .orElse(false);
    }

    public void baixar(Long produtoId, int quantidade) {
        EstoqueProduto produto = repository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no estoque"));

        if (produto.getQuantidade() < quantidade) {
            throw new IllegalStateException("Estoque insuficiente");
        }

        produto.setQuantidade(produto.getQuantidade() - quantidade);
        repository.save(produto);
    }

    public EstoqueProduto salvar(EstoqueProduto produto) {
        return repository.save(produto);
    }
}
