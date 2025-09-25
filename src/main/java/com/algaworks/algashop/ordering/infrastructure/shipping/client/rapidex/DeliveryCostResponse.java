package com.algaworks.algashop.ordering.infrastructure.shipping.client.rapidex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCostResponse {

    @JsonProperty("deliveryCost")
    private String deliveryCost;

    @JsonProperty("estimatedDaysToDeliver")
    private Long estimatedDaysToDeliver;
}