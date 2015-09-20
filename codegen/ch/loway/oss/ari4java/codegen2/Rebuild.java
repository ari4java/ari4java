package ch.loway.oss.ari4java.codegen2;

import ch.loway.oss.ari4java.codegen2.models.Api;
import ch.loway.oss.ari4java.codegen2.models.ApiOperation;
import ch.loway.oss.ari4java.codegen2.models.Model;
import ch.loway.oss.ari4java.codegen2.models.ModelProperty;
import ch.loway.oss.ari4java.codegen2.models.SwaggerFile;
import ch.loway.oss.ari4java.codegen2.writeClass.InterfaceWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lenz
 */
public class Rebuild {

    public static String DIR = "/Users/lenz/dev/github/ari4java/codegen-data";
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        List<String> versions = Arrays.asList( new String[] {
            "ari_1_8_0", "ari_1_7_0"
                , "ari_1_6_0", "ari_1_5_0"
            // field "extends" needed but not valid Java name
            //    ,
            // "ari_1_0_0", "ari_0_0_1"
        });
        
        List<String> files = Arrays.asList(new String[]{
//            "applications", "asterisk", "bridges",
//            "channels", "deviceStates", "endpoints",
//            "mailboxes", "playbacks", "recordings",
//            "sounds", "events"
            
            "sounds"
        });
 
        // Loads all files into an in-memory representation.
        Map<String,Map<String,SwaggerFile>> sources = new HashMap();
        SharedInterfaces sharedInterfaces = new SharedInterfaces();
        SharedEnums      sharedEnums      = new SharedEnums();
        
        for (String version : versions) {
            for (String file : files) {
                loadSwaggerData(sources, version, file);
            }
        }

        for ( String version: versions ) {
        
        
        // Process models
        // Process events
        // Process Actions
        }
        
        
        
        // Common interface
        // Build interface
        // Build enums
        
       
        
        for ( String file: files) {
            for ( String ver: versions ) {
                SwaggerFile sf = sources.get(ver).get(file);
                sharedInterfaces.merge(sf);
            }
        }
        
        //System.out.println( sharedInterfaces.toString() );
        
        for ( String file: files) {
            for ( String ver: versions ) {
                SwaggerFile sf = sources.get(ver).get(file);
                for ( Api api: sf.apis) {
                    for ( ApiOperation ao: api.operations) {
                        sharedEnums.loadEnums( ao );
                    }
                }
                
                for ( Model m: sf.models.values() ) {
                    for ( String prop: m.properties.keySet() ) {
                        ModelProperty mp = m.properties.get(prop);
                        
                        sharedEnums.loadEnums( StringTools.enumClassName(m.id,prop), mp);
                    }
                }
                
            }
        }
        
        
        //System.out.println( sharedEnums.toString() );

        System.out.println( "-------");
        
        // Write interfaces
        
        
        // For each version
        String PATH = "~/varie";
        
        InterfaceWriter iw = new InterfaceWriter();
        iw.process( PATH, sharedInterfaces );
        
        
    }
    
    /**
     * Adds a Swagger file to the sources map.
     *
     *     map[version][filename] = swaggerFile
     * 
     * If no file found / load error, nothing is added.
     * 
     * @param sources
     * @param version
     * @param filename 
     */
    
    private static void loadSwaggerData(Map<String,Map<String,SwaggerFile>> sources, String version, String filename) {
        
        SwaggerFile sf = loadSwagger( version, filename + ".json" );
        
        if ( sf != null ) {
            
            sf.a4j_filename = filename;
            
            if ( !sources.containsKey(version) ) {
                sources.put( version, new HashMap<String,SwaggerFile>());
            }

            Map<String,SwaggerFile> m = sources.get( version );
            m.put( filename, sf );
        }
        
    }
    
    /**
     * Deserializes a swagger file.
     * 
     * @param version
     * @param name
     * @return the object, or null if no file was loaded. 
     */
    
    
    private static SwaggerFile loadSwagger( String version, String name )  {
        
        SwaggerFile sf = null;
        
        try {
        
            ObjectMapper mapper = new ObjectMapper();
            sf = mapper.readValue(new File( DIR + "/" + version + "/" + name ), SwaggerFile.class);

        } catch (IOException e ) {
            e.printStackTrace();
        }
        
        return sf;
    }
    
    
    
    
}
