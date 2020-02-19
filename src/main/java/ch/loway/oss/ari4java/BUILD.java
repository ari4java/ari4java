
package ch.loway.oss.ari4java;

import java.io.IOException;
import java.io.InputStream;
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
        InputStream stream = BUILD.class.getResourceAsStream("build.properties");
        try {
            Properties p = new Properties();
            p.load(stream);
            number = p.getProperty("BUILD_NUMBER");
        } catch (IOException e) {
            // oh well
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                // oh well
            }
        }
        BUILD_N = number;
    }

    public static final String VERSION = BUILD.class.getPackage().getImplementationVersion();
    public static final String BUILD_N;
}

