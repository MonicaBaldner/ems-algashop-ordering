package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.commons;

import com.algaworks.algashop.ordering.core.domain.model.commons.Address;
import com.algaworks.algashop.ordering.core.domain.model.commons.ZipCode;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressEmbeddable {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;

    public Address toDomain() {
        return new Address(
                street,
                number,
                complement,
                neighborhood,
                city,
                state,
                new ZipCode(zipCode)
        );
    }

}
