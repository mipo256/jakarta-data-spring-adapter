package io.mpolivaha.jdsa.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration for Hibernate as a vendor of Jakarta Data repositories.
 *
 * @author Mikhail Polivakha
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HibernateJakartaDataConfiguration {

    /**
     * The name of the Spring bean of type {@link org.hibernate.SessionFactory} that should
     * be used when creating new Jakarta Data repository instances and putting them into
     * {@link org.springframework.context.ApplicationContext}. The default behavior is to
     * look up the {@link jakarta.persistence.EntityManagerFactory} from context and try to
     * unwrap it onto {@link org.hibernate.SessionFactory}
     */
    String sessionFactoryRef() default "";
}
