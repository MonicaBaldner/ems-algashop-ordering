package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomerPersistenceEntityDisassemblerTest {

    private final CustomerPersistenceEntityDisassembler disassembler = new CustomerPersistenceEntityDisassembler();

    @Test
    public void shouldConvertFromPersistence() {

        CustomerPersistenceEntity persistenceEntity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();

        Customer customerDomainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(customerDomainEntity).satisfies(
                c -> assertThat(c.id().value()).isEqualTo(persistenceEntity.getId()),
                c -> assertThat(c.fullName().firstName()).isEqualTo(persistenceEntity.getFirstName()),
                c -> assertThat(c.fullName().lastName()).isEqualTo(persistenceEntity.getLastName()),
                c -> assertThat(c.birthDate().value()).isEqualTo(persistenceEntity.getBirthDate()),
                c -> assertThat(c.email().value()).isEqualTo(persistenceEntity.getEmail()),
                c -> assertThat(c.phone().value()).isEqualTo(persistenceEntity.getPhone()),
                c -> assertThat(c.document().value()).isEqualTo(persistenceEntity.getDocument()),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isEqualTo(persistenceEntity.getPromotionNotificationsAllowed()),
                c -> assertThat(c.isArchived()).isEqualTo(persistenceEntity.getArchived()),
                c -> assertThat(c.registeredAt()).isEqualTo(persistenceEntity.getRegisteredAt()),
                c -> assertThat(c.archivedAt()).isEqualTo(persistenceEntity.getArchivedAt()),
                c -> assertThat(c.loyaltyPoints().value()).isEqualTo(persistenceEntity.getLoyaltyPoints()),
                c -> assertThat(c.version()).isEqualTo(persistenceEntity.getVersion()),
                c -> assertThat(c.address()).isEqualTo(persistenceEntity.getAddress().toDomain())
        );
    }
}
