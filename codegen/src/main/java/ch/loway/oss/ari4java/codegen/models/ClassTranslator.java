package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import ch.loway.oss.ari4java.codegen.genJava.JavaPkgInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class models a ClassTranslator.
 *
 * @author lenz
 */
public class ClassTranslator extends JavaPkgInfo {

    public Map<String, String> mInterfaces;
    public List<String> imports;

    public ClassTranslator(String version) {
        super();
        apiVersion = version;
        mInterfaces = new HashMap<String, String>();
        imports = new ArrayList<String>();
        className = "ClassTranslator";
        imports.add("ch.loway.oss.ari4java.ARI");
        imports.add("ch.loway.oss.ari4java.generated.actions.*");
        imports.add("ch.loway.oss.ari4java.generated.models.Module");
        imports.add("ch.loway.oss.ari4java.generated.models.*");
        imports.add("ch.loway.oss.ari4java.generated." + apiVersion + ".actions.*");
        imports.add("ch.loway.oss.ari4java.generated." + apiVersion + ".models.*");
    }

    public void setClass(String ifName, String implementation) {
        mInterfaces.put(ifName, implementation);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        JavaGen.importClasses(sb, getBaseApiPackage(), imports);
        JavaGen.addBanner(sb, "This is a class translator." + "\n\n");

        List<String> ifNames = new ArrayList<String>(mInterfaces.keySet());
        Collections.sort(ifNames);

        sb.append(
                "public class " + getImplName() + " implements ARI.ClassFactory {\n" +
                        "\n" +
                        "  @Override\n" +
                        "  public Class getImplementationFor(Class interfaceClass) { \n");

        for (String ifName : ifNames) {
            String impl = mInterfaces.get(ifName);

            sb.append("\n"
                    + "\tif ( interfaceClass.equals(")
                    .append(ifName)
                    .append(".class) ) {\n"
                            + "\t   return (")
                    .append(impl)
                    .append(".class);\n"
                            + "\t} else \n");
        }

        sb.append(
                "    {\n" +
                        "      return null;\n" +
                        "    }\n" +
                        "  }\n" +
                        "}\n\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return this.apiVersion.equals(obj);
        } else if (obj instanceof ClassTranslator) {
            return this.apiVersion.equals(((ClassTranslator) obj).apiVersion);
        }
        return super.equals(obj);
    }
}
