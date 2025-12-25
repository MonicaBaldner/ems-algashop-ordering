package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShoppingCartPersistenceEntityDisassemblerTest {

    private final ShoppingCartPersistenceEntityDisassembler disassembler = new ShoppingCartPersistenceEntityDisassembler();

    @Test
    void shouldConvertFromPersistenceEntityToDomain() {
        ShoppingCartPersistenceEntity persistenceEntity = ShoppingCartPersistenceEntityTestDataBuilder.cartWithItems().build();

        persistenceEntity.getItems().forEach(item -> {
            System.out.println("ProductId: " + item.getProductId());
            System.out.println("Name: " + item.getName());
            System.out.println("Price: " + item.getPrice());
        });

        ShoppingCart domainEntity = disassembler.toDomainEntity(persistenceEntity);

        assertThat(domainEntity).satisfies(
                s -> assertThat(s.id()).isEqualTo(new ShoppingCartId(persistenceEntity.getId())),
                s -> assertThat(s.customerId()).isEqualTo(new CustomerId(persistenceEntity.getCustomerId())),
                s -> assertThat(s.totalAmount()).isEqualTo(new Money(persistenceEntity.getTotalAmount())),
                s -> assertThat(s.totalItems()).isEqualTo(new Quantity(persistenceEntity.getTotalItems())),
                s -> assertThat(s.createdAt()).isEqualTo(persistenceEntity.getCreatedAt()),
                s -> assertThat(s.items()).hasSize(persistenceEntity.getItems().size())
        );

        for (ShoppingCartItemPersistenceEntity itemEntity : persistenceEntity.getItems()) {
            assertThat(domainEntity.items()).anySatisfy(item ->
                    assertThat(item.productId()).isEqualTo(new ProductId(itemEntity.getProductId()))
            );
            assertThat(domainEntity.items()).anySatisfy(item ->
                    assertThat(item.name()).isEqualTo(new ProductName(itemEntity.getName()))
            );
        }
    }
}
