
package ch.loway.oss.ari4java.codegen.genJava;

import java.util.Date;
import java.util.List;

/**
 * A set of static methods to generate Java sources.
 *
 * @author lenz
 */
public class JavaGen {

    public static void addPackage(StringBuilder sb, String myPackage) {
        sb.append("package ").append(myPackage).append(";\n\n");
        sb.append("// ----------------------------------------------------\n")
                .append("//      THIS CLASS WAS GENERATED AUTOMATICALLY         \n")
                .append("//               PLEASE DO NOT EDIT                    \n")
                .append("//    Generated on: ").append((new Date()).toString()).append("\n")
                .append("// ----------------------------------------------------\n\n");

    }

    public static void importClasses(StringBuilder sb, String myPackage, List<String> imports) {

        addPackage(sb, myPackage);
        for (String pkg : imports) {
            sb.append("import ").append(pkg).append(";\n");
        }
        sb.append("\n");

    }

    public static void addBanner(StringBuilder sb, String multiLineBanner) {

        String[] rows = multiLineBanner.split("\n");
        sb.append("/**\n");
        for (String row : rows) {
            if (!row.isEmpty()) {
                row = row.replaceAll("<br /><br />", "\n * ");
                row = row.replaceAll("<br />", "\n * ");
                sb.append(" * ").append(row).append("\n");
            }
        }
        sb.append(" */\n");

    }

    public static void addBanner(StringBuilder sb, String multilineBanner, String sinceVersion) {
        multilineBanner += "\n@since " + sinceVersion;
        addBanner(sb, multilineBanner);
    }

    public static String addPrefixAndCapitalize(String prefix, String field) {

        return prefix + field.substring(0, 1).toUpperCase() + field.substring(1);


    }

    public static String addAsyncCallback(String response) {
        return "AriCallback<" + response.replaceAll("^void$", "Void") + "> callback";
    }

    public static void emptyLines(StringBuilder sb, int nLines) {
        for (int i = 0; i < nLines; i++) {
            sb.append("\n");
        }
    }

    public static void emptyLine(StringBuilder sb) {
        emptyLines(sb, 1);
    }


}

// $Log$
//
