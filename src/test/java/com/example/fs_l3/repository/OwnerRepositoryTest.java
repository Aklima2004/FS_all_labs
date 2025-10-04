package com.example.fs_l3.repository;

import com.example.fs_l3.domain.Owner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository repository;

    @Test
    void saveOwner() {
        Owner owner = new Owner("John", "Doe");
        Owner savedOwner = repository.save(owner);

        assertThat(savedOwner).isNotNull();
        assertThat(savedOwner.getId()).isGreaterThan(0);  // ID должен быть сгенерирован
    }

    @Test
    void findOwnerByFirstName() {
        Owner owner = new Owner("Jane", "Doe");
        repository.save(owner);

        Owner found = repository.findById(owner.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getFirstname()).isEqualTo("Jane");
    }

    @Test
    void deleteOwner() {
        Owner owner = new Owner("Mark", "Smith");
        Owner savedOwner = repository.save(owner);
        repository.deleteById(savedOwner.getId());

        assertThat(repository.existsById(savedOwner.getId())).isFalse();
    }
}
