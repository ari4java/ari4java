
package ch.loway.oss.ari4java.codegen.genJava;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author lenz
 */
public class JavaInterface {

    public String pkgName = "";
    public String className = "";

    Map<String,String> definitions = new HashMap<String, String>();

    public void iKnow( String signature, String method, String comment ) {

        StringBuilder sb = new StringBuilder();
        JavaGen.addBanner(sb, comment);
        sb.append( method ).append(";\n\n");

        definitions.put( signature, sb.toString() );
    }






    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        JavaGen.importClasses(sb, pkgName, Arrays.asList( new String[] {
            "java.util.Date",
            "java.util.List",
            "java.util.ArrayList",
            "ch.loway.oss.ari4java.tools.RestException",
            "ch.loway.oss.ari4java.tools.AriCallback"
        }));


        sb.append( "public interface ").append(className).append(" {\n");

        for ( String signature: definitions.keySet() ) {
            sb.append( "\n// ").append( signature ).append("\n");
            sb.append( definitions.get( signature) );
            sb.append( "\n" );
        }


        sb.append( "}\n;");




        return sb.toString();



    }


    

}

// $Log$
//
