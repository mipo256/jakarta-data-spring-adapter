package io.mpolivaha.jdsa;

/**
 * Utility class for class loading operations
 *
 * @author Mikhail Polivakha
 */
public class ClassLoadingUtils {

    public static Class<?> tryLoading(String name) {
        try {
            return Class.forName(name);
        } catch (Exception o_0) {
            return null;
        } catch (NoClassDefFoundError | ExceptionInInitializerError error) {
            // might happen in case some class was present in compilation classpath but is absent in runtime classpath
            return null;
        }
    }

    /**
     * @param fqcn fully qualified class name as a {@link String}
     * @return {@code true} if the {@link Class} with given FQN present, {@code false} otherwise
     */
    public static Boolean isPresent(String fqcn) {
        return tryLoading(fqcn) != null;
    }
}
