package io.mpolivaha.jdsa.core;

import io.mpolivaha.jdsa.JakartaDataVendor;
import io.mpolivaha.jdsa.annotations.EnableJakartaDataRepositories;
import io.mpolivaha.jdsa.annotations.HibernateJakartaDataConfiguration;

/**
 * Class that holds common configuration provided by user via {@link EnableJakartaDataRepositories}.
 *
 * @author Mikhail Polivakha
 */
public class Configuration {

    private static Configuration INSTANCE;

    private boolean failFast;
    private JakartaDataVendor jakartaDataVendor;
    private String sessionFactoryBeanRef;

    /**
     * The double-checked locking is intentionally not used here
     * we assume that the only invocation that is possible is coming
     * from the {@link JakartaDataRepositoriesRegistrar}
     */
    protected static void buildFrom(EnableJakartaDataRepositories enableJakartaDataRepositories) {
        INSTANCE = new Configuration();
        INSTANCE.failFast = enableJakartaDataRepositories.failFast();
        INSTANCE.jakartaDataVendor = enableJakartaDataRepositories.jakartaDataVendor();

        HibernateJakartaDataConfiguration config = enableJakartaDataRepositories.hibernateConfig();
        INSTANCE.sessionFactoryBeanRef = config.sessionFactoryRef();
    }

    public static Configuration getInstance() {
        return INSTANCE;
    }

    public JakartaDataVendor jakartaDataVendor() {
        return jakartaDataVendor;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public String getSessionFactoryBeanRef() {
        return sessionFactoryBeanRef;
    }
}
