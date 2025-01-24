package io.mpolivaha.jdsa.core;

import io.mpolivaha.jdsa.JakartaDataVendor;
import io.mpolivaha.jdsa.annotations.EnableJakartaDataRepositories;

public class Configuration {

    private static Configuration INSTANCE;

    private boolean failFast;
    private JakartaDataVendor jakartaDataVendor;

    /**
     * The double-checked locking is intentionally not used here
     * we assume that the only invocation that is possible is coming
     * from the {@link JakartaDataRepositoriesRegistrar}
     */
    protected static void buildFrom(EnableJakartaDataRepositories enableJakartaDataRepositories) {
        INSTANCE = new Configuration();
        INSTANCE.failFast = enableJakartaDataRepositories.failFast();
        INSTANCE.jakartaDataVendor = enableJakartaDataRepositories.jakartaDataVendor();
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
}
