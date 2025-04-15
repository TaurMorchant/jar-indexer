package org.qubership.tools.jarindexer;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Usage: java -jar jar-indexer.jar <path>");
            System.exit(1);
        }
        String dir = args[0];
        ClassesIndex index = calculateClassesIndex(dir);
        writeToFile(index, "classesIndex.txt");
        List<String> packageIndex = calculatePackageIndex(index);
        writeToFile(packageIndex, "packagesIndex.txt");
    }

    public static ClassesIndex calculateClassesIndex(String dir) {
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

    private static List<String> calculatePackageIndex(ClassesIndex index) {
        Set<String> result = new HashSet<>();
        for (String clazz : index) {
            int lastDotIndex = clazz.lastIndexOf('.');
            if (lastDotIndex == -1) {
                continue;
            }

            result.add(clazz.substring(0, lastDotIndex));
        }

        return result.stream().sorted().toList();
    }

    public static void writeToFile(Collection<String> collection, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : collection) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Error during write file");
        }
    }
}
