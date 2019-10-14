
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;

/**
 * $Id$
 *
 * @author lenz
 */
public class ModelField implements Comparable<ModelField> {

    public String field = "";
    public String typeInterface = "";
    public String typeConcrete = "";
    public boolean required = false;
    public String comment = "";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  /**  ")
                .append(comment)
                .append("  */\n");
        sb.append("  private ").append(typeInterface).append(" ").append(field).append(";\n");

        sb.append(getDeclarationGet()).append(" {\n");
        sb.append("    return ").append(field).append(";\n  }\n");

        if (typeConcrete.startsWith("List")) {
            String innerType = typeConcrete.substring(5, typeConcrete.length() - 1);
            sb.append("  @JsonDeserialize( contentAs=").append(innerType).append(".class )\n");
        } else if ("Object".equals(typeConcrete)) {
            sb.append("  @JsonDeserialize( using=com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer.class").append(" )\n");
        } else {
            sb.append("  @JsonDeserialize( as=").append(typeConcrete).append(".class )\n");
        }
        sb.append(getDeclarationSet()).append("  {\n");
        sb.append("    ").append(field).append(" = val;\n  }\n\n");

        return sb.toString();
    }

    private String getterName(String field) {
        return JavaGen.addPrefixAndCapitalize("get", field);
    }

    private String setterName(String field) {
        return JavaGen.addPrefixAndCapitalize("set", field);
    }

    public String getSignatureGet() {
        return typeInterface + " " + getterName(field);
    }

    public String getSignatureSet() {
        return "void " + setterName(field) + " " + typeInterface;
    }

    public String getDeclarationGet() {
        StringBuilder sb = new StringBuilder();
        sb.append("  public ").append(typeInterface).append(" ").append(getterName(field)).append("()");
        return sb.toString();
    }

    public String getDeclarationSet() {
        StringBuilder sb = new StringBuilder();
        sb.append("  public void ").append(setterName(field)).append("(").append(typeInterface).append(" val )");
        return sb.toString();
    }

    @Override
    public int compareTo(ModelField o) {
        ModelField mf2 = o;
        return field.compareTo(mf2.field);
    }

}

