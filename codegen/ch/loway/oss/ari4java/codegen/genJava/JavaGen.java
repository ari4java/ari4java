
package ch.loway.oss.ari4java.codegen.genJava;

import java.util.List;

/**
 * A set of static methods to geenrate Java sources.
 *
 * @author lenz
 */
public class JavaGen {

    public static void importClasses( StringBuilder sb, String myPackage, List<String> imports ) {

        sb.append( "package " ).append(myPackage).append( ";\n\n");

        for ( String pkg: imports ) {
            sb.append( "import " ).append( pkg ).append(";\n");
        }
        sb.append( "\n" );

    }

    public static void addBanner( StringBuilder sb, String multiLineBanner ) {

        String[] rows = multiLineBanner.split("\n");

        sb.append( "/*** ====================================================\n");

        for (String row: rows ) {
            sb.append( " * " ).append(row).append( "\n");
        }
        sb.append( " * ================================================= */\n");

    }


}

// $Log$
//
