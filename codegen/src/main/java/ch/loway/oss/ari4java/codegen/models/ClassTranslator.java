package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.gen.JavaGen;
import ch.loway.oss.ari4java.codegen.gen.JavaPkgInfo;

import java.util.*;

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
        mInterfaces = new HashMap<>();
        imports = new ArrayList<>();
        className = "ClassTranslator";
        imports.add(JavaPkgInfo.BASE_PKG_NAME + ".ARI");
        imports.add(JavaPkgInfo.GENERATED_PKG_NAME + ".actions.*");
        imports.add(JavaPkgInfo.GENERATED_PKG_NAME + ".models.Module");
        imports.add(JavaPkgInfo.GENERATED_PKG_NAME + ".models.*");
        imports.add(JavaPkgInfo.GENERATED_PKG_NAME + "." + apiVersion + ".actions.*");
        imports.add(JavaPkgInfo.GENERATED_PKG_NAME + "." + apiVersion + ".models.*");
    }

    public void setClass(String ifName, String implementation) {
        mInterfaces.put(ifName, implementation);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        JavaGen.importClasses(sb, getBaseApiPackage(), imports);
        JavaGen.addBanner(sb, "This is a class translator." + "\n\n");

        List<String> ifNames = new ArrayList<>(mInterfaces.keySet());
        Collections.sort(ifNames);

        sb.append("public class ").append(getImplName()).append(" implements ARI.ClassFactory {\n\n");
        sb.append("  @Override\n" +
                "  public Class getImplementationFor(Class interfaceClass) {\n");

        for (String ifName : ifNames) {
            String impl = mInterfaces.get(ifName);

            sb.append("\n"
                    + "\tif ( interfaceClass.equals(")
                    .append(ifName)
                    .append(".class) ) {\n"
                            + "    return (")
                    .append(impl)
                    .append(".class);\n"
                            + "  } else \n");
        }

        sb.append("    {\n" +
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

    @Override
    public int hashCode() {
        return this.apiVersion.hashCode();
    }
}
