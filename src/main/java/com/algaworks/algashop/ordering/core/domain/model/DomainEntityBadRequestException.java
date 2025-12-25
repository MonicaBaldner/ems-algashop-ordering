package com.algaworks.algashop.ordering.core.domain.model;

public class DomainEntityBadRequestException extends RuntimeException{
    public DomainEntityBadRequestException() {
    }

    public DomainEntityBadRequestException(String message) {
        super(message);
    }

    public DomainEntityBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainEntityBadRequestException(Throwable cause) {
        super(cause);
    }
}
