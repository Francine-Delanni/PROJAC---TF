# Pizzaria – Backend do Sistema de Pedidos

![Java](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen?logo=spring&logoColor=white)
![Vaadin](https://img.shields.io/badge/Vaadin-24-purple?logo=vaadin&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Project-red?logo=apachemaven&logoColor=white)

---

**Disciplina:** Projeto e Arquitetura de Software  
**Professor:** Bernardo Copstein  
**Integrantes:**
- Alice
- Francine

---

## Descrição

O projeto implementa o backend de um sistema de gestão de pedidos de uma pizzaria online. A aplicação permite cadastro e autenticação de clientes, consulta ao cardápio, submissão de pedidos, cálculo de valores com impostos e descontos, acompanhamento de status, cancelamento, pagamento e listagem de pedidos entregues.

A estrutura segue a proposta de **arquitetura limpa**, separando apresentação, aplicação, domínio e adaptadores de dados.

---

## Domínio Java do projeto

O domínio/pacote principal do projeto foi ajustado para:

```txt
com.af
```

Esse nome representa as integrantes **Alice** e **Francine**.

---

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.2
- Vaadin 24
- Maven
- H2 Database
- Spring Security

---

## Como rodar o projeto

### Pré-requisitos

- Java 21 ou superior
- Maven

### Execução

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação sobe, por padrão, em:

```txt
http://localhost:8080
```

Console H2:

```txt
http://localhost:8080/h2-console
```

Configuração do banco H2:

```txt
JDBC URL: jdbc:h2:file:./data/pizzaria_db
User: sa
Password: vazio
```

---

## Casos de uso implementados

| UC | Descrição |
|---|---|
| UC1 | Registrar cliente no sistema |
| UC2 | Autenticar cliente no sistema |
| UC3 | Carregar cardápio autenticado |
| UC4 | Submeter pedido para aprovação |
| UC5 | Solicitar status de pedido |
| UC6 | Cancelar pedido aprovado e ainda não pago |
| UC7 | Pagar pedido e encaminhar para cozinha/entrega |
| UC8 | Listar pedidos entregues entre duas datas |
| UC9 | Listar pedidos de um cliente entregues entre duas datas |

---

## Cronograma do enunciado

| Data | Casos de uso |
|---|---|
| 11/05/2026 | UC3 e UC4 |
| 13/05/2026 | UC5 e UC6 |
| 18/05/2026 | UC1, UC2 e UC7 |
| 20/05/2026 | Entrega final com UC8 e UC9 |

---

## Endpoints principais para teste

### Registrar cliente

```txt
POST /public/clientes/registrar
```

### Login

```txt
POST /public/clientes/login
```

### Carregar cardápio

```txt
GET /api/cardapio/1
```

### Submeter pedido

```txt
POST /api/pedidos/submeter
```

### Consultar status

```txt
GET /api/pedidos/status/{id}?cpf={cpf}
```

### Cancelar pedido

```txt
DELETE /api/pedidos/cancel/{id}
```

### Pagar pedido

```txt
POST /api/pedidos/pagamento/{id}?cpf={cpf}
```

### Listar pedidos entregues

```txt
GET /api/pedidos/entregues?inicio=2026-05-01&fim=2026-05-31
```

### Listar pedidos entregues de um cliente

```txt
GET /api/pedidos/entregues/cliente/{cpf}?inicio=2026-05-01&fim=2026-05-31
```

---

## Observação sobre autenticação

Os endpoints `/api/**` são protegidos por Spring Security. Para testar manualmente, use HTTP Basic Auth com o email e a senha cadastrados para o cliente.
