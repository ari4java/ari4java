
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.gen.JavaGen;

import java.util.Objects;

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
        sb.append("  private ").append(typeInterface).append(" ").append(field).append(";\n");
        sb.append("  ").append(getDeclarationGet()).append(" {\n");
        sb.append("    return ");
        if ("Date".equals(typeConcrete)) {
            sb.append("new Date(").append(field).append(".getTime())");
        } else {
            sb.append(field);
        }
        sb.append(";\n  }\n\n");
        if (typeConcrete.startsWith("List")) {
            String innerType = typeConcrete.substring(5, typeConcrete.length() - 1);
            sb.append("  @JsonDeserialize( contentAs=").append(innerType).append(".class )\n");
        } else if ("Object".equals(typeConcrete)) {
            sb.append("  @JsonDeserialize( using=com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer.class").append(" )\n");
        } else {
            sb.append("  @JsonDeserialize( as=").append(typeConcrete).append(".class )\n");
        }
        sb.append("  ").append(getDeclarationSet()).append("  {\n");
        sb.append("    ").append(field);
        if ("Date".equals(typeConcrete)) {
            sb.append(" = new Date(val.getTime())");
        } else {
            sb.append(" = val");
        }
        sb.append(";\n  }\n\n");
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
        return "public " + typeInterface + " " + getterName(field) + "()";
    }

    public String getDeclarationSet() {
        return "public void " + setterName(field) + "(" + typeInterface + " val)";
    }

    @Override
    public int compareTo(ModelField o) {
        return field.compareTo(o.field);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelField that = (ModelField) o;
        return required == that.required &&
                Objects.equals(field, that.field) &&
                Objects.equals(typeInterface, that.typeInterface) &&
                Objects.equals(typeConcrete, that.typeConcrete) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, typeInterface, typeConcrete, required, comment);
    }

}
