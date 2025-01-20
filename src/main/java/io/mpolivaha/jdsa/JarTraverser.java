package io.mpolivaha.jdsa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

/**
 *
 */
public class JarTraverser {

    private static final Logger LOG = Logger.getLogger(JarTraverser.class.getName());
    public static final String SIGN_ERROR = "The JAR file %s has either manifest or byte code classes signed. Either the signature is incorrect, or it is no longer valid";

    public void traverse(Path jarLocation, Consumer<Class<?>> action) {
        try (var source = new JarInputStream(Files.newInputStream(jarLocation))) {
            JarEntry jarEntry;

            while ((jarEntry = source.getNextJarEntry()) != null) {
                if (isJavaByteCodeFile(jarEntry)) {

                    // Files.pathSeparator is not used here the defined path separator for JAR files is '/' and it is not platform dependent.
                    String candidateClassName = jarEntry.getName().replace('/', '.');
                    candidateClassName = candidateClassName.substring(0, candidateClassName.lastIndexOf('.'));
                    Class<?> maybeClass = tryLoading(candidateClassName);

                    if (maybeClass != null) {
                        action.accept(maybeClass);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            if (Configuration.getInstance().isFailFast()) {
                throw new JarFilesIterationException(SIGN_ERROR.formatted(jarLocation.getFileName()), e);
            } else {
                LOG.warning(SIGN_ERROR.formatted(jarLocation.getFileName()));
            }
        }
    }

    private Class<?> tryLoading(String name) {
        try {
            return Class.forName(name);
        } catch (Exception o_0) {
            return null;
        } catch (NoClassDefFoundError | ExceptionInInitializerError error) {
            // might happen in case some class was present in compilation classpath but is absent in runtime classpath
            return null;
        }
    }

    private static boolean isJavaByteCodeFile(JarEntry jarEntry) {
        return !jarEntry.isDirectory() && jarEntry.getName().endsWith(".class");
    }
}

