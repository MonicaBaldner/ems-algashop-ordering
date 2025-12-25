package com.algaworks.algashop.ordering.core.domain.model.order.service;

import com.algaworks.algashop.ordering.core.domain.model.AbstractDomainIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest
public class ShippingCostServiceComProductCatalogAPIClientFakeIT extends AbstractDomainIT {

    @Autowired
    private ShippingCostService shippingCostService;

    @Autowired
    private OriginAddressService originAddressService;

    @Test
    void shouldCalculate() {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode("12345");

        var calculate = shippingCostService
                .calculate(new ShippingCostService.CalculationRequest(origin, destination));

        Assertions.assertThat(calculate.cost()).isNotNull();
        Assertions.assertThat(calculate.expectedDate()).isNotNull();
    }


}
