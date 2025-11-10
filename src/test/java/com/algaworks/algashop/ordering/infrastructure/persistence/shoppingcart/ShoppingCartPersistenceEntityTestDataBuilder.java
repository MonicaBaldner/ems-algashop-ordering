package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

public class ShoppingCartPersistenceEntityTestDataBuilder {

    private ShoppingCartPersistenceEntityTestDataBuilder() {
    }

    public static ShoppingCartPersistenceEntity.ShoppingCartPersistenceEntityBuilder cartWithItems() {
        ShoppingCartPersistenceEntity.ShoppingCartPersistenceEntityBuilder builder =
                ShoppingCartPersistenceEntity.builder()
                        .id(IdGenerator.generateTimeBasedUUID())
                        .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
                        .totalItems(2)
                        .totalAmount(new BigDecimal("150.00"))
                        .createdAt(OffsetDateTime.now());

        ShoppingCartPersistenceEntity cart = builder.build();

        ShoppingCartItemPersistenceEntity item1 = existingItem().shoppingCart(cart).build();
        ShoppingCartItemPersistenceEntity item2 = existingItemAlt().shoppingCart(cart).build();

        //cart.setItems(Set.of(item1, item2));
        cart.setItems(new HashSet<>(Set.of(item1, item2)));

        return cart.toBuilder();
    }

    public static ShoppingCartPersistenceEntity.ShoppingCartPersistenceEntityBuilder emptyCart() {
        return ShoppingCartPersistenceEntity.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
                .totalItems(0)
                .totalAmount(BigDecimal.ZERO)
                .createdAt(OffsetDateTime.now())
                .items(Set.of());
    }

    public static ShoppingCartPersistenceEntity.ShoppingCartPersistenceEntityBuilder cartWithItems(Set<ShoppingCartItemPersistenceEntity> items) {
        return ShoppingCartPersistenceEntity.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .customer(CustomerPersistenceEntityTestDataBuilder.aCustomer().build())
                .totalItems(items.size())
                .totalAmount(items.stream()
                        .map(ShoppingCartItemPersistenceEntity::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .createdAt(OffsetDateTime.now())
                .items(items);
    }

    public static ShoppingCartItemPersistenceEntity.ShoppingCartItemPersistenceEntityBuilder existingItem() {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .productId(IdGenerator.generateTimeBasedUUID())
                .name("Teclado Gamer")
                .price(new BigDecimal("100.00"))
                .quantity(1)
                .available(true)
                .totalAmount(new BigDecimal("100.00"))
                .shoppingCart(null); // será preenchido no assembler
    }

    public static ShoppingCartItemPersistenceEntity.ShoppingCartItemPersistenceEntityBuilder existingItemAlt() {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .productId(IdGenerator.generateTimeBasedUUID())
                .name("Mouse RGB")
                .price(new BigDecimal("50.00"))
                .quantity(1)
                .available(true)
                .totalAmount(new BigDecimal("50.00"))
                .shoppingCart(null);
    }
}