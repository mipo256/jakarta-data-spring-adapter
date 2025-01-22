package io.mpolivaha.jdsa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enabling Jakarta Data generated Repository implementations
 * to be registered as beans in the Spring's {@link org.springframework.context.ApplicationContext}
 *
 * @author Mikhail Polivakha
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableJakartaDataRepositories {

    /**
     * Whether we should fail fast or not. It basically means that we should fail
     * in case we encountered some error during processing of the particular JAR file
     * in the classpath, looking for Jakarta Data repositories annotated
     * with {@link jakarta.data.repository.Repository}.
     */
    boolean failFast() default true;

    /**
     * Explicit specification of the packages to scan for jakarta data repositories
     */
    String[] packagesToScan();
}
