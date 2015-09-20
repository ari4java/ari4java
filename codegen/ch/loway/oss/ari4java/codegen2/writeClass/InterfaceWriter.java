
package ch.loway.oss.ari4java.codegen2.writeClass;

import ch.loway.oss.ari4java.codegen2.SharedInterfaces;
import ch.loway.oss.ari4java.codegen2.StringTools;
import java.util.HashMap;
import java.util.Map;

/**
 * Writes interfaces.
 * 
 * @author lenz
 */
public class InterfaceWriter  extends BasicFtl {

    public void process(String PATH, SharedInterfaces sharedInterfaces) throws Exception {
        
        for ( String intf: StringTools.sortedKeySet( sharedInterfaces.getIntefaces()) ) {
    
            
        Map root = new HashMap();
        root.put( "interfaceName" , intf );
        root.put( "methods", sharedInterfaces.getIntefaces().get(intf).getMethods() );
        
        System.out.println( processTemplate( "ftl/interface.ftl", root));
            
        }
        
        
    }
    

}

// $Log$
//
