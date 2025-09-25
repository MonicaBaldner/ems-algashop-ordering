package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity.OrderPersistenceEntityBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityBuilder existingOrder() {
        BillingEmbeddable billing = buildBilling();
        ShippingEmbeddable shipping = buildShipping();

        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
                .totalItems(3)
                .totalAmount(new BigDecimal("1250"))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .billing(billing)
                .shipping(shipping)
                .items(Set.of(
                        existingItem().build(),
                        existingItemAlt().build()
                ));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItem(){
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productId(IdGenerator.generateTimeBasedUUID())
                .productName("Notebook")
                .price(new BigDecimal(500))
                .quantity(2)
                .totalAmount(new BigDecimal(1000));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItemAlt(){
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .price(new BigDecimal(250))
                .quantity(1)
                .totalAmount(new BigDecimal(250))
                .productName("Mouse pad")
                .productId(IdGenerator.generateTimeBasedUUID());
    }

    private static BillingEmbeddable buildBilling() {
        BillingEmbeddable billing = new BillingEmbeddable();
        billing.setFirstName("João");
        billing.setLastName("Silva");
        billing.setDocument("12345678900");
        billing.setPhone("21999999999");
        billing.setEmail("teste@email.com");
        billing.setAddress(buildAddress());
        return billing;
    }

    private static ShippingEmbeddable buildShipping() {
        ShippingEmbeddable shipping = new ShippingEmbeddable();
        shipping.setCost(new BigDecimal("20.00"));
        shipping.setExpectedDate(LocalDate.now().plusDays(3));
        shipping.setRecipient(buildRecipient());
        shipping.setAddress(buildAddress());
        return shipping;
    }

    private static AddressEmbeddable buildAddress() {
        AddressEmbeddable address = new AddressEmbeddable();
        address.setStreet("Rua das Flores");
        address.setNumber("123");
        address.setComplement("Apto 101");
        address.setNeighborhood("Centro");
        address.setCity("Petrópolis");
        address.setState("RJ");
        address.setZipCode("55555");
        return address;
    }

    private static RecipientEmbeddable buildRecipient() {
        RecipientEmbeddable recipient = new RecipientEmbeddable();
        recipient.setFirstName("Maria");
        recipient.setLastName("Oliveira");
        recipient.setDocument("98765432100");
        recipient.setPhone("21988888888");
        return recipient;
    }

}
