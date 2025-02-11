package io.mpolivaha.jdsa;

import io.mpolivaha.jdsa.utils.ClassLoadingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileSystemTraverser {

    public void traverse(Path root, Consumer<Class<?>> action) {
        try (var stream = Files.walk(root, FileVisitOption.FOLLOW_LINKS)) {
            stream.forEach(path -> {
                String absolutePath = path.toFile().getAbsolutePath();

                if (!path.toFile().isDirectory() && absolutePath.endsWith(".class")) {
                    String classNameSystemDelimited = absolutePath.subSequence(
                            root.toFile().getAbsolutePath().length() + File.separator.length(),
                            absolutePath.length()
                    ).toString();

                    String withDotClass = classNameSystemDelimited.replace(File.separator, ".");

                    String fqcn = withDotClass.substring(0, withDotClass.lastIndexOf("."));

                    Class<?> candidate = ClassLoadingUtils.tryLoading(fqcn);

                    if (candidate != null) {
                        action.accept(candidate);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
