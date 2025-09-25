package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.order.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceEntityDisassemblerTest {

    private final OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    @Test
    public void shouldConvertFromPersistence() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        persistenceEntity.getItems().forEach(item -> {
            System.out.println("ProductId: " + item.getProductId());
            System.out.println("ProductName: " + item.getProductName());
            System.out.println("Price: " + item.getPrice());
        });


        Order domainEntity = disassembler.toDomainEntity(persistenceEntity);
        assertThat(domainEntity).satisfies(
                s -> assertThat(s.id()).isEqualTo(new OrderId(persistenceEntity.getId())),
                s -> assertThat(s.customerId()).isEqualTo(new CustomerId(persistenceEntity.getCustomerId())),
                s -> assertThat(s.totalAmount()).isEqualTo(new Money(persistenceEntity.getTotalAmount())),
                s -> assertThat(s.totalItems()).isEqualTo(new Quantity(persistenceEntity.getTotalItems())),
                s -> assertThat(s.placedAt()).isEqualTo(persistenceEntity.getPlacedAt()),
                s -> assertThat(s.paidAt()).isEqualTo(persistenceEntity.getPaidAt()),
                s -> assertThat(s.canceledAt()).isEqualTo(persistenceEntity.getCanceledAt()),
                s -> assertThat(s.readyAt()).isEqualTo(persistenceEntity.getReadyAt()),
                s -> assertThat(s.status()).isEqualTo(OrderStatus.valueOf(persistenceEntity.getStatus())),
                s -> assertThat(s.paymentMethod()).isEqualTo(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod())),
                s -> assertThat(s.items()).hasSize(persistenceEntity.getItems().size()),

                // Billing
                s -> assertThat(s.billing().fullName().firstName()).isEqualTo(persistenceEntity.getBilling().getFirstName()),
                s -> assertThat(s.billing().fullName().lastName()).isEqualTo(persistenceEntity.getBilling().getLastName()),
                s -> assertThat(s.billing().document().value()).isEqualTo(persistenceEntity.getBilling().getDocument()),
                s -> assertThat(s.billing().phone().value()).isEqualTo(persistenceEntity.getBilling().getPhone()),
                s -> assertThat(s.billing().email().value()).isEqualTo(persistenceEntity.getBilling().getEmail()),

                s -> assertThat(s.billing().address().zipCode().value()).isEqualTo(persistenceEntity.getBilling().getAddress().getZipCode()),

                // Shipping
                s -> assertThat(s.shipping().cost()).isEqualTo(new Money(persistenceEntity.getShipping().getCost())),
                s -> assertThat(s.shipping().expectedDate()).isEqualTo(persistenceEntity.getShipping().getExpectedDate()),
                s -> assertThat(s.shipping().recipient().fullName().firstName()).isEqualTo(persistenceEntity.getShipping().getRecipient().getFirstName()),
                s -> assertThat(s.shipping().recipient().fullName().lastName()).isEqualTo(persistenceEntity.getShipping().getRecipient().getLastName()),
                s -> assertThat(s.shipping().recipient().document().value()).isEqualTo(persistenceEntity.getShipping().getRecipient().getDocument()),
                s -> assertThat(s.shipping().recipient().phone().value()).isEqualTo(persistenceEntity.getShipping().getRecipient().getPhone()),
                s -> assertThat(s.shipping().address().zipCode().value()).isEqualTo(persistenceEntity.getShipping().getAddress().getZipCode()),

                // Items
                s -> assertThat(s.items()).hasSize(persistenceEntity.getItems().size())

                );
    }

}