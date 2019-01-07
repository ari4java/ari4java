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

      public Map<String,String> mInterfaces;
        public List<String> imports;
    
    public ClassTranslator() {
        super();
        mInterfaces = new HashMap<String,String>();
        imports = new ArrayList<String>();
        className = "ClassTranslator";
        imports.add("ch.loway.oss.ari4java.ARI" );
        imports.add("ch.loway.oss.ari4java.generated.Module");
        imports.add( "ch.loway.oss.ari4java.generated.*" );
    }
    
  
    
    public void setClass( String ifName, String implementation ) {
        mInterfaces.put(ifName, implementation);
    }
    
    /**
     * Writes an interface translator.
     * 
     * public class SampleClassFactory implements ARI.ClassFactory {
     * 
     * @Override
     *   public Class getImplementationFor(Class interfaceClass) {
     *       
     *       if ( interfaceClass.equals(ActionBridges.class) ) {
     **           return ActionBridges_impl_ari_0_0_1.class;
     *       } else 
     *       {
     *           return null;
     *       }
     *   }        
     * }
     * 
     * 
     * @return 
     */
    
    @Override
    public String toString() {
        
        imports.add("ch.loway.oss.ari4java.generated." + apiVersion +".models.*");
        imports.add("ch.loway.oss.ari4java.generated." + apiVersion +".actions.*");
        
        StringBuilder sb = new StringBuilder();

        JavaGen.importClasses(sb, getBaseApiPackage(), imports);

        JavaGen.addBanner(sb, "This is a class translator." + "\n\n"   );

        //sb.append(additionalPreambleText).append( "\n" );

        List<String> ifNames = new ArrayList<String>(mInterfaces.keySet());
        Collections.sort( ifNames );
        
        sb.append( 
            "public class " + getImplName() + " implements ARI.ClassFactory {\n" +
            "\n" +
            "  @Override\n" +
            "  public Class getImplementationFor(Class interfaceClass) { \n" );
        
        for ( String ifName: ifNames ) {
            String impl = mInterfaces.get(ifName);
            
            sb.append( "\n"
                    + "\tif ( interfaceClass.equals(")
              .append( ifName )
              .append( ".class) ) {\n" 
                    + "\t   return (" )
              .append( impl )
              .append(".class);\n" 
                    + "\t} else \n" );
        }
        
        sb.append( 
            "    {\n" +
            "      return null;\n" +
            "    }\n" +
            "  }\n" +
            "}\n\n" );
        return sb.toString();
    }
    
    
}
