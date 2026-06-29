package com.af.Adaptadores.Apresentacao;

import com.af.Aplicacao.SubmeterPedidoUC;
import com.af.Aplicacao.CancelarPedidoUC;
import com.af.Aplicacao.PagamentoPedidoUC;
import com.af.Aplicacao.SolicitarStatusUC;
import com.af.Aplicacao.ListarEntreguesUC;
import com.af.Aplicacao.ListarEntregaClienteUC;

import com.af.Aplicacao.Requests.SubmeterPedidoRequest;
import com.af.Aplicacao.Responses.PedidoResponse;
import com.af.Dominio.Entidades.Pedido;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin("*")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final CancelarPedidoUC cancelarPedidoUC;
    private final PagamentoPedidoUC pagamentoPedidoUC;
    private final SolicitarStatusUC solicitarStatusUC;
    private final ListarEntreguesUC listarEntreguesUC;
    private final ListarEntregaClienteUC listarEntregaClienteUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC,
                            CancelarPedidoUC cancelarPedidoUC,
                            PagamentoPedidoUC pagamentoPedidoUC,
                            SolicitarStatusUC solicitarStatusUC,
                            ListarEntreguesUC listarEntreguesUC,
                            ListarEntregaClienteUC listarEntregaClienteUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.pagamentoPedidoUC = pagamentoPedidoUC;
        this.solicitarStatusUC = solicitarStatusUC;
        this.listarEntreguesUC = listarEntreguesUC;
        this.listarEntregaClienteUC = listarEntregaClienteUC;
    }

    @PostMapping("/submeter")
    public ResponseEntity<PedidoResponse> submeter(@RequestBody SubmeterPedidoRequest request,
                                                   Authentication auth) {
        return ResponseEntity.ok(submeterPedidoUC.run(request));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Pedido.Status> statusPedido(@PathVariable Long id,
                                                      @RequestParam String cpf) {
        return ResponseEntity.ok(solicitarStatusUC.run(id, cpf));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(cancelarPedidoUC.run(id));
    }

    @PostMapping("/pagamento/{id}")
    public ResponseEntity<PedidoResponse> pagamento(@PathVariable Long id,
                                                    @RequestParam String cpf) {
        return ResponseEntity.ok(pagamentoPedidoUC.run(id, cpf));
    }

    @GetMapping("/entregues")
    public ResponseEntity<List<Pedido>> listarPedidosEntregues(@RequestParam LocalDate inicio,
                                                               @RequestParam LocalDate fim) {
        return ResponseEntity.ok(listarEntreguesUC.listarPedidosEntregues(inicio, fim));
    }

    @GetMapping("/entregues/cliente/{cpf}")
    public ResponseEntity<List<Pedido>> listarPedidosEntreguesPorCliente(@PathVariable String cpf,
                                                                         @RequestParam LocalDate inicio,
                                                                         @RequestParam LocalDate fim) {
        return ResponseEntity.ok(listarEntregaClienteUC.listarPedidosEntregues(cpf, inicio, fim));
    }
}
