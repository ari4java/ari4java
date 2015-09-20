/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.ari4java.codegen2.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 *
 * @author lenz
 * @version $Id$
 */
public class Model {
    public String id = "";
    public String description = "";
    public Discriminator discriminator = null;
    public List<String> subTypes = new ArrayList<String>();
    // public String extends = "";
    public Map<String,ModelProperty> properties = new HashMap<String,ModelProperty>();

    
    public static enum Discriminator {
        type
    }
    
}

// $Log$
//
