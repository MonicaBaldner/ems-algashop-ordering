package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ShoppingCartPersistenceEntityAssemblerTest {

    @Mock
    private CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    @InjectMocks
    private ShoppingCartPersistenceEntityAssembler assembler;

    @BeforeEach
    void setup() {
        Mockito.when(customerPersistenceEntityRepository.getReferenceById(Mockito.any(UUID.class)))
                .thenAnswer(invocation -> {
                    UUID customerId = invocation.getArgument(0);
                    return CustomerPersistenceEntityTestDataBuilder.aCustomer().id(customerId).build();
                });
    }

    @Test
    void shouldConvertToPersistenceEntity() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(true).build();
        ShoppingCartPersistenceEntity entity = assembler.fromDomain(cart);

        assertThat(entity).satisfies(
                e -> assertThat(e.getId()).isEqualTo(cart.id().value()),
                e -> assertThat(e.getCustomerId()).isEqualTo(cart.customerId().value()),
                e -> assertThat(e.getTotalAmount()).isEqualTo(cart.totalAmount().value()),
                e -> assertThat(e.getTotalItems()).isEqualTo(cart.totalItems().value()),
                e -> assertThat(e.getCreatedAt()).isEqualTo(cart.createdAt()),
                e -> assertThat(e.getItems().size()).isEqualTo(cart.items().size())
        );
    }

    @Test
    void shouldMergeCartIntoExistingEntity() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(true).build();
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.emptyCart().build();

        assembler.merge(entity, cart);

        assertThat(entity).satisfies(
                e -> assertThat(e.getCustomerId()).isEqualTo(cart.customerId().value()),
                e -> assertThat(e.getTotalAmount()).isEqualTo(cart.totalAmount().value()),
                e -> assertThat(e.getTotalItems()).isEqualTo(cart.totalItems().value()),
                e -> assertThat(e.getItems().size()).isEqualTo(cart.items().size())
        );
    }

    @Test
    void givenCartWithNoItems_shouldClearPersistenceEntityItems() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.cartWithItems().build();

        assertThat(cart.items()).isEmpty();
        assertThat(entity.getItems()).isNotEmpty();

        assembler.merge(entity, cart);

        assertThat(entity.getItems()).isEmpty();
    }

    @Test
    void givenCartWithItems_shouldAddToPersistenceEntity() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(true).build();
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.emptyCart().build();

        assertThat(cart.items()).isNotEmpty();
        assertThat(entity.getItems()).isEmpty();

        assembler.merge(entity, cart);

        assertThat(entity.getItems()).hasSize(cart.items().size());
    }

    @Test
    void givenCartWithItems_whenMerge_shouldSyncCorrectly() {
        ShoppingCart cart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(true).build();

        Set<ShoppingCartItemPersistenceEntity> itemEntities = cart.items().stream()
                .map(item -> assembler.mergeItem(new ShoppingCartItemPersistenceEntity(), item))
                .collect(Collectors.toSet());

        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.cartWithItems(itemEntities).build();

        ShoppingCartItem itemToRemove = cart.items().iterator().next();
        cart.removeItem(itemToRemove.id());

        assembler.merge(entity, cart);

        assertThat(entity.getItems()).hasSize(cart.items().size());
    }
}