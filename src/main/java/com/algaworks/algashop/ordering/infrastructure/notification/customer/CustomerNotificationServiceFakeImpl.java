package com.algaworks.algashop.ordering.infrastructure.notification.customer;

import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationServiceFakeImpl implements CustomerNotificationApplicationService {

    @Override
    public void notifyNewRegistration(CustomerNotificationApplicationService.NotifyNewRegistrationInput input)  {

        log.info("Welcome {}", input.firstName());
        log.info("User your email to access your account {}", input.email());

    }
}
