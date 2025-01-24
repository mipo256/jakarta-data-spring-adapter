package io.mpolivaha.jdsa.core;

import io.mpolivaha.jdsa.annotations.EnableJakartaDataRepositories;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.mpolivaha.jdsa")
@EnableJakartaDataRepositories
public class TestConfiguration {
}
