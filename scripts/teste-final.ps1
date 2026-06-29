param(
    [string]$GatewayUrl = "http://localhost:8080",
    [string]$EurekaUrl = "http://localhost:8761",
    [string]$GatewayUser = "admin",
    [string]$GatewayPassword = "admin123",
    [string]$Cpf = "99900000001",
    [string]$Email = "cliente.teste@af.local"
)

$ErrorActionPreference = "Stop"

function New-BasicAuthHeader {
    param(
        [string]$User,
        [string]$Password
    )

    $pair = "${User}:${Password}"
    $bytes = [System.Text.Encoding]::ASCII.GetBytes($pair)
    return @{ Authorization = "Basic " + [Convert]::ToBase64String($bytes) }
}

function ConvertTo-JsonBody {
    param([object]$Object)

    return $Object | ConvertTo-Json -Depth 10 -Compress
}

function Get-ResponseBody {
    param($Response)

    if ($null -eq $Response) {
        return ""
    }

    try {
        $stream = $Response.GetResponseStream()
        if ($null -eq $stream) {
            return ""
        }

        $reader = New-Object System.IO.StreamReader($stream)
        return $reader.ReadToEnd()
    } catch {
        return ""
    }
}

function Write-Failure {
    param(
        [string]$Command,
        [string]$ErrorMessage,
        [string]$Cause,
        [string]$File
    )

    Write-Host ""
    Write-Host "FALHA" -ForegroundColor Red
    Write-Host "Comando: $Command"
    Write-Host "Erro recebido: $ErrorMessage"
    Write-Host "Provavel causa: $Cause"
    Write-Host "Arquivo provavel para corrigir: $File"
}

function Invoke-CheckedWebRequest {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Uri,
        [hashtable]$Headers = @{},
        [string]$Body = $null,
        [int[]]$ExpectedStatus,
        [string]$Cause,
        [string]$File
    )

    $command = "$Method $Uri"
    Write-Host "==> $Name"

    try {
        $params = @{
            Method = $Method
            Uri = $Uri
            Headers = $Headers
            UseBasicParsing = $true
            TimeoutSec = 30
        }

        if ($null -ne $Body) {
            $params.Body = $Body
            $params.ContentType = "application/json"
        }

        $response = Invoke-WebRequest @params
        $status = [int]$response.StatusCode

        if ($ExpectedStatus -notcontains $status) {
            Write-Failure $command "HTTP $status" $Cause $File
            throw "Status HTTP inesperado"
        }

        Write-Host "OK HTTP $status"
        return $response.Content
    } catch {
        $response = $_.Exception.Response
        if ($null -ne $response) {
            $status = [int]$response.StatusCode
            $bodyText = Get-ResponseBody $response

            if ($ExpectedStatus -contains $status) {
                Write-Host "OK HTTP $status"
                return $bodyText
            }

            Write-Failure $command "HTTP $status $bodyText" $Cause $File
            throw
        }

        Write-Failure $command $_.Exception.Message $Cause $File
        throw
    }
}

function Invoke-CheckedCommand {
    param(
        [string]$Name,
        [string]$Command,
        [string]$Cause,
        [string]$File
    )

    Write-Host "==> $Name"
    try {
        Invoke-Expression $Command
        if ($LASTEXITCODE -ne 0) {
            Write-Failure $Command "Exit code $LASTEXITCODE" $Cause $File
            throw "Comando falhou"
        }
        Write-Host "OK"
    } catch {
        Write-Failure $Command $_.Exception.Message $Cause $File
        throw
    }
}

$auth = New-BasicAuthHeader $GatewayUser $GatewayPassword

Write-Host "Teste final da Pizzaria AF"
Write-Host "Gateway: $GatewayUrl"
Write-Host "Eureka:  $EurekaUrl"
Write-Host ""

Invoke-CheckedCommand `
    -Name "1. docker compose ps" `
    -Command "docker compose ps" `
    -Cause "Docker nao esta rodando, compose nao foi iniciado ou o comando foi executado fora da raiz do projeto." `
    -File "docker-compose.yml"

Invoke-CheckedWebRequest `
    -Name "2. verificar Eureka" `
    -Method "GET" `
    -Uri $EurekaUrl `
    -ExpectedStatus @(200) `
    -Cause "eureka-server nao esta acessivel na porta 8761." `
    -File "eureka-server/src/main/resources/application.yaml" | Out-Null

Invoke-CheckedWebRequest `
    -Name "3. rota protegida sem auth deve dar 401" `
    -Method "GET" `
    -Uri "$GatewayUrl/pizzaria/api/cardapio" `
    -ExpectedStatus @(401) `
    -Cause "Gateway nao esta protegendo /pizzaria/api/** ou seguranca do gateway foi alterada." `
    -File "gateway/src/main/java/com/af/gateway/config/GatewaySecurityConfig.java" | Out-Null

Invoke-CheckedWebRequest `
    -Name "4. rota protegida com auth deve dar 200" `
    -Method "GET" `
    -Uri "$GatewayUrl/pizzaria/api/cardapio" `
    -Headers $auth `
    -ExpectedStatus @(200) `
    -Cause "Gateway nao autenticou admin/admin123, rota nao foi descoberta no Eureka ou pizzaria-service falhou." `
    -File "gateway/src/main/resources/application.yaml" | Out-Null

$cliente = @{
    cpf = $Cpf
    nome = "Cliente Teste"
    celular = "51999999999"
    endereco = "Rua Teste, 100"
    email = $Email
    senha = "123456"
}

Invoke-CheckedWebRequest `
    -Name "5. cadastrar cliente" `
    -Method "POST" `
    -Uri "$GatewayUrl/pizzaria/public/clientes/registrar" `
    -Body (ConvertTo-JsonBody $cliente) `
    -ExpectedStatus @(200) `
    -Cause "Rota publica /pizzaria/public/** nao esta liberada ou cadastro de cliente falhou." `
    -File "gateway/src/main/java/com/af/gateway/config/GatewaySecurityConfig.java" | Out-Null

$estoqueAntesJson = Invoke-CheckedWebRequest `
    -Name "6. consultar estoque antes" `
    -Method "GET" `
    -Uri "$GatewayUrl/estoque/api/estoque" `
    -Headers $auth `
    -ExpectedStatus @(200) `
    -Cause "Gateway nao roteou /estoque/** ou estoque-service nao esta UP." `
    -File "gateway/src/main/resources/application.yaml"

$estoqueAntes = $estoqueAntesJson | ConvertFrom-Json
$produtoAntes = @($estoqueAntes | Where-Object { $_.produtoId -eq 1 })[0]
if ($null -eq $produtoAntes) {
    Write-Failure "GET $GatewayUrl/estoque/api/estoque" "Produto 1 nao encontrado" "Carga inicial do estoque nao contem produto 1." "estoque-service/src/main/resources/data.sql"
    throw "Produto 1 nao encontrado"
}

$pedido = @{
    cpf = $Cpf
    nome = "Cliente Teste"
    celular = "51999999999"
    endereco = "Rua Teste, 100"
    email = $Email
    itens = @(
        @{
            produtoId = 1
            quantidade = 1
        }
    )
}

$pedidoJson = Invoke-CheckedWebRequest `
    -Name "7. criar pedido" `
    -Method "POST" `
    -Uri "$GatewayUrl/pizzaria/api/pedidos/submeter" `
    -Headers $auth `
    -Body (ConvertTo-JsonBody $pedido) `
    -ExpectedStatus @(200) `
    -Cause "pizzaria-service nao conseguiu criar pedido ou consultar/baixar estoque." `
    -File "pizzaria-service/src/main/java/com/af/Dominio/Servicos/PedidoService.java"

$pedidoCriado = $pedidoJson | ConvertFrom-Json
$pedidoId = $pedidoCriado.id
if ($null -eq $pedidoId) {
    Write-Failure "POST $GatewayUrl/pizzaria/api/pedidos/submeter" "Resposta sem id" "Pedido foi criado sem retornar id." "pizzaria-service/src/main/java/com/af/Aplicacao/Responses/PedidoResponse.java"
    throw "Resposta sem id"
}

$estoqueDepoisJson = Invoke-CheckedWebRequest `
    -Name "8. consultar estoque depois" `
    -Method "GET" `
    -Uri "$GatewayUrl/estoque/api/estoque" `
    -Headers $auth `
    -ExpectedStatus @(200) `
    -Cause "Gateway nao roteou /estoque/** ou estoque-service nao esta UP apos o pedido." `
    -File "estoque-service/src/main/java/com/af/estoque/controller/EstoqueController.java"

$estoqueDepois = $estoqueDepoisJson | ConvertFrom-Json
$produtoDepois = @($estoqueDepois | Where-Object { $_.produtoId -eq 1 })[0]
if ($produtoDepois.quantidade -ne ($produtoAntes.quantidade - 1)) {
    Write-Failure "GET $GatewayUrl/estoque/api/estoque" "Quantidade antes=$($produtoAntes.quantidade), depois=$($produtoDepois.quantidade)" "Baixa de estoque nao aconteceu como esperado." "pizzaria-service/src/main/java/com/af/Adaptadores/Estoque/EstoqueServiceHttpClient.java"
    throw "Estoque nao foi baixado corretamente"
}
Write-Host "OK estoque baixou de $($produtoAntes.quantidade) para $($produtoDepois.quantidade)"

$pedidoSemEstoque = @{
    cpf = $Cpf
    nome = "Cliente Teste"
    celular = "51999999999"
    endereco = "Rua Teste, 100"
    email = $Email
    itens = @(
        @{
            produtoId = 1
            quantidade = 9999
        }
    )
}

Invoke-CheckedWebRequest `
    -Name "9. pedido sem estoque deve falhar" `
    -Method "POST" `
    -Uri "$GatewayUrl/pizzaria/api/pedidos/submeter" `
    -Headers $auth `
    -Body (ConvertTo-JsonBody $pedidoSemEstoque) `
    -ExpectedStatus @(409) `
    -Cause "Validacao de estoque nao rejeitou quantidade indisponivel." `
    -File "pizzaria-service/src/main/java/com/af/Dominio/Servicos/PedidoService.java" | Out-Null

Invoke-CheckedWebRequest `
    -Name "10. pagar pedido" `
    -Method "POST" `
    -Uri "$GatewayUrl/pizzaria/api/pedidos/pagamento/$pedidoId`?cpf=$Cpf" `
    -Headers $auth `
    -ExpectedStatus @(200) `
    -Cause "Pagamento falhou ou publicacao RabbitMQ quebrou o fluxo." `
    -File "pizzaria-service/src/main/java/com/af/Dominio/Servicos/PedidoService.java" | Out-Null

Start-Sleep -Seconds 5

Write-Host "==> 11. verificar logs do entregas-service consumindo RabbitMQ"
$logs = docker compose logs --tail=200 entregas-service
$logs | Select-String -Pattern "Instancia de entrega recebeu pedido|Instância de entrega recebeu pedido|Pedido entregue pela instancia atual|Pedido entregue pela instância atual"

if ($LASTEXITCODE -ne 0) {
    Write-Failure "docker compose logs --tail=200 entregas-service" "Nao foi possivel ler logs" "Docker Compose nao esta acessivel." "docker-compose.yml"
    throw "Falha ao ler logs"
}

if ($logs -notmatch "recebeu pedido: $pedidoId") {
    Write-Failure "docker compose logs --tail=200 entregas-service" "Pedido $pedidoId nao apareceu nos logs" "Mensagem RabbitMQ nao foi consumida por entregas-service." "pizzaria-service/src/main/java/com/af/Adaptadores/Entregas/PedidoEntregaPublisher.java"
    throw "Consumo RabbitMQ nao confirmado"
}

Write-Host ""
Write-Host "Teste final concluido com sucesso." -ForegroundColor Green
