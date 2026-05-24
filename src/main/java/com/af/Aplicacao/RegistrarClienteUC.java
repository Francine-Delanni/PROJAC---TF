package com.af.Aplicacao;

import com.af.Dominio.Entidades.Cliente;
import com.af.Dominio.Servicos.ClienteService;
import org.springframework.stereotype.Service;

@Service
public class RegistrarClienteUC {

    private final ClienteService clienteService;

    public RegistrarClienteUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public Cliente registrar(Cliente cliente) {
        return clienteService.registrar(cliente);
    }
}
