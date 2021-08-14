package ch.loway.oss.ari4java.tools;

import ch.loway.oss.ari4java.ARI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ARIEncoder {

    public final static Charset ENCODING = StandardCharsets.UTF_8;

    /**
     * Exception safe way to encode url parts
     * @param url the part to encode
     * @return a encoded string
     */
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, ARIEncoder.ENCODING.name());
        } catch (UnsupportedEncodingException e) {
            // this should happen, but if so return the input
            return url;
        }
    }


}
