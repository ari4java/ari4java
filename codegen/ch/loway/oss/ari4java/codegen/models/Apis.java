
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import ch.loway.oss.ari4java.codegen.genJava.JavaInterface;
import ch.loway.oss.ari4java.codegen.genJava.JavaPkgInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class Apis extends JavaPkgInfo {
    
    public List<Action> actions = new ArrayList<Action>();

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        JavaGen.importClasses(sb, getActionsPackage(), Arrays.asList( new String[] {
            "ch.loway.oss.ari4java.generated.*",
            "java.util.Date",
            "java.util.List",
            "java.util.ArrayList",
            "ch.loway.oss.ari4java.tools.BaseAriAction",
            "ch.loway.oss.ari4java.tools.RestException",
            "com.fasterxml.jackson.core.type.TypeReference",
            getModelPackage() + ".*"
        } ));


        sb.append( "public class " ).append( getImplName() )
                .append(" extends BaseAriAction ")
                .append(" implements ")
                .append( getInterfaceName() )
                .append( " {\n" );

        for ( Action a: actions ) {
            sb.append( a.toString() );
        }

        sb.append( "};\n");
        return sb.toString();

    }

    public void registerInterfaces(JavaInterface j) {
        for ( Action a: actions ) {
            a.registerInterfaces( j );
        }
    }




}

// $Log$
//
