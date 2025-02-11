package io.mpolivaha.jdsa.integration;

import io.mpolivaha.jdsa.annotations.EnableJakartaDataRepositories;
import io.mpolivaha.jdsa.annotations.HibernateJakartaDataConfiguration;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@EnableJakartaDataRepositories(
        hibernateConfig = @HibernateJakartaDataConfiguration(
                sessionFactoryRef = E2eTestsConfiguration.SESSION_FACTORY_REF
        )
)
@ComponentScan("io.mpolivaha.jdsa")
@Import(CassandraDataAutoConfiguration.class)
public class E2eTestsConfiguration {

    public static final String SESSION_FACTORY_REF = "customEntityManagerFactory";

    @Bean(SESSION_FACTORY_REF)
    public EntityManagerFactory customEntityManagerFactory() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        StatelessSession statelessSession = Mockito.mock(StatelessSession.class);
        Mockito.when(sessionFactory.openStatelessSession()).thenReturn(statelessSession);
        Mockito.when(sessionFactory.unwrap(SessionFactory.class)).thenReturn(sessionFactory);

        return sessionFactory;
    }
}
