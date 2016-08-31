
package ch.loway.oss.ari4java.codegen;

import java.io.File;
import java.io.IOException;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class run {

    //public static String SOURCES = "codegen-data/";
    
    public static String PROJECT = "/Users/lenz/dev/github/ari4java";
    
    public static String SOURCES = PROJECT + "/codegen-data/";
    

    public static String SRC_001 = SOURCES + "ari_0_0_1/";
    // D:\git\ari4java\codegen-data\ari_0_0_1


    public static void main( String [] argv ) throws IOException {
        System.out.println("This is ARI4JAVA Code Generator version " + VERSION.VER );

        DefMapper dm = new DefMapper();
        dm.setProjectFolder(PROJECT);

        loadAsteriskDefs( dm, "ari_0_0_1" );
        loadAsteriskDefs( dm, "ari_1_0_0" );
        loadAsteriskDefs( dm, "ari_1_5_0" );
        loadAsteriskDefs( dm, "ari_1_6_0" );
        loadAsteriskDefs( dm, "ari_1_7_0" );
        loadAsteriskDefs( dm, "ari_1_8_0" );
        loadAsteriskDefs( dm, "ari_1_9_0" );
        loadAsteriskDefs( dm, "ari_1_10_0" );
        
        dm.generateAllClasses();


//        dm.writeProperties("ari_0_0_1");
        
    }


    private static void loadAsteriskDefs( DefMapper dm, String srcVer ) throws IOException {

        String srcDir = SOURCES + srcVer + "/";

        dm.clean( srcVer );
        dm.parseJsonDefinition( new File(srcDir + "applications.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "asterisk.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "bridges.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "channels.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "endpoints.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "playbacks.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "recordings.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "sounds.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "deviceStates.json"), srcVer, false );
        dm.parseJsonDefinition( new File(srcDir + "events.json"), srcVer, true );
        
    }



}

// $Log$
//
