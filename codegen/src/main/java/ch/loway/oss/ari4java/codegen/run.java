
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

        Files.list(Paths.get(SOURCES))
                .filter(p -> p.toFile().isDirectory())
                .sorted()
                .forEach(p -> loadAsteriskDefs(dm, p.toFile()));

        dm.generateAllClasses();

    }

    private static void loadAsteriskDefs(DefMapper dm, File folder) {
        try {
            String srcVer = folder.getName();
            System.out.println("Loading: " + folder.getAbsolutePath());
            dm.clean(srcVer);
            for (File definition : folder.listFiles()) {
                if (definition.getName().endsWith(".json")) {
                    dm.parseJsonDefinition(definition, srcVer, "events.json".equals(definition.getName()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

}

// $Log$
//
