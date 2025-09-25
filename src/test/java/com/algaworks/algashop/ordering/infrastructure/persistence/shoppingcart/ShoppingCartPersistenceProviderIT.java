package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        ShoppingCartsPersistenceProvider.class,
        ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class ShoppingCartPersistenceProviderIT {

    @Autowired
    private ShoppingCartPersistenceEntityRepository cartRepository;

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    // 1. Persistir um ShoppingCart com múltiplos itens
    @Test
    void shouldPersistShoppingCartWithMultipleItems() {
        CustomerPersistenceEntity customer = customerRepository.save(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        ShoppingCartPersistenceEntity cart = ShoppingCartPersistenceEntityTestDataBuilder.emptyCart().customer(customer).build();

        ShoppingCartItemPersistenceEntity item1 = ShoppingCartPersistenceEntityTestDataBuilder.existingItem().shoppingCart(cart).build();
        ShoppingCartItemPersistenceEntity item2 = ShoppingCartPersistenceEntityTestDataBuilder.existingItemAlt().shoppingCart(cart).build();

        cart.setItems(Set.of(item1, item2));

        ShoppingCartPersistenceEntity saved = cartRepository.save(cart);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getItems()).hasSize(2);
        assertThat(saved.getItems()).allMatch(i -> i.getShoppingCart().getId().equals(saved.getId()));
    }

    // 2. Garantir que createdAt e lastModifiedAt são preenchidos corretamente após o save
    @Test
    void shouldFillAuditFieldsOnSave() {
        CustomerPersistenceEntity customer = customerRepository.save(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        ShoppingCartPersistenceEntity cart = ShoppingCartPersistenceEntityTestDataBuilder.emptyCart().customer(customer).build();

        ShoppingCartPersistenceEntity saved = cartRepository.save(cart);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getLastModifiedAt()).isNotNull();
    }

    // 3. Buscar um carrinho por customerId e validar os dados dos itens
    @Test
    void shouldFindCartByCustomerIdAndValidateItems() {
        CustomerPersistenceEntity customer = customerRepository.save(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        ShoppingCartPersistenceEntity cart = ShoppingCartPersistenceEntityTestDataBuilder.emptyCart().customer(customer).build();

        ShoppingCartItemPersistenceEntity item = ShoppingCartPersistenceEntityTestDataBuilder.existingItem().shoppingCart(cart).build();
        cart.setItems(Set.of(item));

        cartRepository.save(cart);

        Optional<ShoppingCartPersistenceEntity> found = cartRepository.findByCustomer_Id(customer.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getItems()).hasSize(1);
        assertThat(found.get().getItems().iterator().next().getProductId()).isEqualTo(item.getProductId());
    }

    // 4. Remover um carrinho do banco e garantir que ele não é mais encontrado
    @Test
    void shouldRemoveCartAndEnsureItIsNotFound() {
        CustomerPersistenceEntity customer = customerRepository.save(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        ShoppingCartPersistenceEntity cart = ShoppingCartPersistenceEntityTestDataBuilder.emptyCart().customer(customer).build();
        ShoppingCartPersistenceEntity saved = cartRepository.save(cart);

        cartRepository.deleteById(saved.getId());

        Optional<ShoppingCartPersistenceEntity> found = cartRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    // 5. Atualizar um carrinho existente e verificar que a nova versão é persistida
    @Test
    void shouldUpdateCartAndIncrementVersion() {
        CustomerPersistenceEntity customer = customerRepository.save(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        ShoppingCartPersistenceEntity cart = ShoppingCartPersistenceEntityTestDataBuilder.emptyCart().customer(customer).build();
        ShoppingCartPersistenceEntity saved = cartRepository.saveAndFlush(cart);

        Long originalVersion = saved.getVersion();

        saved.setTotalItems(5);
        ShoppingCartPersistenceEntity updated = cartRepository.saveAndFlush(saved);

        assertThat(updated.getVersion()).isGreaterThan(originalVersion);
        assertThat(updated.getTotalItems()).isEqualTo(5);
    }
}