package ch.loway.oss.ari4java.tools;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;

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
            return URLEncoder.encode(url, ARIEncoder.ENCODING);
        } catch (Exception e) {
            // this should happen, but if so return the input
            return url;
        }
    }

    public static String encodeCreds(String username, String password) {
        ByteBuf buf1 = Unpooled.copiedBuffer(username + ":" + password, ARIEncoder.ENCODING);
        try {
            ByteBuf buf2 = Base64.encode(buf1, false);
            try {
                return "Basic " + buf2.toString(ARIEncoder.ENCODING);
            } finally {
                buf2.release();
            }
        } finally {
            buf1.release();
        }
    }

}
