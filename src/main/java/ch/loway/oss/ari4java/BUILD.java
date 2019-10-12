
package ch.loway.oss.ari4java;

import java.io.IOException;
import java.util.Properties;

/**
 * Do not hand-edit this file.
 * The values below will be set by the Gradle build script.
 *
 * 
 * @author lenz
 */
public class BUILD {

    static {
        String number = "x";
        try {
            Properties p = new Properties();
            p.load(BUILD.class.getResourceAsStream("build.properties"));
            number = p.getProperty("BUILD_NUMBER");
        } catch (IOException e) {
            // oh well
        }
        BUILD_N = number;
    }

    public static final String VERSION = BUILD.class.getPackage().getImplementationVersion();
    public static final String BUILD_N;
}

