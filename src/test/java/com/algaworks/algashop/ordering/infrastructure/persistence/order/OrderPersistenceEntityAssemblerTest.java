package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderItem;
import com.algaworks.algashop.ordering.domain.model.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.order.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderPersistenceEntityAssemblerTest {

    @Mock
    private CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    @InjectMocks
    private OrderPersistenceEntityAssembler assembler;

    @BeforeEach
    public void setup(){
        Mockito.when(customerPersistenceEntityRepository
                        .getReferenceById(Mockito.any(UUID.class)))
                .then(a->{
                    UUID customerId = a.getArgument(0, UUID.class);
                    return CustomerPersistenceEntityTestDataBuilder.aCustomer().id(customerId).build();
                });
    }


    @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);
        assertThat(orderPersistenceEntity).satisfies(
                p-> assertThat(p.getId()).isEqualTo(order.id().value().toLong()),
                p-> assertThat(p.getCustomerId()).isEqualTo(order.customerId().value()),
                p -> assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                p -> assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value()),
                p -> assertThat(p.getStatus()).isEqualTo(order.status().name()),
                p -> assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                p -> assertThat(p.getPlacedAt()).isEqualTo(order.placedAt()),
                p -> assertThat(p.getPaidAt()).isEqualTo(order.paidAt()),
                p -> assertThat(p.getCanceledAt()).isEqualTo(order.canceledAt()),
                p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt()),
                p -> {
                    assertThat(p.getBilling()).isNotNull();
                    assertThat(p.getBilling().getFirstName()).isEqualTo(order.billing().fullName().firstName());
                    assertThat(p.getBilling().getLastName()).isEqualTo(order.billing().fullName().lastName());
                    assertThat(p.getBilling().getDocument()).isEqualTo(order.billing().document().value());
                    assertThat(p.getBilling().getPhone()).isEqualTo(order.billing().phone().value());
                },
                p -> {
                    assertThat(p.getShipping()).isNotNull();
                    assertThat(p.getShipping().getCost()).isEqualTo(order.shipping().cost().value());
                    assertThat(p.getShipping().getExpectedDate()).isEqualTo(order.shipping().expectedDate());
                    assertThat(p.getShipping().getAddress().getStreet()).isEqualTo(order.shipping().address().street());
                    assertThat(p.getShipping().getRecipient().getFirstName()).isEqualTo(order.shipping().recipient().fullName().firstName());
                    assertThat(p.getShipping().getRecipient().getLastName()).isEqualTo(order.shipping().recipient().fullName().lastName());
                    assertThat(p.getShipping().getRecipient().getDocument()).isEqualTo(order.shipping().recipient().document().value());
                    assertThat(p.getShipping().getRecipient().getPhone()).isEqualTo(order.shipping().recipient().phone().value());
                }

        );
    }


    @Test
    void shouldMerge() {
        // Arrange: cria um pedido de teste
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();
        OrderPersistenceEntity entity = new OrderPersistenceEntity();

        // Act: aplica o merge
        assembler.merge(entity, order);

        // Assert: verifica se os dados foram corretamente copiados
        assertThat(entity).satisfies(
                p -> assertThat(p.getCustomerId()).isEqualTo(order.customerId().value()),
                p -> assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                p -> assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value()),
                p -> assertThat(p.getStatus()).isEqualTo(order.status().name()),
                p -> assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                p -> assertThat(p.getPlacedAt()).isEqualTo(order.placedAt()),
                p -> assertThat(p.getPaidAt()).isEqualTo(order.paidAt()),
                p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt()),
                p -> {
                    assertThat(p.getBilling()).isNotNull();
                    assertThat(p.getBilling().getFirstName()).isEqualTo("John");
                    assertThat(p.getBilling().getLastName()).isEqualTo("Doe");
                    assertThat(p.getBilling().getDocument()).isEqualTo("225-09-1992");
                    assertThat(p.getBilling().getPhone()).isEqualTo("123-111-9911");
                    assertThat(p.getBilling().getAddress().getStreet()).isEqualTo("Bourbon Street");
                    assertThat(p.getBilling().getAddress().getZipCode()).isEqualTo("79911");
                },
                p -> {
                    assertThat(p.getShipping()).isNotNull();
                    assertThat(p.getShipping().getCost()).isEqualTo(order.shipping().cost().value());
                    assertThat(p.getShipping().getExpectedDate()).isEqualTo(order.shipping().expectedDate());
                    assertThat(p.getShipping().getAddress().getStreet()).isEqualTo("Bourbon Street");
                    assertThat(p.getShipping().getAddress().getZipCode()).isEqualTo("79911");
                    assertThat(p.getShipping().getRecipient().getFirstName()).isEqualTo("John");
                    assertThat(p.getShipping().getRecipient().getLastName()).isEqualTo("Doe");
                    assertThat(p.getShipping().getRecipient().getDocument()).isEqualTo("112-33-2321");
                    assertThat(p.getShipping().getRecipient().getPhone()).isEqualTo("111-441-1244");
                }
        );

    }

    @Test
    void givenOrderWhithNoItems_shouldRemovePersistenceEntityItems(){
        Order order = OrderTestDataBuilder.anOrder().withItems(false).build();
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        Assertions.assertThat(order.items()).isEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();

        assembler.merge(orderPersistenceEntity, order);
        Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();
    }

    @Test
    void givenOrderWithItems_shouldAddToPersistenceEntity(){
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder
                .existingOrder()
                .items(new HashSet<>())
                .build();

        Assertions.assertThat(order.items()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();

        assembler.merge(orderPersistenceEntity, order);
        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity
                        .getItems().size())
                .isEqualTo(order.items().size());
    }

    @Test
    void givenOrderWithItems_whenMerge_shouldMergeCorrectly(){
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();

        Assertions.assertThat(order.items().size()).isEqualTo(2);

        Set<OrderItemPersistenceEntity> orderItemPersistenceEntities = order
                .items()
                .stream()
                .map(i -> assembler.fromDomain(i))
                .collect(Collectors.toSet());

        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder
                .existingOrder()
                .items(orderItemPersistenceEntities)
                .build();

        OrderItem orderItem = order.items().iterator().next();
        order.removeItem(orderItem.id());

        assembler.merge(orderPersistenceEntity, order);

        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity
                        .getItems().size())
                .isEqualTo(order.items().size());
    }

}