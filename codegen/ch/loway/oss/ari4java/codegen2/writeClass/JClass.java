
package ch.loway.oss.ari4java.codegen2.writeClass;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenz
 */
public class JClass {

    public String xPackage = "";
    public String xName    = "";
    public Type   type     = null;
    public JClass xtends   = null;
    public List<JClass> xImpls = new ArrayList<JClass>();
    
    public static enum Type {
        CLASS,
        INTERFACE,
        ENUM
    }
    
    
    public String getFullName() {
        return xPackage + "." + xName;
    }
    
    
    
}

// $Log$
//
