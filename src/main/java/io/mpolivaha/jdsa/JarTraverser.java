package io.mpolivaha.jdsa;

import io.mpolivaha.jdsa.core.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Class that is responsible to traverse all the classes in the given Jar.
 *
 * @author Mikhail Polivakha
 */
public class JarTraverser {

    private static final Logger LOG = Logger.getLogger(JarTraverser.class.getName());
    public static final String SIGN_ERROR = "The JAR file %s has either manifest or byte code classes signed. Either the signature is incorrect, or it is no longer valid";
    private static final String DOT_CLASS = ".class";

    public void traverseDirectory(Path location, Consumer<Class<?>> action) throws IOException {
        try (Stream<Path> walk = Files.walk(location, FileVisitOption.FOLLOW_LINKS)) {
            walk.forEach(path -> {
                if (isJavaByteCodeFile(path)) {
                    String relativeClassPath = path.toFile().getAbsolutePath().substring(location.toFile().getAbsolutePath().length() + File.pathSeparator.length());
                    String classWithTail = relativeClassPath.replace(File.separatorChar, '.');
                    String fqcn = classWithTail.substring(0, classWithTail.lastIndexOf(DOT_CLASS));

                    Class<?> clazz = ClassLoadingUtils.tryLoading(fqcn);
                    if (clazz != null) {
                        action.accept(clazz);
                    }
                }
            });
        }
    }

    public void traverseJar(Path jarLocation, Consumer<Class<?>> action) {
        try (var source = new JarInputStream(Files.newInputStream(jarLocation))) {
            JarEntry jarEntry;

            while ((jarEntry = source.getNextJarEntry()) != null) {
                if (isJavaByteCodeFile(jarEntry)) {

                    // Files.pathSeparator is not used here the defined path separator for JAR files is '/' and it is not platform dependent.
                    String candidateClassName = jarEntry.getName().replace('/', '.');
                    candidateClassName = candidateClassName.substring(0, candidateClassName.lastIndexOf('.'));
                    Class<?> maybeClass = ClassLoadingUtils.tryLoading(candidateClassName);

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

    private static boolean isJavaByteCodeFile(JarEntry jarEntry) {
        return !jarEntry.isDirectory() && jarEntry.getName().endsWith(".class");
    }

    private static boolean isJavaByteCodeFile(Path path) {
        return !path.toFile().isDirectory() && path.toFile().getAbsolutePath().endsWith(".class");
    }
}

