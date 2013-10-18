
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class ModelField implements Comparable {

    public String field = "";
    public String type = "";
    public boolean required = false;    
    public String comment = "";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append( "  /**  " )
          .append( comment )
          .append("  */\n");
        sb.append( "  private ").append( type ).append( " ").append( field ).append( ";\n" );

        sb.append( getDeclarationGet() ).append(" {\n");
        sb.append( "   return ").append(field).append(";\n }\n\n");

        sb.append( getDeclarationSet() ).append( " {\n");
        sb.append( "   ").append( field).append(" = val;\n }\n\n");

        return sb.toString();
    }



    private String getterName( String field ) {
        return JavaGen.addPrefixAndCapitalize( "get", field );
    }

    private String setterName( String field ) {
        return JavaGen.addPrefixAndCapitalize( "set", field );
    }






    public String getSignatureGet() {
        return type + " " + getterName(field);
    }

    public String getSignatureSet() {
        return "void " + setterName(field) + " " + type;
    }

    public String getDeclarationGet() {
        StringBuilder sb = new StringBuilder();
        sb.append( " public ").append(type).append(" ").append( getterName(field) ).append("()" );
        return sb.toString();
    }

    public String getDeclarationSet() {
        StringBuilder sb = new StringBuilder();
        sb.append( " public void ").append( setterName(field) ).append("(") .append( type ).append(" val )" );
        return sb.toString();
    }

    public int compareTo(Object o) {
        ModelField mf2 = (ModelField) o;
        return field.compareTo( mf2.field );
    }



}

