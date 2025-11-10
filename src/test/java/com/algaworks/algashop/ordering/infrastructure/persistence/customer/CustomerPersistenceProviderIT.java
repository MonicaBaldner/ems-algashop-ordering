package com.algaworks.algashop.ordering.infrastructure.persistence.customer;
import com.algaworks.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerPersistenceProviderIT {

    @Autowired
    private CustomerPersistenceEntityRepository repository;

    @Autowired
    private EntityManager entityManager;



    @Autowired
    private CustomerPersistenceEntityDisassembler disassembler;

    @Test
    void shouldPersistCustomerCorrectly() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();

        CustomerPersistenceEntity saved = repository.save(entity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(entity.getEmail());
        assertThat(saved.getAddress()).isEqualTo(entity.getAddress());
    }

    @Test
    void shouldUpdateCustomerAndIncrementVersion() {
        CustomerPersistenceEntity entity = repository.saveAndFlush(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        Long originalVersion = entity.getVersion(); // deve ser 0

        entity.setFirstName("UpdatedName");

        CustomerPersistenceEntity updated = repository.saveAndFlush(entity);

        assertThat(updated.getVersion()).isGreaterThan(originalVersion); // deve ser 1
        assertThat(updated.getFirstName()).isEqualTo("UpdatedName");
    }

    @Test
    void shouldFailUpdateWithOldVersionUsingDetach() {
        // Salva e sincroniza com o banco
        CustomerPersistenceEntity entity = repository.saveAndFlush(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        // Simula dois usuários acessando a mesma entidade
        CustomerPersistenceEntity staleEntity = repository.findById(entity.getId()).orElseThrow();
        CustomerPersistenceEntity freshEntity = repository.findById(entity.getId()).orElseThrow();

        // Desanexa a entidade "stale" do contexto de persistência
        entityManager.detach(staleEntity);

        // Atualiza e salva a versão mais recente
        freshEntity.setFirstName("Fresh Update");
        repository.saveAndFlush(freshEntity); // versão incrementada

        // Tenta salvar a versão antiga (stale)
        staleEntity.setFirstName("Stale Update");

        assertThatThrownBy(() -> repository.saveAndFlush(staleEntity))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void shouldFindCustomerByIdWithCompleteMapping() {
        CustomerPersistenceEntity entity = repository.save(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        Optional<CustomerPersistenceEntity> found = repository.findById(entity.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(entity.getEmail());
        assertThat(found.get().getAddress()).isEqualTo(entity.getAddress());
    }

    @Test
    void shouldCountAndCheckExistence() {
        long countBefore = repository.count();

        CustomerPersistenceEntity entity = repository.save(CustomerPersistenceEntityTestDataBuilder.aCustomer().build());

        long countAfter = repository.count();

        assertThat(countAfter).isEqualTo(countBefore + 1);
        assertThat(repository.existsById(entity.getId())).isTrue();
    }
}