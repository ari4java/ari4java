
package ch.loway.oss.ari4java.codegen.gen;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lenz
 */
public class JavaPkgInfo {

    public static final Map<String, String> TypeMap;
    public static final Map<String, String> primitiveSignature;
    public static final String BASE_PKG_NAME = "ch.loway.oss.ari4java";
    public static final String GENERATED_PKG_NAME = BASE_PKG_NAME + ".generated";
    public static final String CLAZZ_IMPL_STRING = "_impl_";

    static {
        TypeMap = new HashMap<>();
        TypeMap.put("string", "String");
        TypeMap.put("long", "Long");
        TypeMap.put("int", "Integer");
        TypeMap.put("double", "Double");
        TypeMap.put("date", "Date");
        TypeMap.put("object", "Object");
        TypeMap.put("boolean", "Boolean");
        TypeMap.put("binary", "byte[]");
        TypeMap.put("containers", "Map<String,String>");
        
        primitiveSignature = new HashMap<>();
        primitiveSignature.put("Boolean", "boolean");
        primitiveSignature.put("Integer", "int");
        primitiveSignature.put("Long", "long");
        primitiveSignature.put("Double", "double");
    }

    public String className = "";
    public String apiVersion = "";
    public JavaInterface minimalIf = null;

    public void setPackageInfo(String classN, String apiV) {
        className = classN;
        apiVersion = apiV;
    }

    public String getBaseApiPackage() {
        return GENERATED_PKG_NAME + "." + apiVersion;
    }

    public String getModelPackage() {
        return getBaseApiPackage() + "." + "models";
    }

    public String getActionsPackage() {
        return getBaseApiPackage() + "." + "actions";
    }

    public String getInterfaceName() {
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }

    public String getImplName() {
        return className + CLAZZ_IMPL_STRING + apiVersion;
    }

    /**
     * This is a "minimal" interface that has to be implemented at all costs.
     * @param i interface
     */
    public void setMinimalInterface(JavaInterface i) {
        minimalIf = i;
    }

    /**
     * Gets a copy of the current base interface.
     * This is meant to have signatures removed as they are written.
     * @return JavaInterface
     */
    public JavaInterface getBaseInterface() {
        if (minimalIf == null) {
            return new JavaInterface();
        } else {
            return minimalIf.createScratchCopy();
        }
    }

}
