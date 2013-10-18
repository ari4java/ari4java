
package ch.loway.oss.ari4java.codegen.genJava;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class JavaPkgInfo {

    String base = "ch.loway.oss.ari4java.generated";

    public String className = "";
    public String apiVersion = "";

    public void setPackageInfo( String classN, String apiV ) {
        className = classN;
        apiVersion = apiV;
    }


    public String getInterfacePackage() {
        return base + "." + className;
    }

    public String getModelPackage() {
        return base + "." + apiVersion + "." + "models";
    }

    public String getActionsPackage() {
        return base + "." + apiVersion + "." + "actions";
    }

    public String getInterfaceName() {        
        String s = className.substring(0, 1).toUpperCase() + className.substring(1);
        return s;
    }

    public String getImplName() {
        return className + "_impl_" + apiVersion;
    }



}

// $Log$
//
