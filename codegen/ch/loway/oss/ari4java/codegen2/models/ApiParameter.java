
package ch.loway.oss.ari4java.codegen2.models;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class ApiParameter {

    public String name= "";
    public String description = ""; 
    public ParamType paramType = null;
    public boolean required = false;
    public boolean allowMultiple = false;
    public DataType dataType = null;
    public String defaultValue = null;
    public AllowableValue allowableValues = null;
    public String note = "";
    
    public static enum ParamType {
        query,
        path,
        body;
    }

    
    public boolean translatesToEnum() {
        if (allowableValues != null ) {
            if ( allowableValues.valueType == AllowableValue.Type.LIST) {
                assert( dataType.javaType.equalsIgnoreCase("String") );
                return true;
            }
        }
        return false;
    }
    
    
}

// $Log$
//
