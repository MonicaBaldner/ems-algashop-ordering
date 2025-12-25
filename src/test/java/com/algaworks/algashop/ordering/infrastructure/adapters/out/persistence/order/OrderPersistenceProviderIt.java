package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.order;


import com.algaworks.algashop.ordering.core.domain.model.order.Order;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderStatus;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.AbstractPersistenceIT;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.config.auditing.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityDisassembler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.flyway.locations=classpath:db/migration,classpath:db/testdata")
class OrdersPersistenceProviderIT extends AbstractPersistenceIT {

    private OrdersPersistenceProvider persistenceProvider;
    private CustomersPersistenceProvider customersPersistenceProvider;
    private OrderPersistenceEntityRepository entityRepository;

    @Autowired
    public OrdersPersistenceProviderIT(OrdersPersistenceProvider persistenceProvider,
                                       OrderPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
    }

   /* @BeforeEach
    public void setup() {
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }*/

    @Test
    public void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        long orderId = order.id().value().toLong();
        persistenceProvider.add(order);

        var persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        order = persistenceProvider.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        persistenceProvider.add(order);

        persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());

        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldAddFindAndNotFailWhenNoTransaction(){
        Order order = OrderTestDataBuilder.anOrder().build();
        persistenceProvider.add(order);
       /*  Order savedOrder = persistenceProvider.ofId(order.id()).orElseThrow();
     Assertions.assertThat(savedOrder).isNotNull();*/
        Assertions.assertThatNoException().isThrownBy(
                ()-> persistenceProvider.ofId(order.id()).orElseThrow()
        );
    }

}