
package ch.loway.oss.ari4java.codegen2;

import ch.loway.oss.ari4java.codegen2.writeClass.JMethod;
import ch.loway.oss.ari4java.codegen2.models.ApiOperation;
import ch.loway.oss.ari4java.codegen2.models.ApiParameter;
import ch.loway.oss.ari4java.codegen2.models.DataType;
import ch.loway.oss.ari4java.codegen2.models.ModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class SharedInterface {
    
    public String name = "";
    public String fromFile = "";
    
    public Map<String,JMethod> methods = new HashMap<String,JMethod>();
    
    /**
     * Merges a SwaggerFile into this interface.
     * 
     * @param si 
     */
    
    public void add( String apiVersion, ApiOperation op ) {
        
        JMethod m = JMethod.build(op);
        setUnlessDupe(m, apiVersion);
        
    }
    
    
    
    
    
    public void addGetterSetter( String apiVersion, String propertyName, ModelProperty mp ) {
        
        setUnlessDupe(JMethod.buildGetter(propertyName, mp), apiVersion);
        setUnlessDupe(JMethod.buildSetter(propertyName, mp), apiVersion);
        
    }
    
    
    private JMethod setUnlessDupe( JMethod m, String apiVersion ) {
        String sig = m.getJavaSignature();
        
        if ( methods.containsKey(sig) ) {
            m = methods.get(sig);
        } else {
            methods.put(sig, m);
        }
        
        m.addVersionAvailable( apiVersion );
        return m;
    }
    
    public List<JMethod> getMethods() {
        List<JMethod> lM = new ArrayList();
        for ( String mn: StringTools.sortedKeySet(methods) ) {
            lM.add( methods.get(mn) );
        }
        return lM;
    }
    
    
    

}

// $Log$
//
