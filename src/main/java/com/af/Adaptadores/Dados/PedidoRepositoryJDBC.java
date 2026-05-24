package com.af.Adaptadores.Dados;

import com.af.Dominio.Dados.PedidoRepository;
import com.af.Dominio.Entidades.Cliente;
import com.af.Dominio.Entidades.Pedido;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepositoryJDBC implements PedidoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido save(Pedido p) {
        String sql = """
            INSERT INTO pedidos (cliente_cpf, data_pedido, status, valor, impostos, desconto, valor_cobrado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getCliente() != null ? p.getCliente().getCpf() : null);
            ps.setObject(2, p.getDataHoraPagamento());
            ps.setString(3, p.getStatus().toString());
            ps.setDouble(4, p.getValor());
            ps.setDouble(5, p.getImpostos());
            ps.setDouble(6, p.getDesconto());
            ps.setDouble(7, p.getValorCobrado());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            p.setId(keyHolder.getKey().longValue());
        }

        return p;
    }

    @Override
    public Optional<Pedido> findById(long id) {
        String sql = "SELECT * FROM pedidos WHERE id = ?";
        List<Pedido> pedidos = jdbcTemplate.query(sql, ps -> ps.setLong(1, id), this::mapRowToPedido);
        return pedidos.stream().findFirst();
    }

    @Override
    public long countByClienteCpfEntre(String cpf, LocalDateTime de, LocalDateTime ate) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_pedido BETWEEN ? AND ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, cpf, de, ate);
        return count != null ? count : 0;
    }

    @Override
    public void updateStatus(long id, Pedido.Status status) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status.toString(), id);
    }

    @Override
    public void updateStatusAndDataEntrega(long id, Pedido.Status status, LocalDateTime dataEntrega) {
        String sql = "UPDATE pedidos SET status = ?, data_entrega = ? WHERE id = ?";
        jdbcTemplate.update(sql, status.toString(), dataEntrega, id);
    }

    @Override
    public void removeById(long id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // UC8 — Listar pedidos entregues entre duas datas
    @Override
    public List<Pedido> pedidosEntreDuasDatas(Pedido.Status status, LocalDate inicio, LocalDate fim) {
        String sql = """
            SELECT * FROM pedidos
            WHERE status = ?
            AND CAST(data_entrega AS DATE) BETWEEN ? AND ?
        """;

        return jdbcTemplate.query(sql, ps -> {
            ps.setString(1, status.toString());
            ps.setObject(2, inicio);
            ps.setObject(3, fim);
        }, this::mapRowToPedido);
    }

    // UC9 — Listar pedidos entregues de um cliente entre duas datas
    @Override
    public List<Pedido> pedidosEntreDuasDatasByCliente(String cpf, Pedido.Status status, LocalDate inicio, LocalDate fim) {
        String sql = """
            SELECT * FROM pedidos
            WHERE cliente_cpf = ?
            AND status = ?
            AND CAST(data_entrega AS DATE) BETWEEN ? AND ?
        """;

        return jdbcTemplate.query(sql, ps -> {
            ps.setString(1, cpf);
            ps.setString(2, status.toString());
            ps.setObject(3, inicio);
            ps.setObject(4, fim);
        }, this::mapRowToPedido);
    }

    private Pedido mapRowToPedido(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente(
            rs.getString("cliente_cpf"),
            null,
            null,
            null,
            null,
            null
        );

        return new Pedido(
                rs.getLong("id"),
                cliente,
                rs.getTimestamp("data_pedido").toLocalDateTime(),
                Collections.emptyList(),
                Pedido.Status.valueOf(rs.getString("status")),
                rs.getBigDecimal("valor") != null ? rs.getBigDecimal("valor").doubleValue() : 0.0,
                rs.getBigDecimal("impostos") != null ? rs.getBigDecimal("impostos").doubleValue() : 0.0,
                rs.getBigDecimal("desconto") != null ? rs.getBigDecimal("desconto").doubleValue() : 0.0,
                rs.getBigDecimal("valor_cobrado") != null ? rs.getBigDecimal("valor_cobrado").doubleValue() : 0.0
        );
    }
}
