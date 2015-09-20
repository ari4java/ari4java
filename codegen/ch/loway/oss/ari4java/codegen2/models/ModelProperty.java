/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.ari4java.codegen2.models;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class ModelProperty {

    public DataType type = null;
    public String description = "";
    public boolean required;
    public AllowableValue allowableValues = null;
		
    
    /**
     * Questo e' un valore semplice o un enum?
     * 
     * @return 
     */
    
    public boolean translatesToEnum() {
        if (allowableValues != null ) {
            if ( allowableValues.valueType == AllowableValue.Type.LIST) {
                assert( type.javaType.equalsIgnoreCase("String") );
                return true;
            }
        }
        return false;
    }
    
}

// $Log$
//
