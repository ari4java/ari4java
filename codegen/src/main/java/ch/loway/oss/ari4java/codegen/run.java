
package ch.loway.oss.ari4java.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * $Id$
 *
 * @author lenz
 */
public class run {

    private static String SOURCES = "./src/main/resources/codegen-data/";
    private static String OUTPUT = "../src/main/generated/";

    public static void main(String[] argv) throws IOException {
        System.out.println("This is ARI4JAVA Code Generator version " + VERSION.VER);

        DefMapper dm = new DefMapper();
        dm.setOutputFolder(OUTPUT);
        dm.clean();

        Files.list(Paths.get(SOURCES))
                .filter(p -> p.toFile().isDirectory())
                .sorted((p1, p2) -> {
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
                })
                .forEach(p -> loadAsteriskDefs(dm, p.toFile()));

        dm.generateAllClasses();
//        System.out.println("Finished Generating");

    }

    private static void loadAsteriskDefs(DefMapper dm, File folder) {
        try {
            String srcVer = folder.getName();
//            System.out.println("Loading: " + folder.getAbsolutePath());
            for (File definition : folder.listFiles()) {
                if (definition.getName().endsWith(".json")) {
                    dm.parseJsonDefinition(definition, srcVer);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

}

// $Log$
//
