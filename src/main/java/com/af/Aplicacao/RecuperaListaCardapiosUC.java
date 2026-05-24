package com.af.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.af.Aplicacao.Responses.CabecalhoCardapioResponse;
import com.af.Dominio.Servicos.CardapioService;

@Component
public class RecuperaListaCardapiosUC {
    private CardapioService cardapioService;

    @Autowired
    public RecuperaListaCardapiosUC(CardapioService cardapioService){
        this.cardapioService = cardapioService;
    }

    public CabecalhoCardapioResponse run(){
        return new CabecalhoCardapioResponse(cardapioService.recuperaListaDeCardapios());
    }    
}

