package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomerPersistenceEntityAssemblerTest {

    private final CustomerPersistenceEntityAssembler assembler = new CustomerPersistenceEntityAssembler();

    @Test
    void shouldConvertToDomain(){
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerPersistenceEntity customerPersistenceEntity = assembler.fromDomain(customer);

        assertThat(customerPersistenceEntity).satisfies(
                c -> assertThat(c.getId()).isEqualTo(customer.id().value()),
                c -> assertThat(c.getFirstName()).isEqualTo(customer.fullName().firstName()),
                c -> assertThat(c.getLastName()).isEqualTo(customer.fullName().lastName()),
                c -> assertThat(c.getEmail()).isEqualTo(customer.email().value()),
                c -> assertThat(c.getPhone()).isEqualTo(customer.phone().value()),
                c -> assertThat(c.getDocument()).isEqualTo(customer.document().value()),
                c -> assertThat(c.getPromotionNotificationsAllowed()).isEqualTo(customer.isPromotionNotificationsAllowed()),
                c -> assertThat(c.getArchived()).isEqualTo(customer.isArchived()),
                c -> assertThat(customerPersistenceEntity.getAddress().toDomain()).isEqualTo(customer.address())

        );
    }

}
