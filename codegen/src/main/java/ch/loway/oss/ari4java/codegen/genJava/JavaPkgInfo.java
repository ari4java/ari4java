
package ch.loway.oss.ari4java.codegen.genJava;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lenz
 */
public class JavaPkgInfo {

    public final static Map<String, String> TypeMap;
    public final static Map<String, String> primitiveSignature;

    static {
        TypeMap = new HashMap<String, String>();
        TypeMap.put("string", "String");
        TypeMap.put("long", "Long");
        TypeMap.put("int", "Integer");
        TypeMap.put("double", "Double");
        TypeMap.put("date", "Date");
        TypeMap.put("object", "Object");
        TypeMap.put("boolean", "Boolean");
        TypeMap.put("binary", "byte[]");
        TypeMap.put("containers", "Map<String,String>");
        
        primitiveSignature = new HashMap<String, String>();
        primitiveSignature.put("Boolean", "boolean");
        primitiveSignature.put("Integer", "int");
        primitiveSignature.put("Long", "long");
        primitiveSignature.put("Double", "double");
    }

    String base = "ch.loway.oss.ari4java.generated";
    public String className = "";
    public String apiVersion = "";
    public JavaInterface minimalIf = null;

    public void setPackageInfo(String classN, String apiV) {
        className = classN;
        apiVersion = apiV;
    }

    public String getInterfacePackage() {
        return base + "." + className;
    }

    public String getBaseApiPackage() {
        return base + "." + apiVersion;
    }

    public String getModelPackage() {
        return getBaseApiPackage() + "." + "models";
    }

    public String getActionsPackage() {
        return getBaseApiPackage() + "." + "actions";
    }

    public String getInterfaceName() {
        String s = className.substring(0, 1).toUpperCase() + className.substring(1);
        return s;
    }

    public String getImplName() {
        return className + "_impl_" + apiVersion;
    }

    /**
     * This is a "minimal" interface that has to be implemented at all costs.
     *
     * @param i
     */
    public void setMinimalInterface(JavaInterface i) {
        minimalIf = i;
    }

    /**
     * Gets a copy of the current base interface.
     * This is meant to have signatures removed as they are written.
     *
     * @return
     */
    public JavaInterface getBaseInterface() {
        if (minimalIf == null) {
            return new JavaInterface();
        } else {
            return minimalIf.createScratchCopy();
        }
    }

}
