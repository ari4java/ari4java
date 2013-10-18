
package ch.loway.oss.ari4java.codegen.models;

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
        sb.append( "  public ").append( type ).append( " ").append( field ).append( ";\n" );

        return sb.toString();
    }

    public int compareTo(Object o) {
        ModelField mf2 = (ModelField) o;
        return field.compareTo( mf2.field );
    }



}

