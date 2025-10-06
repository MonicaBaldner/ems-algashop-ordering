package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {


    @Query("""
        SELECT o 
        FROM OrderPersistenceEntity o
        WHERE o.customer.id = :id
        AND YEAR(o.placedAt) = :year
        """)
    List<OrderPersistenceEntity> placedByCustomerInYear(
            @Param("id") UUID id,
            @Param("year") Integer year
    );

    @Query("""
        SELECT COUNT(o)
        FROM OrderPersistenceEntity o
        WHERE o.customer.id = :id
        AND YEAR(o.placedAt) = :year
        AND o.paidAt IS NOT NULL
        AND o.canceledAt IS NULL
        """)
    Long salesQuantityByCustomerInYear(
            @Param("id") UUID id,
            @Param("year") int year
    );

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount),0)
        FROM OrderPersistenceEntity o
        WHERE o.customer.id = :id
        AND o.canceledAt IS NULL
        AND o.paidAt IS NOT NULL
        """)
    BigDecimal totalSoldForCustomer(
            @Param("id") UUID id);


    @Override
    @EntityGraph(attributePaths = {"customer", "items"})
    Optional<OrderPersistenceEntity> findById(Long id);


}

