
package ch.loway.oss.ari4java.codegen2.writeClass;

/**
 * A Java call parameter.
 * 
 * @author lenz
 */
public class JParm {
    public String type = "";
    public String name = "";
    public String comment = "";

    public static JParm build(String t, String v, String c) {
        JParm jp = new JParm();
        jp.type = t;
        jp.name = v;
        jp.comment = c;
        return jp;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

}

