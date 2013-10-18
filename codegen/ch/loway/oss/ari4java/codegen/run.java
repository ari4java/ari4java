
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

    public static String SOURCES = "codegen-data/ari_0_0_1/";
    // D:\git\ari4java\codegen-data\ari_0_0_1


    public static void main( String [] argv ) throws IOException {
        System.out.println("This is ARI4JAVA Code Generator version " + VERSION.VER );

        DefMapper dm = new DefMapper();

        dm.load( new File(SOURCES + "applications.json") );
        dm.load( new File(SOURCES + "asterisk.json") );
        dm.load( new File(SOURCES + "bridges.json") );
        dm.load( new File(SOURCES + "channels.json") );
        dm.load( new File(SOURCES + "endpoints.json") );
        dm.load( new File(SOURCES + "events.json") );
        dm.load( new File(SOURCES + "playback.json") );
        dm.load( new File(SOURCES + "recordings.json") );
        dm.load( new File(SOURCES + "sounds.json") );
    }


}

// $Log$
//
