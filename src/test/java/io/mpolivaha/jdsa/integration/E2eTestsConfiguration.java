package io.mpolivaha.jdsa.integration;

import io.mpolivaha.jdsa.annotations.EnableJakartaDataRepositories;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@EnableJakartaDataRepositories
@ComponentScan("io.mpolivaha.jdsa")
public class E2eTestsConfiguration {

}
