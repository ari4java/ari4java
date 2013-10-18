
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaInterface;
import ch.loway.oss.ari4java.codegen.genJava.JavaPkgInfo;
import java.util.ArrayList;
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

        sb.append( "package " ).append( getActionsPackage() ).append( ";\n");
        sb.append( "import ch.loway.oss.ari4java.generated.*;\n");        
        sb.append( "import ").append( getModelPackage() ).append( ".*;\n");
        sb.append( "import java.util.Date;\n");
        sb.append( "import java.util.List;\n");
        sb.append( "import java.util.ArrayList;\n");
        sb.append( "import ch.loway.oss.ari4java.tools.*;\n");

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
