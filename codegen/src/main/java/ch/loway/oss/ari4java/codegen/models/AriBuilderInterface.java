
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The interface AriBuilder - it builds ARI objects of the required type.
 *
 * @author lenz
 */
public class AriBuilderInterface {

    public List<String> knownInterfaces = new ArrayList<String>();

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        JavaGen.importClasses(sb, "ch.loway.oss.ari4java.generated",
                Arrays.asList(new String[]{
                        "ch.loway.oss.ari4java.ARI",
                        "ch.loway.oss.ari4java.generated.actions.*"
                })
        );

        sb.append("public interface AriBuilder {\n");

        Collections.sort(knownInterfaces);

        for (String iface : knownInterfaces) {
            sb.append("  public abstract ").append(iface)
                    .append(" ").append(lcFirst(iface)).append("();\n");
        }

        sb.append("\n\n"
                + "  public abstract ARI.ClassFactory getClassFactory();\n\n");


        sb.append("\n}\n");

        return sb.toString();

    }

    /**
     * Rende minuscolo il primo carattere.
     *
     * @param s
     * @return
     */
    private static String lcFirst(String s) {
        if (s.length() > 1) {
            String s1 = s.substring(0, 1);
            String s2 = s.substring(1);
            return s1.toLowerCase() + s2;
        } else {
            return s;
        }
    }

    public static String getMethod(String ifName, String apiVer) {
        String s = "public " + ifName + " " + lcFirst(ifName) + "() {\n"
                + "  return new " + ifName + "_impl_" + apiVer + "();\n };\n\n";
        return s;
    }

    public static String getUnimplemented(String ifName) {
        String s = "public " + ifName + " " + lcFirst(ifName) + "() {\n"
                + "  throw new UnsupportedOperationException();\n };\n\n";
        return s;
    }

}

// $Log$
//
