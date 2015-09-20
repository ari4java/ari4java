
package ch.loway.oss.ari4java.codegen2;

import ch.loway.oss.ari4java.codegen2.writeClass.JMethod;
import ch.loway.oss.ari4java.codegen2.models.Api;
import ch.loway.oss.ari4java.codegen2.models.ApiOperation;
import ch.loway.oss.ari4java.codegen2.models.Model;
import ch.loway.oss.ari4java.codegen2.models.SwaggerFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds a set of shared iterfaces.
 * 
 * @author lenz
 * @version $Id$
 */
public class SharedInterfaces {

    Map<String, SharedInterface> ints = new HashMap<String,SharedInterface>();
    
    public void merge( SwaggerFile sf ) {
        String version = sf.apiVersion;
        
        // sounds.json -> ActionSounds
        String actionName = sf.getActionName();
        SharedInterface sia = fetchOrCreate(actionName);
        
        for ( Api api: sf.apis ) {
            for (ApiOperation op: api.operations ) {
                sia.add(version, op);
            }
        }
        
        // Objects
        for ( Model m: sf.models.values() ){
            String name = m.id;
            SharedInterface smi = fetchOrCreate(name);
            
            for (String names: m.properties.keySet() ) {
                smi.addGetterSetter( version, names, m.properties.get(names) );
            }
        } 
    }
    
    /**
     * Gets a Shared Interface, or creates it if it does not exist.
     * 
     * @param interfaceName
     * @return 
     */
    
    private SharedInterface fetchOrCreate( String interfaceName ) {
        if ( !ints.containsKey( interfaceName) ) {
            SharedInterface si = new SharedInterface();
            si.name = interfaceName;
            ints.put(interfaceName, si);
        }
        return ints.get(interfaceName);
    }

    
    /**
     * Dumps the shared interface.
     * 
     * @return 
     */
    
    
    @Override
    public String toString() {
        
        String s = "SharedInterface:\n";
        for ( String k: StringTools.sortedKeySet( ints ) ) {
            s += "- Inteface " + k + ":\n";
            SharedInterface si = ints.get(k);
            for ( String mn: StringTools.sortedKeySet( si.methods) ) {
                JMethod m = si.methods.get(mn);
                s += "   " + mn + " " + m.availableIn + "\n";
            }
        }
        return s;
    }
    
    public Map<String, SharedInterface> getIntefaces() {
        return ints;
    }
    
    
}

// $Log$
//
