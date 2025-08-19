package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

import java.util.Optional;

public class OrdersImpl implements Orders {

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        return Optional.empty();
    }

    @Override
    public boolean exists(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order aggregateRoot) {

    }

    @Override
    public int count() {
        return 0;
    }
}
