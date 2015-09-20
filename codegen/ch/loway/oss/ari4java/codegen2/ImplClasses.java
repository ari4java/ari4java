
package ch.loway.oss.ari4java.codegen2;

import ch.loway.oss.ari4java.codegen2.models.Api;
import ch.loway.oss.ari4java.codegen2.models.SwaggerFile;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lenz
 */
public class ImplClasses {

    Map<String,AriEntity> hmClassi = new HashMap<String,AriEntity>();
    
    public void process( String ariVersion, SwaggerFile sf ) {
        
        for ( Api a: sf.apis ) {
            
        }
        
        
    }
    
    public void addClass( AriEntity e ) {
        String className = e.javaClass.getFullName();
        
        assert( !hmClassi.containsKey( className ) );
        
        hmClassi.put(className, e);
        
    }
    
    
}

