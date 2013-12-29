
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaInterface;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author lenz
 */
public class Action {

    public String path = "";
    public String description = "";
    public List<Operation> operations = new ArrayList<Operation>();
    public String javaFile = "";

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for ( Operation o: operations ) {
            sb.append( o.toJava(this) );
        }

        return sb.toString();

    }

    void registerInterfaces(JavaInterface j) {
        for ( Operation o: operations ) {
            String javaSignature = o.getSignature();
            String definition = o.getDefinition();

            j.iKnow(javaSignature, definition, o.description );
            j.iKnow(o.getSignatureAsync(), o.getDefinitionAsync(), "");
        }
    }




}

