package com.af.estoque.controller;

import com.af.estoque.dominio.EstoqueProduto;
import com.af.estoque.servico.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @GetMapping
    public List<EstoqueProduto> listar() {
        return service.listar();
    }

    @GetMapping("/{produtoId}/disponivel")
    public ResponseEntity<Boolean> disponivel(@PathVariable Long produtoId,
                                              @RequestParam int quantidade) {
        return ResponseEntity.ok(service.disponivel(produtoId, quantidade));
    }

    @PostMapping("/{produtoId}/baixar")
    public ResponseEntity<Void> baixar(@PathVariable Long produtoId,
                                       @RequestParam int quantidade) {
        service.baixar(produtoId, quantidade);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public EstoqueProduto salvar(@RequestBody EstoqueProduto produto) {
        return service.salvar(produto);
    }
}
