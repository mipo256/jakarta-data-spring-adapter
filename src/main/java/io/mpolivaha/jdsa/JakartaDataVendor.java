package io.mpolivaha.jdsa;

import io.mpolivaha.jdsa.utils.Lazy;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.context.ApplicationContext;

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
                EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
                SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
                StatelessSession statelessSession = sessionFactory.openStatelessSession();

                try {
                    return implClass.getDeclaredConstructor(StatelessSession.class).newInstance(statelessSession);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
    ),
    OPEN_LIBERTY(Lazy.of(() -> false), (repository, context) -> null),
    JNOSQL(Lazy.of(() -> false), (repository, context) -> null);

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
}
