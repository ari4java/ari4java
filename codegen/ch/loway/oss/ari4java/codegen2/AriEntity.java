package ch.loway.oss.ari4java.codegen2;

import ch.loway.oss.ari4java.codegen2.writeClass.JClass;
import ch.loway.oss.ari4java.codegen2.writeClass.JMethod;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a bsic ARI entity:
 * - ActionXXX
 * - A model
 * - An event
 * 
 * @author lenz
 */
public class AriEntity {

    public String ariVersion = "";
    public JClass javaClass  = new JClass();
    
    public List<JMethod> methods = new ArrayList<JMethod>();
    
}

