
package ch.loway.oss.ari4java.codegen2.models;

import java.util.List;

/**
 *
 * @author lenz
 * @see 
 */
public class AllowableValue {

    public Type valueType= null;
    public int min = 0;
    public List<String> values = null;
    
    public static enum Type {
        RANGE,
        LIST
    }
    
}
