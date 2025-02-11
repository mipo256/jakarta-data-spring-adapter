package io.mpolivaha.jdsa.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = E2eTestsConfiguration.class)
public class E2eIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testEndToEnd() {
        BookRepository bookRepository = applicationContext.getBean(BookRepository.class);
        PersonRepository personRepository = applicationContext.getBean(PersonRepository.class);

        Assertions.assertThat(bookRepository).isNotNull();
        Assertions.assertThat(personRepository).isNotNull();
    }
}
