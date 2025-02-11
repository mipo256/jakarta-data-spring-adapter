package io.mpolivaha.jdsa;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * The explorer of the application's runtime classpath. Includes the exploration
 * of the current application classes.
 *
 * @author Mikhail Polivakha
 */
public class ClassPathExplorer {

    private static final Logger LOG = Logger.getLogger(ClassPathExplorer.class.getName());
    private static final String DOT_JAR = ".jar";

    private final JarTraverser jarTraverser;
    private final FileSystemTraverser fileSystemTraverser;

    public ClassPathExplorer(JarTraverser jarTraverser) {
        this.jarTraverser = jarTraverser;
        this.fileSystemTraverser = new FileSystemTraverser();
    }

    public Set<Class<?>> findAllClassesInClasspath(Predicate<Class<?>> predicate) throws URISyntaxException, IOException {
        String classpath = System.getProperty("java.class.path");
        String[] classPathResources = classpath.split(File.pathSeparator);

        Set<Class<?>> classes = new HashSet<>();

        for (String classPathResource : classPathResources) {
            Path classPathEntry = Paths.get(classPathResource);

             if (Files.exists(classPathEntry)) {
                 if (classPathEntry.toFile().getAbsolutePath().endsWith(DOT_JAR)) {
                     if (!Files.isDirectory(classPathEntry)) {
                         classes.addAll(exploreClassPathEntry(classPathEntry, predicate));
                     }
                 } else {
                     fileSystemTraverser.traverse(classPathEntry, aClass -> {
                         if (predicate.test(aClass)) {
                             classes.add(aClass);
                         }
                     });
                 }
            } else {
                 LOG.warning("The classpath entry %s cannot be scanned since it does not exists".formatted(classPathResource));
            }
        }
        
        return classes;
    }

    private Set<Class<?>> exploreClassPathEntry(
            Path classPathEntry,
            Predicate<Class<?>> predicate
    ) {
        Set<Class<?>> loadedClasses = new HashSet<>();

        jarTraverser.traverseJar(classPathEntry, clazz -> {
            if (predicate.test(clazz)) {
                loadedClasses.add(clazz);
            }
        });

        return loadedClasses;
    }
}
