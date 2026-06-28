package com.af.Dominio.Servicos;

import com.af.Aplicacao.Requests.SubmeterPedidoRequest;
import com.af.Aplicacao.Responses.PedidoResponse;
import com.af.Dominio.Dados.PedidoRepository;
import com.af.Dominio.Dados.ProdutosRepository;
import com.af.Dominio.Entidades.Cliente;
import com.af.Dominio.Entidades.Pedido;
import com.af.Dominio.Entidades.Produto;
import com.af.Dominio.Entidades.ItemPedido;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.af.Dominio.Servicos.Precificacao.ServicoDeDescontos;
import com.af.Dominio.Servicos.Precificacao.ServicoDeImpostos;

@Service
public class PedidoService {
    private final ProdutosRepository produtoRepo;
    private final PedidoRepository pedidoRepo;
    private final ServicoDeImpostos servicoDeImpostos;
    private final ServicoDeDescontos servicoDeDescontos;
    private final EstoqueService estoqueService;

    public PedidoService(ProdutosRepository produtoRepo,
            PedidoRepository pedidoRepo,
            ServicoDeImpostos servicoDeImpostos,
            ServicoDeDescontos servicoDeDescontos,
            EstoqueService estoqueService) {
        this.produtoRepo = produtoRepo;
        this.pedidoRepo = pedidoRepo;
        this.servicoDeDescontos = servicoDeDescontos;
        this.servicoDeImpostos = servicoDeImpostos;
        this.estoqueService = estoqueService;
    }

    // UC4: Submeter pedido para aprovação
    public PedidoResponse submeter(SubmeterPedidoRequest req) {
        if (req == null || req.getItens() == null || req.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve possuir ao menos um item");
        }

        var cliente = new Cliente(
                req.getCpf(),
                req.getNome(),
                req.getCelular(),
                req.getEndereco(),
                req.getEmail(),
                "");

        var itens = new ArrayList<ItemPedido>();
        double soma = 0.0;

        for (SubmeterPedidoRequest.Item it : req.getItens()) {
            if (it.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }

            Produto produto = produtoRepo.recuperaProdutoPorid(it.getProdutoId());
            if (produto == null) {
                throw new IllegalArgumentException("Produto inválido: " + it.getProdutoId());
            }

            boolean disponivel = estoqueService.verificarDisponibilidade(produto.getId(), it.getQuantidade());
            if (!disponivel) {
                throw new IllegalStateException(
                        "Pedido rejeitado: falta de estoque para o produto " + produto.getDescricao());
            }

            double precoUnit = produto.getPreco() / 100.0;
            soma += precoUnit * it.getQuantidade();
            itens.add(new ItemPedido(produto, it.getQuantidade()));
        }

        double desconto = servicoDeDescontos.calcular(req.getCpf(), soma);
        double impostos = servicoDeImpostos.calcular(soma - desconto);
        double total = soma - desconto + impostos;

        soma = round2(soma);
        desconto = round2(desconto);
        impostos = round2(impostos);
        total = round2(total);

        var pedido = new Pedido(
                0L,
                cliente,
                LocalDateTime.now(),
                itens,
                Pedido.Status.APROVADO,
                soma, impostos, desconto, total);

        var salvo = pedidoRepo.save(pedido);
        return montarResponse(salvo);
    }

    // UC5: Consultar status do pedido
    public Pedido.Status status(Long pedidoId, String cpf) {
        var pedido = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        if (pedido.getCliente() == null || !cpf.equals(pedido.getCliente().getCpf())) {
            throw new SecurityException("Pedido não pertence ao cliente");
        }
        return pedido.getStatus();
    }

    // UC6: Cancelar pedido
    public PedidoResponse cancelar(Long pedidoId) {
        var pedido = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalStateException("Pedido não pode ser cancelado no status atual");
        }

        pedidoRepo.updateStatus(pedidoId, Pedido.Status.CANCELADO);
        var atualizado = pedidoRepo.findById(pedidoId).orElse(pedido);
        return montarResponse(atualizado);
    }

    // UC7 : Pagar pedido
    public PedidoResponse pagamento(Long pedidoId, String cpf) {
        var pedido = pedidoRepo.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        if (pedido.getCliente() == null || !cpf.equals(pedido.getCliente().getCpf())) {
            throw new SecurityException("Pedido não pertence ao cliente");
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalStateException("Somente pedidos APROVADOS podem ser pagos");
        }

        Pedido.Status[] fluxo = {
                Pedido.Status.PAGO,
                Pedido.Status.AGUARDANDO,
                Pedido.Status.PREPARACAO,
                Pedido.Status.PRONTO,
                Pedido.Status.TRANSPORTE,
                Pedido.Status.ENTREGUE
        };

        for (Pedido.Status fase : fluxo) {
            if (fase == Pedido.Status.ENTREGUE) {
                pedidoRepo.updateStatusAndDataEntrega(pedidoId, fase, LocalDateTime.now());
            } else {
                pedidoRepo.updateStatus(pedidoId, fase);
            }
        }

        var atualizado = pedidoRepo.findById(pedidoId).orElse(pedido);
        return montarResponse(atualizado);
    }

    private PedidoResponse montarResponse(Pedido pedido) {
        var resp = new PedidoResponse();
        resp.setId(pedido.getId());
        resp.setStatus(pedido.getStatus());
        resp.setSomaItens(toMoney(pedido.getValor()));
        resp.setDesconto(toMoney(pedido.getDesconto()));
        resp.setImpostos(toMoney(pedido.getImpostos()));
        resp.setTotal(toMoney(pedido.getValorCobrado()));

        List<ItemPedido> itens = pedido.getItens() != null ? pedido.getItens() : Collections.emptyList();
        resp.setItens(itens.stream().map(i -> {
            var ri = new PedidoResponse.Item();
            var prod = i.getItem();
            ri.setProdutoId(prod.getId());
            ri.setDescricao(prod.getDescricao());
            ri.setQuantidade(i.getQuantidade());
            ri.setPrecoUnitario(toMoney(prod.getPreco() / 100.0));
            return ri;
        }).toList());
        resp.setHistoricoStatus(pedido.getHistoricoStatus());
        return resp;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private static BigDecimal toMoney(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);
    }
}
