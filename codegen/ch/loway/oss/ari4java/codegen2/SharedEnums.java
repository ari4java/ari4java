/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.ari4java.codegen2;

import ch.loway.oss.ari4java.codegen2.models.ApiOperation;
import ch.loway.oss.ari4java.codegen2.models.ApiParameter;
import ch.loway.oss.ari4java.codegen2.models.ModelProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class SharedEnums {

    Map<String,Set<String>> enums = new HashMap();
    
    public void addEnumValue( String enumName, String value ) {
        
        enumName = StringTools.prefixCapitalize("E", enumName);
        
        if ( !enums.containsKey(enumName) ) {
            enums.put( enumName, new TreeSet<String>() );
        }
        
        Set<String> vals = enums.get(enumName);
        vals.add(value);
    }
    
    
    public void loadEnums( ApiOperation op ) {
        for ( ApiParameter p: op.parameters) {
            if (p.translatesToEnum() ) {
                for ( String evs: p.allowableValues.values ) {
                    addEnumValue( p.name, evs);
                }
            }
        }
    }

    
    
    public void loadEnums( String name, ModelProperty mp ) {
        if (mp.translatesToEnum() ) {
                for ( String evs: mp.allowableValues.values ) {
                    addEnumValue( name, evs);
                }
        }
    }
    
    
    @Override
    public String toString() {
        return "ENUMS:\n" + enums.toString();
    }
    
    
    
}

// $Log$
//
