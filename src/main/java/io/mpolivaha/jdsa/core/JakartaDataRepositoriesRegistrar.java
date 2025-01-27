package io.mpolivaha.jdsa.core;

import io.mpolivaha.jdsa.ClassPathExplorer;
import io.mpolivaha.jdsa.JakartaDataVendor;
import io.mpolivaha.jdsa.JarTraverser;
import io.mpolivaha.jdsa.annotations.EnableJakartaDataRepositories;
import io.mpolivaha.jdsa.discovery.ArtifactsBasedJakartaDataVendorDiscoverer;
import io.mpolivaha.jdsa.discovery.JakartaDataVendorDiscoverer;
import jakarta.data.repository.Repository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

/**
 * The main component that is responsible for registration Jakarta Data repositories as beans
 * in the context.
 *
 * @author Mikhail Polivakha
 */
@Configuration
public class JakartaDataRepositoriesRegistrar implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final ClassPathExplorer classPathExplorer;
    private final JakartaDataVendorDiscoverer jakartaDataVendorDiscoverer;

    public JakartaDataRepositoriesRegistrar() {
        this.classPathExplorer = new ClassPathExplorer(new JarTraverser());
        this.jakartaDataVendorDiscoverer = new ArtifactsBasedJakartaDataVendorDiscoverer();
    }

    @Override
    public void afterPropertiesSet() throws URISyntaxException, IOException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableJakartaDataRepositories.class);

        if (beansWithAnnotation.size() == 1) {
            Object bean = beansWithAnnotation.values().iterator().next();
            EnableJakartaDataRepositories annotation = AnnotationUtils.getAnnotation(
                    ClassUtils.getUserClass(bean.getClass()), // might already be proxied
                    EnableJakartaDataRepositories.class
            );

            Assert.notNull(
                    annotation,
                    "We did not manage to find an @EnableJakartaDataRepositories. That typically should not happen. Please, report to developers"
            );

            io.mpolivaha.jdsa.core.Configuration.buildFrom(annotation);
        } else {
            StringBuilder duplicates = buildDuplicatePairs(beansWithAnnotation);
            throw new IllegalStateException(
                    "The application context contains % beans annotated with @EnableJakartaDataRepositories : " + duplicates);
        }

        JakartaDataVendor jakartaDataVendor = jakartaDataVendorDiscoverer.discover();

        ConfigurableListableBeanFactory autowireCapableBeanFactory = (ConfigurableListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        Set<Class<?>> repositoryCandidates = classPathExplorer.findAllClassesInClasspath(aClass -> aClass.getAnnotation(Repository.class) != null);

        for (Class<?> repositoryCandidate : repositoryCandidates) {
            Object implementation = jakartaDataVendor.createImplementation(repositoryCandidate, applicationContext);
            autowireCapableBeanFactory.registerSingleton(repositoryCandidate.getSimpleName(), implementation);
        }
    }

    private static StringBuilder buildDuplicatePairs(Map<String, Object> beansWithAnnotation) {
        StringBuilder duplicates = new StringBuilder();
        for (String s : beansWithAnnotation.keySet()) {
            duplicates.append("\n - %s".formatted(s));
        }
        return duplicates;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
