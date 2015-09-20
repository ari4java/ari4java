package ch.loway.oss.ari4java.codegen2.models;

import java.util.HashMap;
import java.util.Map;

public class DataType {

    private static Map<String,String> REMAP_TYPES;
    static {
        REMAP_TYPES = new HashMap<String,String>();
        REMAP_TYPES.put( "string", "String");
        REMAP_TYPES.put( "int", "int");
        REMAP_TYPES.put( "boolean", "boolean"); 
        REMAP_TYPES.put( "long", "long");
        REMAP_TYPES.put( "containers", "containers");
        REMAP_TYPES.put( "void", "void" );
        REMAP_TYPES.put( "Date", "Date");
        REMAP_TYPES.put( "double", "double");
    }
    
    
    public String swagType = "";
    public String javaType = "";
    
    
    public DataType( String fromSwaggerType ) {
        swagType = fromSwaggerType;
        javaType = mkJavaType( swagType );
        
    }
    
    
    public String asJavaType() {
        return javaType;
    }
    
    /**
     * Translates a Swagger type to Java.
     * 
     * @param type
     * @return 
     */
    
    private String mkJavaType( String type ) {
        
        if ( type.startsWith("List[") && type.endsWith("]")) {
            String inner = type.substring( "List[".length(), type.length() - "]".length() );
            return "List<" + mkJavaType(inner) + ">";
        }
        
        if ( REMAP_TYPES.containsKey(type) ) {
            return REMAP_TYPES.get(type);
        } else {
            return type;
        }
           
    }
        
}

