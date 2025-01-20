package io.mpolivaha.jdsa;

import jakarta.data.repository.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

public class ClassPathExplorer {

    private static final Logger LOG = Logger.getLogger(ClassPathExplorer.class.getName());

    public static final String SIGN_ERROR = "The JAR file %s has either manifest or byte code classes signed. Either the signature is incorrect, or it is no longer valid";
    private static final String DOT_JAR = ".jar";

    private final JarTraverser jarTraverser;

    public ClassPathExplorer(JarTraverser jarTraverser) {
        this.jarTraverser = jarTraverser;
    }

    public static void main(String[] args) {
        System.out.println(new ClassPathExplorer(new JarTraverser()).findAllClassesInClasspath(clazz -> clazz.getAnnotation(Repository.class) != null));
    }

    public Set<Class<?>> findAllClassesInClasspath(Predicate<Class<?>> predicate) {
        String classpath = System.getProperty("java.class.path");
        String[] classPathResources = classpath.split(File.pathSeparator);

        Set<Class<?>> classes = new HashSet<>();

        for (String classPathResource : classPathResources) {
            Path classPathEntry = Paths.get(classPathResource);

            if (classPathEntry.getFileName().endsWith(DOT_JAR)) {
                if (Files.exists(classPathEntry)) {
                    if (!Files.isDirectory(classPathEntry)) {
                        classes.addAll(exploreClassPathEntry(classPathEntry, predicate));
                    }
                } else {
                    LOG.warning("The classpath entry %s cannot be scanned since it does not exists".formatted(classPathResource));
                }
            }
        }

        try (var currentJarStream = new JarInputStream(new FileInputStream(getExecutableJarLocation().toFile()))) {

            JarEntry entry;

            while ((entry = currentJarStream.getNextJarEntry()) != null) {
                if (isJavaByteCodeFile(entry.getName())) {

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    private static Path getExecutableJarLocation() {
        return Paths.get(ClassPathExplorer.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath());
    }

    private Set<Class<?>> exploreClassPathEntry(
            Path classPathEntry,
            Predicate<Class<?>> predicate
    ) {
        Set<Class<?>> loadedClasses = new HashSet<>();

        jarTraverser.traverse(classPathEntry, aClass -> {
            if (aClass.getAnnotation(Repository.class) != null) {
                loadedClasses.add(aClass);
            }
        });

        try (var source = new JarInputStream(Files.newInputStream(classPathEntry))) {
            JarEntry jarEntry;

            while ((jarEntry = source.getNextJarEntry()) != null) {
                if (isJavaByteCodeFile(jarEntry)) {

                    // Files.pathSeparator is not used here the defined path separator for JAR files is '/' and it is not platform dependent.
                    String candidateClassName = jarEntry.getName().replace('/', '.');
                    candidateClassName = candidateClassName.substring(0, candidateClassName.lastIndexOf('.'));
                    Class<?> maybeClass = tryLoading(candidateClassName);

                    if (maybeClass != null && predicate.test(maybeClass)) {
                        loadedClasses.add(maybeClass);
                    }
                }
            }

            return loadedClasses;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            if (Configuration.getInstance().isFailFast()) {
                throw new JarFilesIterationException(SIGN_ERROR.formatted(classPathEntry.getFileName()), e);
            } else {
                LOG.warning(SIGN_ERROR.formatted(classPathEntry.getFileName()));
            }
        }
        return loadedClasses;
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
