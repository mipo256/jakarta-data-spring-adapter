package io.mpolivaha.jdsa;

import io.mpolivaha.jdsa.core.Configuration;
import io.mpolivaha.jdsa.utils.Lazy;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

/**
 * The specific provider of the Jakarta Data API.
 *
 * @author Mikhail Polivakha
 */
public enum JakartaDataVendor {

    // TODO: add discovery functions for other providers
    HIBERNATE(
            Lazy.of(() -> ClassLoadingUtils.isPresent("org.hibernate.StatelessSession")),
            (repository, context) -> {
                Class<?> implClass = ClassLoadingUtils.tryLoading(repository.getName() + "_");
                jakarta.persistence.EntityManagerFactory entityManagerFactory;

                String sessionFactoryBeanRef = Configuration.getInstance().getSessionFactoryBeanRef();
                if (StringUtils.hasText(sessionFactoryBeanRef)) {
                    entityManagerFactory = context.getBean(sessionFactoryBeanRef, jakarta.persistence.EntityManagerFactory.class);
                } else {
                    entityManagerFactory = context.getBean(jakarta.persistence.EntityManagerFactory.class);
                }

                org.hibernate.SessionFactory sessionFactory = entityManagerFactory.unwrap(org.hibernate.SessionFactory.class);
                org.hibernate.StatelessSession statelessSession = sessionFactory.openStatelessSession();

                try {
                    return implClass.getDeclaredConstructor(org.hibernate.SessionFactory.class).newInstance(statelessSession);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
    ),
    OPEN_LIBERTY(Lazy.of(() -> false), (repository, context) -> null),
    JNOSQL(Lazy.of(() -> false), (repository, context) -> null),

    /**
     * Stub in order to account for that fact that in Java the annotation attributes cannot be {@code null}
     */
    UNSPECIFIED(null, null);

    private final Lazy<Boolean> discoveryFunction;
    private final BiFunction<Class<?>, ApplicationContext, ?> implementationExtractor;

    JakartaDataVendor(Lazy<Boolean> discoveryFunction,
                      BiFunction<Class<?>, ApplicationContext, ?> implementationExtractor) {
        this.discoveryFunction = discoveryFunction;
        this.implementationExtractor = implementationExtractor;
    }

    public boolean isPresent() {
        return this.discoveryFunction.get();
    }

    public Object createImplementation(Class<?> repositoryInterface, ApplicationContext applicationContext) {
        return implementationExtractor.apply(repositoryInterface, applicationContext);
    }

    public static JakartaDataVendor[] explicitValues() {
        return new JakartaDataVendor[] {
                JakartaDataVendor.HIBERNATE,
                JakartaDataVendor.JNOSQL,
                JakartaDataVendor.OPEN_LIBERTY
        };
    }
}
