package com.af.Adaptadores.Apresentacao;

import com.af.Adaptadores.Apresentacao.Presenters.CabecalhoCardapioPresenter;
import com.af.Adaptadores.Apresentacao.Presenters.CardapioPresenter;
import com.af.Aplicacao.RecuperaListaCardapiosUC;
import com.af.Aplicacao.RecuperarCardapioUC;
import com.af.Aplicacao.Responses.CardapioResponse;
import com.af.Dominio.Entidades.Produto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/cardapio")
@CrossOrigin("*")
public class CardapioController {

    private final RecuperarCardapioUC recuperarCardapioUC;
    private final RecuperaListaCardapiosUC recuperaListaCardapiosUC;

    public CardapioController(RecuperarCardapioUC recuperarCardapioUC,
                              RecuperaListaCardapiosUC recuperaListaCardapiosUC) {
        this.recuperarCardapioUC = recuperarCardapioUC;
        this.recuperaListaCardapiosUC = recuperaListaCardapiosUC;
    }

    @GetMapping({"", "/", "/atual"})
    public ResponseEntity<CardapioPresenter> recuperarCardapioAtual() {
        return recuperarCardapio(1L);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardapioPresenter> recuperarCardapio(@PathVariable long id) {
        CardapioResponse response = recuperarCardapioUC.run(id);
        Set<Long> sugestoes = new HashSet<>(response.getSugestoesDoChef()
                .stream().map(Produto::getId).toList());

        CardapioPresenter presenter = new CardapioPresenter(response.getCardapio().getTitulo());
        for (Produto produto : response.getCardapio().getProdutos()) {
            presenter.insereItem(produto.getId(), produto.getDescricao(),
                    produto.getPreco(), sugestoes.contains(produto.getId()));
        }

        return ResponseEntity.ok(presenter);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<CabecalhoCardapioPresenter>> listarCardapios() {
        return ResponseEntity.ok(
                recuperaListaCardapiosUC.run().cabecalhos().stream()
                        .map(c -> new CabecalhoCardapioPresenter(c.id(), c.titulo()))
                        .toList()
        );
    }
}
