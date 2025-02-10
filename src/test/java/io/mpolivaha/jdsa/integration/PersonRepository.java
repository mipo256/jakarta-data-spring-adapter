package io.mpolivaha.jdsa.integration;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Repository
public interface PersonRepository extends CrudRepository<PersonRepository.Person, Long> {

    @Entity
    class Person {
        @Id
        private Long id;
    }
}
