package com.af.Adaptadores.Apresentacao;

import com.af.Aplicacao.RegistrarClienteUC;
import com.af.Dominio.Entidades.Cliente;
import com.af.Dominio.Servicos.ClienteService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/clientes")
@CrossOrigin("*")
public class ClienteController {

    private final RegistrarClienteUC registrarClienteUC;
    private final ClienteService clienteService;

    public ClienteController(RegistrarClienteUC registrarClienteUC, ClienteService clienteService) {
        this.registrarClienteUC = registrarClienteUC;
        this.clienteService = clienteService;
    }

    // UC1 — Registrar cliente (livre)
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Cliente cliente) {
        try {
            Cliente novo = registrarClienteUC.registrar(cliente);
            return ResponseEntity.ok(Map.of(
                "cpf", novo.getCpf(),
                "nome", novo.getNome(),
                "celular", novo.getCelular(),
                "endereco", novo.getEndereco(),
                "email", novo.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar cliente: " + e.getMessage());
        }
    }

    // UC2 — Autenticar cliente (livre)
    // Observação: depois do login, os endpoints /api/** podem ser acessados com HTTP Basic usando email e senha.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return clienteService.autenticar(request.email(), request.senha())
                .<ResponseEntity<?>>map(cliente -> ResponseEntity.ok(Map.of(
                        "autenticado", true,
                        "cpf", cliente.getCpf(),
                        "nome", cliente.getNome(),
                        "email", cliente.getEmail(),
                        "mensagem", "Use HTTP Basic com este email e senha para acessar os endpoints /api/**"
                )))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of(
                        "autenticado", false,
                        "mensagem", "Email ou senha inválidos"
                )));
    }

    // Endpoint auxiliar protegido para conferir o usuário autenticado via HTTP Basic.
    @GetMapping("/me")
    public ResponseEntity<?> usuarioAutenticado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        return ResponseEntity.ok(Map.of("usuario", authentication.getName()));
    }

    public record LoginRequest(String email, String senha) {}
}
