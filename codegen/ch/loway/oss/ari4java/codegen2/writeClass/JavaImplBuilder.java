
package ch.loway.oss.ari4java.codegen2.writeClass;

import ch.loway.oss.ari4java.codegen2.Settings;
import ch.loway.oss.ari4java.codegen2.SharedInterface;
import ch.loway.oss.ari4java.codegen2.models.Model;
import ch.loway.oss.ari4java.codegen2.models.ModelProperty;
import java.util.Map.Entry;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class JavaImplBuilder {

    public static JClass makeModelImpl( String version, Model m, SharedInterface i ) {
        
        JClass jc = new JClass();
        jc.xName = m.id + "_impl_" + version;
        jc.xPackage = Settings.pkgGen + "." + version + ".models";
        
        for ( Entry<String,ModelProperty> mp: m.properties.entrySet() ) {
            String propName = mp.getKey();
            ModelProperty pr = mp.getValue();
            
            JMethod getter = JMethod.buildGetter(propName, pr);
            JMethod setter = JMethod.buildSetter(propName, pr);
            
            getter.body = "";
            setter.body = "";
        
            jc.xMethods.add(setter);
            jc.xMethods.add(getter);
            
        }

        
        
        
        // controlla i metodi esisteti
        for (Entry<String, JMethod> em: i.methods.entrySet() ) {

        }

        return jc;
        
        
    }
    
    
}

