
package ch.loway.oss.ari4java.codegen.genJava;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class JavaPkgInfo {

    String base = "ch.loway.oss.ari4java.generated";

    String className = "";    
    String apiVersion = "";

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
        return className;
    }

    public String getImplName() {
        return className + "_impl_" + apiVersion;
    }



}

// $Log$
//
