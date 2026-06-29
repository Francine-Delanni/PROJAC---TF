package com.af.Dominio.Excecoes;

public class EstoqueServiceException extends RuntimeException {

    public EstoqueServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
