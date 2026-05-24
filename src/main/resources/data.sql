-- Inserção dos clientes
INSERT INTO clientes (cpf, nome, celular, endereco, email, senha) VALUES ('9001', 'Huguinho Pato', '51985744566', 'Rua das Flores, 100', 'huguinho.pato@email.com', '123456');
INSERT INTO clientes (cpf, nome, celular, endereco, email, senha) VALUES ('9002', 'Luizinho Pato', '5199172079', 'Av. Central, 200', 'zezinho.pato@email.com', '123456');

-- Inserção dos ingredientes
INSERT INTO ingredientes (id, descricao) VALUES (1, 'Disco de pizza');
INSERT INTO ingredientes (id, descricao) VALUES (2, 'Porcao de tomate');
INSERT INTO ingredientes (id, descricao) VALUES (3, 'Porcao de mussarela');
INSERT INTO ingredientes (id, descricao) VALUES (4, 'Porcao de presunto');
INSERT INTO ingredientes (id, descricao) VALUES (5, 'Porcao de calabresa');
INSERT INTO ingredientes (id, descricao) VALUES (6, 'Molho de tomate (200ml)');
INSERT INTO ingredientes (id, descricao) VALUES (7, 'Porcao de azeitona');
INSERT INTO ingredientes (id, descricao) VALUES (8, 'Porcao de oregano');
INSERT INTO ingredientes (id, descricao) VALUES (9, 'Porcao de cebola');

-- Inserção dos itens de estoque
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (1, 30, 1);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (2, 30, 2);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (3, 30, 3);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (4, 30, 4);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (5, 30, 5);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (6, 30, 6);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (7, 30, 7);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (8, 30, 8);
INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (9, 30, 9);

-- Inserção das receitas
INSERT INTO receitas (id, titulo) VALUES (1, 'Pizza calabresa');
INSERT INTO receitas (id, titulo) VALUES (2, 'Pizza queijo e presunto');
INSERT INTO receitas (id, titulo) VALUES (3, 'Pizza margherita');

-- Associação dos ingredientes à receita Pizza calabresa
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 5); -- Porcao de calabresa
-- Associação dos ingredientes à receita Pizza queijo e presunto
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 4); -- Porcao de presunto
-- Associação dos ingredientes à receita Pizza margherita
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 8); -- Porcao de cebola

-- insercao dos produtos
INSERT INTO produtos (id,descricao,preco) VALUES (1,'Pizza calabresa',5500);
INSERT INTO produtos (id,descricao,preco) VALUES (2,'Pizza queijo e presunto',6000);
INSERT INTO produtos (id,descricao,preco) VALUES (3,'Pizza margherita',4000);

-- Associação dos produtos com as receitas
INSERT INTO produto_receita (produto_id,receita_id) VALUES(1,1);
INSERT INTO produto_receita (produto_id,receita_id) VALUES(2,2);
INSERT INTO produto_receita (produto_id,receita_id) VALUES(3,3);

-- Insercao dos cardapios
INSERT INTO cardapios (id,titulo) VALUES(1,'Cardapio de Agosto');
INSERT INTO cardapios (id,titulo) VALUES(2,'Cardapio de Setembro');

-- Associação dos cardapios com os produtos
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,1);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,2);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,3);

INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (2,1);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (2,3);


-- Clientes fictícios (se ainda não existirem)
INSERT INTO clientes (cpf, nome, celular, endereco, email, senha)
VALUES
    ('11111111111', 'Maria Oliveira', '11999999999', 'Rua das Flores, 100', 'maria@teste.com', '123'),
    ('22222222222', 'João Souza', '21988888888', 'Av. Paulista, 200', 'joao@teste.com', '123'),
    ('33333333333', 'Ana Lima', '31977777777', 'Rua Central, 50', 'ana@teste.com', '123');

-- Pedidos de Maria (alguns entregues, outros não)
INSERT INTO pedidos (cliente_cpf, data_pedido, data_entrega, status, valor, impostos, desconto, valor_cobrado)
VALUES
    ('11111111111', '2025-10-01 10:00:00', '2025-10-03 14:30:00', 'ENTREGUE', 120.00, 12.00, 0.00, 132.00),
    ('11111111111', '2025-10-05 09:00:00', NULL, 'TRANSPORTE', 89.00, 8.90, 0.00, 97.90),
    ('11111111111', '2025-10-08 12:00:00', '2025-10-09 15:45:00', 'ENTREGUE', 75.00, 7.50, 5.00, 77.50);

-- Pedidos de João
INSERT INTO pedidos (cliente_cpf, data_pedido, data_entrega, status, valor, impostos, desconto, valor_cobrado)
VALUES
    ('22222222222', '2025-09-28 18:20:00', '2025-10-02 11:10:00', 'ENTREGUE', 150.00, 15.00, 10.00, 155.00),
    ('22222222222', '2025-10-10 13:30:00', NULL, 'PENDENTE', 99.00, 9.90, 0.00, 108.90);

-- Pedidos de Ana
INSERT INTO pedidos (cliente_cpf, data_pedido, data_entrega, status, valor, impostos, desconto, valor_cobrado)
VALUES
    ('33333333333', '2025-10-06 10:10:00', '2025-10-07 17:00:00', 'ENTREGUE', 60.00, 6.00, 0.00, 66.00),
    ('33333333333', '2025-10-13 15:45:00', NULL, 'TRANSPORTE', 130.00, 13.00, 0.00, 143.00);


INSERT INTO users (username, password, enabled)
VALUES ('user', '{bcrypt}$2a$10$7Vw0T8Vz4EJZPwV5d5h9FeJ5qsoM8upZ0ED1T9T6f2WzDUNZzmlfe', true);

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');
