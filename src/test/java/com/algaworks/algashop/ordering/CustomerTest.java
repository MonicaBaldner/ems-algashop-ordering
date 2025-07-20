package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTest {

    @Test
    public void testingCustomer(){
        Customer customer = new Customer(
                 new CustomerId(),
                 new FullName("Kevin","Valentim"),
                new BirthDate(LocalDate.of(1990,10,9)),
                new Email("kv@email.com"),
                new Phone("478-256-2504"),
                new Document("255-08-0578"),
                true,
                OffsetDateTime.now()
        );

        System.out.println(customer.id());
        System.out.println(IdGenerator.generateTimeBasedUUID());

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
    }
}
