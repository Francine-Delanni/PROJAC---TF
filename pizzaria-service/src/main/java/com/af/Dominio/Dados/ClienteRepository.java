package com.af.Dominio.Dados;

import java.util.Optional;
import com.af.Dominio.Entidades.Cliente;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
}
