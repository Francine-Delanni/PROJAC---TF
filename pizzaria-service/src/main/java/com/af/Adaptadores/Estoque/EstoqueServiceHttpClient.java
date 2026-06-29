package com.af.Adaptadores.Estoque;

import com.af.Dominio.Excecoes.EstoqueInsuficienteException;
import com.af.Dominio.Excecoes.EstoqueServiceException;
import com.af.Dominio.Servicos.EstoqueService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class EstoqueServiceHttpClient implements EstoqueService {

    private static final String ESTOQUE_SERVICE_URL = "http://ESTOQUE-SERVICE";

    private final RestClient restClient;

    public EstoqueServiceHttpClient(@LoadBalanced RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(ESTOQUE_SERVICE_URL).build();
    }

    @Override
    public boolean verificarDisponibilidade(Long produtoId, int quantidade) {
        try {
            Boolean disponivel = restClient.get()
                    .uri("/api/estoque/{produtoId}/disponivel?quantidade={quantidade}", produtoId, quantidade)
                    .retrieve()
                    .body(Boolean.class);

            return Boolean.TRUE.equals(disponivel);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                return false;
            }
            throw new EstoqueServiceException("Falha ao consultar disponibilidade no estoque-service", ex);
        } catch (RestClientException ex) {
            throw new EstoqueServiceException("Falha ao consultar disponibilidade no estoque-service", ex);
        }
    }

    @Override
    public void baixarEstoque(Long produtoId, int quantidade) {
        try {
            restClient.post()
                    .uri("/api/estoque/{produtoId}/baixar?quantidade={quantidade}", produtoId, quantidade)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)
                    || ex.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
                throw new EstoqueInsuficienteException(
                        "Pedido rejeitado: estoque insuficiente para o produto " + produtoId, ex);
            }
            throw new EstoqueServiceException("Falha ao baixar estoque no estoque-service", ex);
        } catch (RestClientException ex) {
            throw new EstoqueServiceException("Falha ao baixar estoque no estoque-service", ex);
        }
    }
}
