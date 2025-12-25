package com.algaworks.algashop.ordering.core.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.AbstractDomainIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartsPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Import({ShoppingCartsPersistenceProvider.class, ShoppingCartPersistenceEntityAssembler.class,
        ShoppingCartPersistenceEntityDisassembler.class, CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class, CustomerPersistenceEntityDisassembler.class})
class ShoppingCartsIT extends AbstractDomainIT {

    private ShoppingCarts shoppingCarts;

    private Customers customers;

    @Autowired
    public ShoppingCartsIT(ShoppingCarts shoppingCarts, Customers customers) {
        this.shoppingCarts = shoppingCarts;
        this.customers = customers;
    }

    @BeforeEach
    public void setup(){
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)){
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    public void shouldPersistAndFind(){
        ShoppingCart originalShoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        ShoppingCartId shoppingCartId = originalShoppingCart.id();

        shoppingCarts.add(originalShoppingCart);

        Optional<ShoppingCart> optionalShoppingCart = shoppingCarts.ofId(shoppingCartId);

        Assertions.assertThat(optionalShoppingCart.isPresent());

        ShoppingCart savedShoppingCart = optionalShoppingCart.get();

        Assertions.assertThat(savedShoppingCart).satisfies(
                s-> Assertions.assertThat(s.id()).isEqualTo(shoppingCartId),
                s-> Assertions.assertThat(s.customerId()).isEqualTo(originalShoppingCart.customerId()),
                s-> Assertions.assertThat(s.totalAmount()).isEqualTo(originalShoppingCart.totalAmount()),
                s-> Assertions.assertThat(s.totalItems()).isEqualTo(originalShoppingCart.totalItems()),
                s->Assertions.assertThat(s.createdAt())
                        .isCloseTo(originalShoppingCart.createdAt(), Assertions.within(10, ChronoUnit.MILLIS))

        );

    }

    @Test
    public void shouldUpdateExistingShoppingCart(){
        ShoppingCart originalShoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(true).build();

        shoppingCarts.add(originalShoppingCart);

        originalShoppingCart = shoppingCarts.ofId(originalShoppingCart.id()).orElseThrow();

        originalShoppingCart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(1));

        shoppingCarts.add(originalShoppingCart);

        originalShoppingCart = shoppingCarts.ofId(originalShoppingCart.id()).orElseThrow();

        Assertions.assertThat(originalShoppingCart.totalItems().value()).isEqualTo(4);
    }

    @Test
    public void shouldCountExistingShoppingCarts(){

        Assertions.assertThat(shoppingCarts.count()).isZero();

        ShoppingCart ShoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        shoppingCarts.add(ShoppingCart);


        Assertions.assertThat(shoppingCarts.count()).isEqualTo(1);
    }

    @Test
    public void shouldReturnIfShoppingCartExists(){

        ShoppingCart ShoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        shoppingCarts.add(ShoppingCart);

        Assertions.assertThat(shoppingCarts.exists(ShoppingCart.id())).isTrue();
        Assertions.assertThat(shoppingCarts.exists(new ShoppingCartId())).isFalse();
    }

}
