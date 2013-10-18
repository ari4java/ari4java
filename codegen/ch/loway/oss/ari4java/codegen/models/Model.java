
package ch.loway.oss.ari4java.codegen.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class Model {

    public String pkgName = "ch.loway.oss.ari4java.generated";
    public String className = "";
    public String description = "";
    public String extendsModel = "";
    public String comesFromFile = "";
    public List<String> implementsInterafaces = new ArrayList<String>();
    
    public List<String> imports = new ArrayList<String>();
    public List<ModelField> fileds = new ArrayList<ModelField>();

    public Model() {
        imports.add( "java.util.Date" );
        imports.add( "java.util.List" );
    }



    @Override
    public String toString() {

        Collections.sort(imports);
        Collections.sort( fileds );
        Collections.sort( implementsInterafaces );


        StringBuilder sb = new StringBuilder();

        sb.append( "package " ).append( pkgName ).append( ";\n\n" );



        for ( String myImport: imports ) {
            sb.append( "import ").append( myImport ).append(";\n");
        }

        sb.append( "\n/** ----------------------------------------------------\n")
          .append( "  * " ).append( description ).append( "\n")
          .append( "  * Defined in file: " ).append( comesFromFile ).append( "\n")
          .append( "  * ------------------------------------------------- */\n\n");


        sb.append(  "public class " ).append(  className );

        if ( extendsModel.length() > 0 ) {
            sb.append( " extends " ).append( extendsModel );
        }

        sb.append( " implements " );

            
        for ( String inf: implementsInterafaces ) {
            sb.append( inf ).append( ", " );
        }

        sb.append( "java.io.Serializable {\n" );

        for ( ModelField mf: fileds) {
            sb.append( mf.toString() );
        }

        sb.append( "}\n" );
        return sb.toString();


    }


    public void toFile( String baseJavaClasses ) throws IOException {

        String fName = baseJavaClasses 
                     + pkgName.replace(".", "/" )
                     + "/"
                     + className + ".java";
        
        FileWriter outFile = new FileWriter( fName );
        PrintWriter out = new PrintWriter(outFile);
        out.println( toString() );
        out.close();

    }



}

// $Log$
//
