
package ch.loway.oss.ari4java.codegen2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class StringTools {

    public static String capitalize( String in ) {
        if ( in.length() < 2 ) {
            return in.toUpperCase();
        } else {
            return in.substring(0,1).toUpperCase() + in.substring(1);
        }
    }
    
    public static String prefixCapitalize( String prefix, String name ) {
        return prefix + capitalize(name);
    }
    
    public static String mkGetter( String name ) {
        return prefixCapitalize("get", name);
    }
    
    public static String mkSetter( String name ) {
        return prefixCapitalize("set", name);
    }
    
    public static String mkActionClass( String name ) {
        return prefixCapitalize( "Action", name);
    }
    
    /**
     * Traverses map.keySet orderly.
     * 
     * @param m
     * @return 
     */
    
    public static List<String> sortedKeySet( Map<String, ?> m) {
        List<String> ks = new ArrayList<String>( m.keySet() );
        Collections.sort(ks);
        return ks;
    } 
    
    /**
     * Joins a list of objects with a separator.
     * 
     * @param vals
     * @param sep
     * @return 
     */
    
    public static String join( List vals, String sep ) {
        StringBuilder sb = new StringBuilder();
        for (int i =0; i < vals.size(); i++) {
            if (i>0) {
                sb.append( sep );
            }
            sb.append( vals.get(i).toString() ); 
        }
        return sb.toString();
    }
    
    public static String enumClassName( String model, String name ) {
        
        if ( name.equalsIgnoreCase( "state") ) {
            return prefixCapitalize(model, name);
        }
        
        return capitalize(name);
    }
    
    
}

