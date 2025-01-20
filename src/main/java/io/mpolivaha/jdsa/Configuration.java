package io.mpolivaha.jdsa;

public class Configuration {

    private static Configuration INSTANCE;

    private boolean failFast;

    /**
     * The double-checked locking is intentionally not used here
     * we assume that the only invocation that is possible is coming
     * from the {@link JakartaDataRepositoriesRegistrar}
     */
    protected static void buildFrom(EnableJakartaDataRepositories enableJakartaDataRepositories) {
        INSTANCE = new Configuration();
        INSTANCE.failFast = enableJakartaDataRepositories.failFast();
    }

    public static Configuration getInstance() {
        return INSTANCE;
    }

    public boolean isFailFast() {
        return failFast;
    }
}
