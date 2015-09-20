package ch.loway.oss.ari4java.codegen2.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lenz
 */
public class SwaggerFile {
    
    // Loaded by JAckson
    
    public String _copyright = "";
    public String _author = "";
    public String _svn_revision = "";
    public String swaggerVersion = "";
    public String basePath = "";
    public String apiVersion = "";
    public String resourcePath = "";
    
    public List<Api> apis = new ArrayList<Api>();
    public Map<String,Model> models = new HashMap<String,Model>();
    
    
    // Other
    public String a4j_filename = "";
    
    /**
     * File: sounds -> ActionSounds.
     * 
     * @return  
     */
    public String getActionName() {
        return "Action" 
                + a4j_filename.substring(0, 1).toUpperCase()
                + a4j_filename.substring(1);
    }
    
    
    
    
    
    
    
    
    
}
