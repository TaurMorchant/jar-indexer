package org.qubership.tools.jarindexer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


@Slf4j
public class ClassesIndex extends ArrayList<String> {
    public void addClassesFromJar(Path filePath) {
        log.info("Process jar: {}", filePath.toString());
        try (JarFile jarFile = new JarFile(filePath.toFile())) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.endsWith(".class") && !name.startsWith("META-INF/") && !name.contains("$")) {
                    String className = name.replace('/', '.').replaceAll("\\.class$", "");
                    add(className);
                }
            }
        } catch (IOException e) {
            log.error("Cannot read JAR file: {}", e.getMessage());
        }
    }
}
