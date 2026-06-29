# Como testar

1) Rode:

```powershell
mvn spring-boot:run
```

2) Abra um novo terminal na raiz do projeto enquanto o backend roda

3) Cole os seguintes comandos para testar as rotas

---

# Cardápio autenticado

```powershell
curl.exe -u admin:admin123 http://localhost:8080/api/cardapio
```

ou

```powershell
curl.exe -u admin:admin123 http://localhost:8080/api/cardapio/1
```

---

# Cadastro de cliente

```powershell
curl.exe -X POST http://localhost:8080/public/clientes/registrar `
-H "Content-Type: application/json" `
-d "{\"cpf\":\"44444444444\",\"nome\":\"Alice Teste\",\"celular\":\"51999999999\",\"endereco\":\"Rua Teste, 123\",\"email\":\"alice@teste.com\",\"senha\":\"123456\"}"
```

OU

```powershell
Invoke-RestMethod -Method POST `
-Uri "http://localhost:8080/public/clientes/registrar" `
-ContentType "application/json" `
-Body '{"cpf":"11111111111","nome":"Joao","celular":"51999999999","endereco":"Rua A","email":"joao@email.com","senha":"123456"}'
```

---

# Login

```powershell
curl.exe -X POST http://localhost:8080/public/clientes/login `
-H "Content-Type: application/json" `
-d "{\"email\":\"alice@teste.com\",\"senha\":\"123456\"}"
```

OU

```powershell
Invoke-RestMethod -Method POST `
-Uri "http://localhost:8080/public/clientes/login" `
-ContentType "application/json" `
-Body '{"email":"joao@email.com","senha":"123456"}'
```

---

# Testar autenticação de admin

```powershell
curl.exe -u admin:admin123 http://localhost:8080/api/cardapio
```

---

# Testar autenticação de cliente

```powershell
curl.exe -u joao@email.com:123456 http://localhost:8080/api/cardapio
```

---

# Testar acesso sem login (não autorizado)

```powershell
curl.exe http://localhost:8080/api/cardapio
```

---

# Consultar cardápio

```powershell
curl.exe -u admin:admin123 http://localhost:8080/api/cardapio
```

---

# Consultar item específico do cardápio

```powershell
curl.exe -u admin:admin123 http://localhost:8080/api/cardapio/1
```

---

# Submeter pedido

```powershell
curl.exe -u joao@email.com:123456 -X POST "http://localhost:8080/api/pedidos/submeter" `
-H "Content-Type: application/json" `
-d "{\"cpf\":\"11111111111\",\"nome\":\"Joao\",\"celular\":\"51999999999\",\"endereco\":\"Rua A\",\"email\":\"joao@email.com\",\"itens\":[{\"produtoId\":1,\"quantidade\":1}]}"
```

---

# Consultar status do pedido

```powershell
curl.exe -u admin:admin123 "http://localhost:8080/api/pedidos/status/1?cpf=11111111111"
```

---

# Cancelar pedido

> Pedido não pode ser cancelado caso já esteja entregue.

```powershell
curl.exe -u admin:admin123 -X DELETE "http://localhost:8080/api/pedidos/cancel/1?cpf=11111111111"
```

---

# Listar pedidos do cliente

```powershell
curl.exe -u admin:admin123 "http://localhost:8080/api/pedidos/cliente/11111111111"
```

---

# Testes de erro

## Produto inexistente

```powershell
curl.exe -u joao@email.com:123456 -X POST "http://localhost:8080/api/pedidos/submeter" `
-H "Content-Type: application/json" `
-d "{\"cpf\":\"11111111111\",\"nome\":\"Joao\",\"celular\":\"51999999999\",\"endereco\":\"Rua A\",\"email\":\"joao@email.com\",\"itens\":[{\"produtoId\":999,\"quantidade\":1}]}"
```

---

## Quantidade maior que estoque

```powershell
curl.exe -u joao@email.com:123456 -X POST "http://localhost:8080/api/pedidos/submeter" `
-H "Content-Type: application/json" `
-d "{\"cpf\":\"11111111111\",\"nome\":\"Joao\",\"celular\":\"51999999999\",\"endereco\":\"Rua A\",\"email\":\"joao@email.com\",\"itens\":[{\"produtoId\":1,\"quantidade\":999}]}"
```

---

## Pedido inexistente

```powershell
curl.exe -u admin:admin123 "http://localhost:8080/api/pedidos/status/999?cpf=11111111111"
```

---

## CPF inválido na consulta

```powershell
curl.exe -u admin:admin123 "http://localhost:8080/api/pedidos/status/1?cpf=00000000000"
```

---

# Remover cliente

```powershell
curl.exe -u admin:admin123 -X DELETE "http://localhost:8080/api/clientes/11111111111"
```

---

# Atualizar cliente

```powershell
curl.exe -u admin:admin123 -X PUT "http://localhost:8080/api/clientes/11111111111" `
-H "Content-Type: application/json" `
-d "{\"cpf\":\"11111111111\",\"nome\":\"Joao Silva\",\"celular\":\"51888888888\",\"endereco\":\"Rua Nova\",\"email\":\"joao@email.com\",\"senha\":\"123456\"}"
```