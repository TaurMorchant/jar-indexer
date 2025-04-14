package org.qubership.tools.jarindexer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Usage: java -jar jar-indexer.jar <path>");
            System.exit(1);
        }
        String dir = args[0];
        ClassesIndex index = processDir(dir);
        index.writeFoFile("index.txt");
    }

    public static ClassesIndex processDir(String dir) {
        ClassesIndex index = new ClassesIndex();

        Path dirPath = Paths.get(dir);

        try (Stream<Path> paths = Files.walk(dirPath)) {

            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".jar"))
                    .filter(p -> !p.toString().endsWith("sources.jar"))
                    .forEach(index::addClassesFromJar);
        } catch (IOException e) {
            log.error("Error while processing files in dir {}", dir);
        }
        return index;
    }

}
