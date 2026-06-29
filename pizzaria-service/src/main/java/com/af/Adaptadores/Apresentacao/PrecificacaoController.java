package com.af.Adaptadores.Apresentacao;

import com.af.Dominio.Servicos.Precificacao.ServicoDeDescontos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/precificacao")
public class PrecificacaoController {

    private final ServicoDeDescontos servicoDeDescontos;

    public PrecificacaoController(ServicoDeDescontos servicoDeDescontos) {
        this.servicoDeDescontos = servicoDeDescontos;
    }

    @GetMapping("/descontos")
    public ResponseEntity<?> listarDescontos() {
        return ResponseEntity.ok(Map.of(
                "ativo", servicoDeDescontos.codigoAtivo(),
                "disponiveis", servicoDeDescontos.listar()
        ));
    }

    @PutMapping("/descontos/ativo")
    public ResponseEntity<?> selecionarDesconto(@RequestParam String codigo) {
        return ResponseEntity.ok(Map.of(
                "ativo", servicoDeDescontos.selecionar(codigo),
                "disponiveis", servicoDeDescontos.listar()
        ));
    }
}
