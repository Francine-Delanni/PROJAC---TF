package com.af.Dominio.Servicos;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import com.af.Dominio.Dados.ClienteRepository;
import com.af.Dominio.Entidades.Cliente;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager userDetailsManager;

    public ClienteService(ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          JdbcUserDetailsManager userDetailsManager) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    // UC1 — Registrar cliente
    public Cliente registrar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não informado");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (cliente.getSenha() == null || cliente.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);
        Cliente salvo = clienteRepository.save(cliente);

        UserDetails usuario = User.withUsername(cliente.getEmail())
                .password(senhaCriptografada)
                .roles("USER")
                .build();

        if (userDetailsManager.userExists(cliente.getEmail())) {
            userDetailsManager.updateUser(usuario);
        } else {
            userDetailsManager.createUser(usuario);
        }

        return salvo;
    }

    // UC2 — Autenticar cliente
    public Optional<Cliente> autenticar(String email, String senha) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            if (passwordEncoder.matches(senha, cliente.getSenha())) {
                return Optional.of(cliente);
            }
        }

        return Optional.empty();
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
}
