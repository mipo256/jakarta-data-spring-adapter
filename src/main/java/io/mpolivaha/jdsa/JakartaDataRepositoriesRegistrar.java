package io.mpolivaha.jdsa;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Map;

@Configuration
public class JakartaDataRepositoriesRegistrar implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
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

            io.mpolivaha.jdsa.Configuration.buildFrom(annotation);
        } else {
            StringBuilder duplicates = buildDuplicatePairs(beansWithAnnotation);
            throw new IllegalStateException(
                    "The application context contains % beans annotated with @EnableJakartaDataRepositories : " + duplicates);
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
