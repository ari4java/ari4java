
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import ch.loway.oss.ari4java.codegen.genJava.JavaInterface;
import ch.loway.oss.ari4java.codegen.genJava.JavaPkgInfo;
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
public class Model extends JavaPkgInfo {

    public String description = "";
    public String extendsModel = "";
    public String comesFromFile = "";
    public List<String> implementsInterafaces = new ArrayList<String>();
    
    public List<String> imports = new ArrayList<String>();
    public List<ModelField> fields = new ArrayList<ModelField>();

    public Model() {
        imports.add( "java.util.Date" );
        imports.add( "java.util.List" );
        imports.add( "ch.loway.oss.ari4java.generated.*");
    }



    @Override
    public String toString() {

        Collections.sort(imports);
        Collections.sort( fields );
        Collections.sort( implementsInterafaces );


        StringBuilder sb = new StringBuilder();

        JavaGen.importClasses(sb, getModelPackage(), imports);

        JavaGen.addBanner(sb, description + "\n\n"  + "Defined in file :" + comesFromFile );


        sb.append(  "public class " ).append(  getImplName() );

        // concrete implementation for the model to be extended
        if ( extendsModel.length() > 0 ) {

            JavaPkgInfo jpi = new JavaPkgInfo();
            jpi.setPackageInfo(extendsModel, apiVersion);

            sb.append( " extends " ).append( jpi.getImplName() );
        }

        sb.append( " implements " );

            
        for ( String inf: implementsInterafaces ) {
            sb.append( inf ).append( ", " );
        }

        sb.append( getInterfaceName() ).append( ", ");
        sb.append( "java.io.Serializable {\n" );

        for ( ModelField mf: fields) {
            sb.append( mf.toString() );
        }

        sb.append( "}\n" );
        return sb.toString();


    }


    public void registerInterfaces( JavaInterface j ) {

        for ( ModelField mf: fields) {
            String signature = mf.getSignatureGet();
            String declaration = mf.getDeclarationGet();
            String comment = mf.comment;

            j.iKnow(signature, declaration, comment);
        }

        for ( ModelField mf: fields) {
            String signature = mf.getSignatureSet();
            String declaration = mf.getDeclarationSet();
            String comment = mf.comment;

            j.iKnow(signature, declaration, comment);
        }





    }


}

// $Log$
//
