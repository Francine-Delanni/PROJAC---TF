# Pizzaria AF - Parte 2

Projeto de pizzaria em arquitetura de microservicos com Spring Boot, Eureka, Gateway, RabbitMQ e Docker Compose.

## Arquitetura

Servicos principais:

- `eureka-server`: service discovery. Os demais servicos registram suas instancias nele.
- `gateway`: ponto de entrada HTTP da aplicacao. Centraliza a autenticacao HTTP Basic.
- `pizzaria-service`: regras principais da pizzaria, cardapio, clientes, pedidos, pagamento, impostos, descontos, integracao com estoque e publicacao de mensagens de entrega.
- `estoque-service`: microsservico de estoque com banco proprio H2/JPA.
- `entregas-service`: consumidor RabbitMQ responsavel por processar pedidos pagos. Pode subir com multiplas instancias.
- `rabbitmq`: broker de mensagens usado entre `pizzaria-service` e `entregas-service`.

Fluxo principal:

1. O cliente acessa a aplicacao pelo `gateway`.
2. O `gateway` autentica rotas protegidas e roteia para `pizzaria-service` ou `estoque-service`.
3. Ao submeter pedido, o `pizzaria-service` consulta o `estoque-service` pelo Eureka/load balancing.
4. Ao pagar pedido, o `pizzaria-service` publica mensagem na fila RabbitMQ.
5. Uma das instancias do `entregas-service` consome a mensagem.

## Subir o ambiente

Na raiz do projeto:

```powershell
docker compose down -v
docker compose up --build -d --scale entregas-service=3
```

Conferir containers:

```powershell
docker compose ps
```

## Credenciais

Gateway HTTP Basic:

- Usuario: `admin`
- Senha: `admin123`

RabbitMQ Management:

- Usuario: `guest`
- Senha: `guest`

## URLs

- Eureka: `http://localhost:8761`
- Gateway: `http://localhost:8080`
- RabbitMQ Management: `http://localhost:15672`
- Pizzaria direto: `http://localhost:8081`
- Estoque direto: `http://localhost:8082`

Use preferencialmente o Gateway para testar a aplicacao.

## Endpoints principais via Gateway

### Cardapio

```http
GET /pizzaria/api/cardapio
```

Exemplo:

```powershell
curl.exe -u admin:admin123 http://localhost:8080/pizzaria/api/cardapio
```

### Cadastro de cliente

Rota publica, sem autenticacao:

```http
POST /pizzaria/public/clientes/registrar
```

Exemplo:

```powershell
curl.exe -H "Content-Type: application/json" `
  -X POST http://localhost:8080/pizzaria/public/clientes/registrar `
  -d "{\"cpf\":\"99900000001\",\"nome\":\"Cliente Teste\",\"celular\":\"51999999999\",\"endereco\":\"Rua Teste, 100\",\"email\":\"cliente.teste@af.local\",\"senha\":\"123456\"}"
```

### Criar pedido

Rota protegida pelo Gateway:

```http
POST /pizzaria/api/pedidos/submeter
```

Exemplo:

```powershell
curl.exe -u admin:admin123 -H "Content-Type: application/json" `
  -X POST http://localhost:8080/pizzaria/api/pedidos/submeter `
  -d "{\"cpf\":\"99900000001\",\"nome\":\"Cliente Teste\",\"celular\":\"51999999999\",\"endereco\":\"Rua Teste, 100\",\"email\":\"cliente.teste@af.local\",\"itens\":[{\"produtoId\":1,\"quantidade\":1}]}"
```

### Pagar pedido

Rota protegida pelo Gateway:

```http
POST /pizzaria/api/pedidos/pagamento/{id}?cpf={cpf}
```

Exemplo:

```powershell
curl.exe -u admin:admin123 -X POST "http://localhost:8080/pizzaria/api/pedidos/pagamento/1?cpf=99900000001"
```

### Estoque

Rota protegida pelo Gateway:

```http
GET /estoque/api/estoque
GET /estoque/api/estoque/{produtoId}/disponivel?quantidade={quantidade}
```

Exemplos:

```powershell
curl.exe -u admin:admin123 http://localhost:8080/estoque/api/estoque
curl.exe -u admin:admin123 "http://localhost:8080/estoque/api/estoque/1/disponivel?quantidade=1"
```

### Descontos

Listar descontos:

```http
GET /pizzaria/api/precificacao/descontos
```

Trocar desconto ativo:

```http
PUT /pizzaria/api/precificacao/descontos/ativo?codigo={codigo}
```

Codigos esperados:

- `FIDELIDADE`
- `SEM_DESCONTO`
- `NENHUM`
- `PERCENTUAL_5`
- `VALOR_MINIMO`

Exemplos:

```powershell
curl.exe -u admin:admin123 http://localhost:8080/pizzaria/api/precificacao/descontos
curl.exe -u admin:admin123 -X PUT "http://localhost:8080/pizzaria/api/precificacao/descontos/ativo?codigo=PERCENTUAL_5"
```

## Imposto por env/config

A politica de imposto ativa e definida na inicializacao do `pizzaria-service`.

Opcoes:

- `IMPOSTO_TIPO=PADRAO`
- `IMPOSTO_TIPO=REDUZIDO`
- `app.imposto.codigo=LEI_10`
- `app.imposto.codigo=LEI_5`

Exemplo local:

```powershell
$env:IMPOSTO_TIPO="REDUZIDO"
mvn spring-boot:run
```

No Docker Compose atual, o valor configurado e:

```yaml
IMPOSTO_CODIGO=LEI_10
```

## Roteiro de teste final

1. Conferir containers:

```powershell
docker compose ps
```

2. Verificar Eureka:

```powershell
curl.exe http://localhost:8761
```

3. Rota protegida sem autenticacao deve retornar `401`:

```powershell
curl.exe -i http://localhost:8080/pizzaria/api/cardapio
```

4. Rota protegida com autenticacao deve retornar `200`:

```powershell
curl.exe -i -u admin:admin123 http://localhost:8080/pizzaria/api/cardapio
```

5. Cadastrar cliente:

```powershell
curl.exe -H "Content-Type: application/json" `
  -X POST http://localhost:8080/pizzaria/public/clientes/registrar `
  -d "{\"cpf\":\"99900000001\",\"nome\":\"Cliente Teste\",\"celular\":\"51999999999\",\"endereco\":\"Rua Teste, 100\",\"email\":\"cliente.teste@af.local\",\"senha\":\"123456\"}"
```

6. Consultar estoque antes:

```powershell
curl.exe -u admin:admin123 http://localhost:8080/estoque/api/estoque
```

7. Criar pedido:

```powershell
curl.exe -u admin:admin123 -H "Content-Type: application/json" `
  -X POST http://localhost:8080/pizzaria/api/pedidos/submeter `
  -d "{\"cpf\":\"99900000001\",\"nome\":\"Cliente Teste\",\"celular\":\"51999999999\",\"endereco\":\"Rua Teste, 100\",\"email\":\"cliente.teste@af.local\",\"itens\":[{\"produtoId\":1,\"quantidade\":1}]}"
```

8. Consultar estoque depois:

```powershell
curl.exe -u admin:admin123 http://localhost:8080/estoque/api/estoque
```

9. Pedido sem estoque deve falhar:

```powershell
curl.exe -i -u admin:admin123 -H "Content-Type: application/json" `
  -X POST http://localhost:8080/pizzaria/api/pedidos/submeter `
  -d "{\"cpf\":\"99900000001\",\"nome\":\"Cliente Teste\",\"celular\":\"51999999999\",\"endereco\":\"Rua Teste, 100\",\"email\":\"cliente.teste@af.local\",\"itens\":[{\"produtoId\":1,\"quantidade\":9999}]}"
```

10. Pagar pedido:

```powershell
curl.exe -u admin:admin123 -X POST "http://localhost:8080/pizzaria/api/pedidos/pagamento/{id}?cpf=99900000001"
```

11. Verificar logs do `entregas-service` consumindo RabbitMQ:

```powershell
docker compose logs --tail=200 entregas-service
```

Procure mensagens como:

- `Instancia de entrega recebeu pedido`
- `Pedido entregue pela instancia atual`

## Script de teste final

Foi criado um script simples para automatizar o roteiro:

```powershell
.\scripts\teste-final.ps1
```

Ele assume que o ambiente ja foi iniciado com:

```powershell
docker compose up --build -d --scale entregas-service=3
```
