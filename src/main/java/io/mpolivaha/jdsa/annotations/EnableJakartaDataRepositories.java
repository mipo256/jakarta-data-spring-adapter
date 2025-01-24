package io.mpolivaha.jdsa.annotations;

import io.mpolivaha.jdsa.JakartaDataVendor;

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
     * Explicit specification of the packages to scan for jakarta data repositories.
     */
    String[] packagesToScan() default {};

    /**
     * Explictely specify vendor to be used for beans creation. If it is unset, then
     * we'll try to guess the Jakarta Data vendor by ourselves. If this property is set,
     * then we'll search only for this exact provider. If we fail, then the exception is raised
     */
    JakartaDataVendor jakartaDataVendor() default JakartaDataVendor.UNSPECIFIED;
}
