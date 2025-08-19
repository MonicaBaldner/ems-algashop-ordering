package com.algaworks.algashop.ordering.domain.exceptions;

import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException{

    public CustomerArchivedException(Throwable cause) {
        super(ERROR_CUSTOMER_ARCHIVED, cause);
    }

    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }
}
