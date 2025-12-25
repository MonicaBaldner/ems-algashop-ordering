package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity persistenceEntity,
                                               ShoppingCart shoppingCart) {
        persistenceEntity.setId(shoppingCart.id().value());
        persistenceEntity.setCustomer(customerPersistenceEntityRepository
                .getReferenceById(shoppingCart.customerId().value()));
        persistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
        persistenceEntity.setTotalItems(shoppingCart.totalItems().value());
        persistenceEntity.setCreatedAt(shoppingCart.createdAt());
        persistenceEntity.setVersion((shoppingCart.version()));
      //  persistenceEntity.replaceItems(toOrderItemsEntities(shoppingCart.items()));
        // Cria um mapa dos itens já existentes por ID
        Map<UUID, ShoppingCartItemPersistenceEntity> itensExistentesPorId = persistenceEntity.getItems().stream()
                .collect(Collectors.toMap(ShoppingCartItemPersistenceEntity::getId, i -> i));

        // Atualiza ou cria os itens com base no domínio
        Set<ShoppingCartItemPersistenceEntity> itensAtualizados = shoppingCart.items().stream()
                .map(itemDominio -> {
                    UUID itemId = itemDominio.id().value();
                    ShoppingCartItemPersistenceEntity existente = itensExistentesPorId.get(itemId);
                    if (existente != null) {
                        return mergeItem(existente, itemDominio); // atualiza o existente
                    } else {
                        return mergeItem(new ShoppingCartItemPersistenceEntity(), itemDominio); // cria novo
                    }
                })
                .collect(Collectors.toSet());

        persistenceEntity.getItems().clear();
        persistenceEntity.getItems().addAll(itensAtualizados);

        // Garante que todos os itens saibam a quem pertencem
        persistenceEntity.getItems().forEach(item -> item.setShoppingCart(persistenceEntity));

        persistenceEntity.addEvents(shoppingCart.domainEvents());

        return persistenceEntity;
    }

    private Set<ShoppingCartItemPersistenceEntity> toOrderItemsEntities(Set<ShoppingCartItem> source) {
        return source.stream().map(i -> this.mergeItem(new ShoppingCartItemPersistenceEntity(), i)).collect(Collectors.toSet());
    }

    ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity persistenceEntity,
                                                ShoppingCartItem shoppingCartItem
    ) {
        persistenceEntity.setId(shoppingCartItem.id().value());
        persistenceEntity.setProductId(shoppingCartItem.productId().value());
        persistenceEntity.setName(shoppingCartItem.name().value());
        persistenceEntity.setPrice(shoppingCartItem.price().value());
        persistenceEntity.setQuantity(shoppingCartItem.quantity().value());
        persistenceEntity.setAvailable(shoppingCartItem.isAvailable());
        persistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());
        return persistenceEntity;
    }

    private ShoppingCartItemPersistenceEntity toOrderItemsEntities(ShoppingCartItem source) {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(source.id().value())
                .shoppingCart(ShoppingCartPersistenceEntity.builder().id(source.shoppingCartId().value()).build())
                .productId(source.productId().value())
                .name(source.name().value())
                .price(source.price().value())
                .quantity(source.quantity().value())
                .available(source.isAvailable())
                .totalAmount(source.totalAmount().value())
                .build();
    }
}
