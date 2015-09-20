
package ch.loway.oss.ari4java.codegen2.writeClass;

import ch.loway.oss.ari4java.codegen2.StringTools;
import ch.loway.oss.ari4java.codegen2.models.ApiOperation;
import ch.loway.oss.ari4java.codegen2.models.ApiParameter;
import ch.loway.oss.ari4java.codegen2.models.DataType;
import ch.loway.oss.ari4java.codegen2.models.ModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class JMethod {
    public String returnType = "";
    public String name = "";
    public String description = "";
    public List<JParm>  parameters  = new ArrayList<JParm>();
    public List<String> availableIn = new ArrayList<String>();
    public List<String> throwsEx    = new ArrayList<String>();

    public void addParameter(DataType d, String name, String comment) {
        parameters.add( JParm.build(d.asJavaType(), name, comment));
    }

    /**
     * 
     * @param version xxx
     */
    
    public void addVersionAvailable(String version) {
        availableIn.add(version);
    }

    public static JMethod build(ApiOperation o) {
        JMethod m = new JMethod();
        m.name = o.nickname;
        m.returnType = o.responseClass.asJavaType();
        for (ApiParameter ap : o.parameters) {
            m.addParameter(ap.dataType, ap.name, ap.description + " " + ap.note );
        }
        return m;
    }

    public static JMethod buildGetter(String name, ModelProperty mp) {
        JMethod m = new JMethod();
        m.name = StringTools.mkGetter(name);
        m.returnType = mp.type.asJavaType();
        return m;
    }

    public static JMethod buildSetter(String name, ModelProperty mp) {
        JMethod m = new JMethod();
        m.name = StringTools.mkSetter(name);
        m.returnType = "void";
        m.addParameter(mp.type, "v", mp.description);
        return m;
    }

    public String getJavaSignature() {
        return returnType + " " + name + "(" + StringTools.join(parameters, ", ") + ")";
    }
    
    public String getCode() {
        String c = "public " + returnType + " " 
               + name + "(" + StringTools.join(parameters, ", ") + ")";
        
        if ( !throwsEx.isEmpty()  ) {
            c += " throws " + StringTools.join( throwsEx, ", ");
        }
        return c;
    }
    
    public String getAvailableVers() {
        return StringTools.join(availableIn, ", ");
    }
     
    public String getParamDescriptions() {
        
        String s = "";
        for ( JParm p: parameters) {
            s += " * @param " + p.name + " " + p.comment + "\n";
        }
        return s;
    }
    
    public String getDescription() {
        return description;
    }
    
    
    
    

}

// $Log$
//
