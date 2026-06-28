package com.af.Adaptadores.Dados;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.af.Dominio.Dados.ClienteRepository;
import com.af.Dominio.Entidades.Cliente;

@Component
public class ClienteRepositoryJDBC implements ClienteRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClienteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Cliente save(Cliente cliente) {
        Optional<Cliente> existente = findByCpf(cliente.getCpf());

        if (existente.isPresent()) {
            String sql = "UPDATE clientes SET nome = ?, celular = ?, endereco = ?, email = ?, senha = ? WHERE cpf = ?";
            jdbcTemplate.update(sql,
                    cliente.getNome(),
                    cliente.getCelular(),
                    cliente.getEndereco(),
                    cliente.getEmail(),
                    cliente.getSenha(),
                    cliente.getCpf());
        } else {
            String sql = "INSERT INTO clientes (cpf, nome, celular, endereco, email, senha) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    cliente.getCpf(),
                    cliente.getNome(),
                    cliente.getCelular(),
                    cliente.getEndereco(),
                    cliente.getEmail(),
                    cliente.getSenha());
        }

        return cliente;
    }

    @Override
    public Optional<Cliente> findByEmail(String email) {
        String sql = "SELECT cpf, nome, celular, endereco, email, senha FROM clientes WHERE email = ?";
        List<Cliente> clientes = jdbcTemplate.query(
                sql,
                ps -> ps.setString(1, email),
                (rs, rowNum) -> new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("celular"),
                        rs.getString("endereco"),
                        rs.getString("email"),
                        rs.getString("senha")
                )
        );

        return clientes.stream().findFirst();
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        String sql = "SELECT cpf, nome, celular, endereco, email, senha FROM clientes WHERE cpf = ?";
        List<Cliente> clientes = jdbcTemplate.query(
                sql,
                ps -> ps.setString(1, cpf),
                (rs, rowNum) -> new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("celular"),
                        rs.getString("endereco"),
                        rs.getString("email"),
                        rs.getString("senha")
                )
        );

        return clientes.stream().findFirst();
    }
}
