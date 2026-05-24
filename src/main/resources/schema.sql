create table if not exists clientes(
                                       cpf varchar(15) not null primary key,
    nome varchar(100) not null,
    celular varchar(20) not null,
    endereco varchar(255) not null,
    email varchar(255) not null,
    senha varchar(255) not null
    );

create table if not exists ingredientes (
                                            id bigint primary key,
                                            descricao varchar(255) not null
    );

create table if not exists itensEstoque(
                                           id bigint primary key,
                                           quantidade int,
                                           ingrediente_id bigint,
                                           foreign key (ingrediente_id) references ingredientes(id)
    );

-- Tabela Receita
create table if not exists receitas (
                                        id bigint primary key,
                                        titulo varchar(255) not null
    );

-- Tabela de relacionamento entre Receita e Ingrediente
create table if not exists receita_ingrediente (
                                                   receita_id bigint not null,
                                                   ingrediente_id bigint not null,
                                                   primary key (receita_id, ingrediente_id),
    foreign key (receita_id) references receitas(id),
    foreign key (ingrediente_id) references ingredientes(id)
    );

-- Tabela de Produtos
create table if not exists produtos (
                                        id bigint primary key,
                                        descricao varchar(255) not null,
    preco bigint
    );

-- Tabela de relacionamento entre Produto e Receita
create table if not exists produto_receita (
                                               produto_id bigint not null,
                                               receita_id bigint not null,
                                               primary key (produto_id,receita_id),
    foreign key (produto_id) references produtos(id),
    foreign key (receita_id) references receitas(id)
    );

-- Tabela de Cardapios
create table if not exists cardapios (
                                         id bigint primary key,
                                         titulo varchar(255) not null
    );

-- Tabela de relacionamento entre Cardapio e Produto
create table if not exists cardapio_produto (
                                                cardapio_id bigint not null,
                                                produto_id bigint not null,
                                                primary key (cardapio_id,produto_id),
    foreign key (cardapio_id) references cardapios(id),
    foreign key (produto_id) references produtos(id)
    );

CREATE TABLE IF NOT EXISTS pedidos (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       cliente_cpf VARCHAR(15) NOT NULL,
    FOREIGN KEY (cliente_cpf) REFERENCES clientes(cpf),
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_entrega TIMESTAMP NULL,
    status VARCHAR(50) NOT NULL,
    valor DECIMAL(10,2) DEFAULT 0.00,
    impostos DECIMAL(10,2) DEFAULT 0.00,
    desconto DECIMAL(10,2) DEFAULT 0.00,
    valor_cobrado DECIMAL(10,2) DEFAULT 0.00
    );

CREATE TABLE users (
                       username VARCHAR(50) NOT NULL PRIMARY KEY,
                       password VARCHAR(100) NOT NULL,
                       enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             constraint fk_authorities_users FOREIGN KEY (username) REFERENCES users(username)
);

create unique index ix_auth_username on authorities (username, authority);