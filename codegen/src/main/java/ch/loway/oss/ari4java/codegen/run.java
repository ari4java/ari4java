
package ch.loway.oss.ari4java.codegen;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * $Id$
 *
 * @author lenz
 */
public class run {

    public static void main(String[] argv) throws Exception {
        System.out.println("This is ARI4JAVA Code Generator version " + VERSION.VER);
        String sourceFolder = "./src/main/resources/codegen-data/";
        String outputFolder = "../src/main/generated/";
        DefMapper dm = new DefMapper();
        dm.setOutputFolder(outputFolder);
        dm.clean();
        Files.list(Paths.get(sourceFolder));
        try (Stream<Path> list = Files.list(Paths.get(sourceFolder))) {
            list.filter(p -> p.toFile().isDirectory()).sorted((p1, p2) -> {
                // break up folder into version parts and sort as numbers
                String[] items1 = p1.getFileName().toString().split("_");
                String[] items2 = p2.getFileName().toString().split("_");
                if (items1.length == 4 && items2.length == 4) {
                    if (items1[1].equals(items2[1])) {
                        if (items1[2].equals(items2[2])) {
                            return Integer.valueOf(items1[3]).compareTo(Integer.valueOf(items2[3]));
                        }
                        return Integer.valueOf(items1[2]).compareTo(Integer.valueOf(items2[2]));
                    }
                    return Integer.valueOf(items1[1]).compareTo(Integer.valueOf(items2[1]));
                }
                return p1.compareTo(p2);
            }).forEach(p -> loadAsteriskDefs(dm, p.toFile()));
        }
        dm.generateAllClasses();
    }

    private static void loadAsteriskDefs(DefMapper dm, File folder) {
        try {
            String srcVer = folder.getName();
            File[] files = folder.listFiles();
            if (files != null) {
                for (File definition : files) {
                    if (definition.getName().endsWith(".json")) {
                        dm.parseJsonDefinition(definition, srcVer);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

}

