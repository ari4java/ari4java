
package ch.loway.oss.ari4java.codegen;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import ch.loway.oss.ari4java.codegen.genJava.JavaInterface;
import ch.loway.oss.ari4java.codegen.genJava.JavaPkgInfo;
import ch.loway.oss.ari4java.codegen.models.Action;
import ch.loway.oss.ari4java.codegen.models.Apis;
import ch.loway.oss.ari4java.codegen.models.AriBuilderInterface;
import ch.loway.oss.ari4java.codegen.models.ClassTranslator;
import ch.loway.oss.ari4java.codegen.models.Operation;
import ch.loway.oss.ari4java.codegen.models.Model;
import ch.loway.oss.ari4java.codegen.models.ModelField;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The model mapper keeps a list of interfaces and actual implementations.
 *
 * $Id$
 * @author lenz
 */
public class DefMapper {

    List<Model> mymodels = new ArrayList<Model>();
    List<String> knownModels = new ArrayList<String>();

    List<Apis> myAPIs = new ArrayList<Apis>();

    Map<String, JavaInterface> interfaces = new HashMap<String, JavaInterface>();
    String myAbsoluteProjectFolder = ".";
    
    
    /**
     * Loads definitions from a module.
     *
     * @param f The source .json file
     * @param apiVersion The version of the API we are working with
     * @param modelHasEvents whether this file generates WS events
     * @throws IOException
     */


    public void parseJsonDefinition( File f, String apiVersion, boolean modelHasEvents ) throws IOException {

        ObjectMapper om = new ObjectMapper();
        System.out.println( "Loading as: " + f.getAbsolutePath() );

        JsonNode rootNode = om.readTree(f);
        List<Model> lModels = loadModels(rootNode.get("models"), f, apiVersion );
        Apis api1 = loadApis( rootNode.get("apis"), f, apiVersion );

        mymodels.addAll(lModels);
        myAPIs.add(api1);

        if ( modelHasEvents ) {
        
            Model typeMessage = null;
            List<Model> otherModels = new ArrayList<Model>();

            for ( Model m: lModels ) {
                if ( m.className.equalsIgnoreCase("Message") ) {
                    typeMessage = m;
                } else {
                    otherModels.add(m);
                }
            }

            String defs = "";
            for ( Model m: otherModels ) {
                if ( defs.length() > 0 ) {
                    defs += ", ";
                }
                defs += "@Type(value = " + m.getImplName() + ".class, name = \"" + m.getInterfaceName() + "\")\n";
            }

            typeMessage.additionalPreambleText = ""
                 + " @JsonTypeInfo(  "
                 + "     use = JsonTypeInfo.Id.NAME,  "
                 + "     include = JsonTypeInfo.As.PROPERTY,  "
                 + "     property = \"type\") \n "
                 + " @JsonSubTypes({  "
                 + defs
                 + " })  \n\n";

            typeMessage.imports.add("com.fasterxml.jackson.annotation.JsonSubTypes");
            typeMessage.imports.add("com.fasterxml.jackson.annotation.JsonSubTypes.Type");
            typeMessage.imports.add("com.fasterxml.jackson.annotation.JsonTypeInfo");

        }                

        // Now generate the interface
        for ( Model m: mymodels ) {
            JavaInterface j = interfaces.get(m.getInterfaceName());
            if ( j == null ) {
                j = new JavaInterface();
                j.pkgName = "ch.loway.oss.ari4java.generated";
                j.className = m.getInterfaceName();
                j.parent = m.extendsModel;
                interfaces.put(m.getInterfaceName(), j);
            }

            m.registerInterfaces(j, apiVersion);
        }

        //for ( Apis api: myAPIs ) {
            JavaInterface j = interfaces.get(api1.getInterfaceName());
            if ( j == null ) {
                j = new JavaInterface();
                j.pkgName = "ch.loway.oss.ari4java.generated";
                j.className = api1.getInterfaceName();
                interfaces.put(api1.getInterfaceName(), j);
            }

            api1.registerInterfaces(j, apiVersion);
        //}

//        //
//        for ( String ifName: interfaces.keySet() ) {
//            JavaInterface ji = interfaces.get(ifName);
////            saveToDisk(ji);
//        }

    }

    /**
     * Generate all files.
     *
     *
     * @throws IOException
     */

    public void generateAllClasses() throws IOException {
        AriBuilderInterface abi = generateInterfaces();
        generateModels();
        generateApis();
        generateProperties( abi );
        generateImplementationClasses( abi );
    }

    /**
     * Generate all interfaces.
     * 
     * @throws IOException
     */

    public AriBuilderInterface generateInterfaces() throws IOException {
        //
        AriBuilderInterface abi = new AriBuilderInterface();
        for ( String ifName: interfaces.keySet() ) {
            JavaInterface ji = interfaces.get(ifName);
            abi.knownInterfaces.add(ifName);
            saveToDisk(ji);
        }

        // generate the AriBuilder class
        saveToDisk( "classes/", "ch.loway.oss.ari4java.generated", "AriBuilder", abi.toString() );
        return abi;
    }

    /**
     * Generates a model.
     * For each model. let's find out the minimal interface it should implement.
     * 
     * @throws IOException
     */

    public void generateModels() throws IOException {
        for (Model m : mymodels) {
            String minIf = m.getInterfaceName();
            JavaInterface ji = interfaces.get(minIf);

            m.setMinimalInterface(ji);
            saveToDisk(m);
        }
    }

    /**
     * Save APIs to disk.
     * 
     * @throws IOException
     */

    public void generateApis() throws IOException {
        for ( Apis api: myAPIs ) {
            String minIf = api.getInterfaceName();
            JavaInterface ji = interfaces.get(minIf);

            api.setMinimalInterface(ji);           
            saveToDisk(api);
        }
    }

    /**
     * Write a properties file for each API version.
     *
     * @throws IOException
     */

    public void generateProperties( AriBuilderInterface abi ) throws IOException {

        Map<String,Set<Model>> mM = new HashMap<String, Set<Model>>();
        Map<String,Set<Apis>> mA = new HashMap<String, Set<Apis>>();

        for ( Apis api: myAPIs ) {
            String ver = api.apiVersion;
            if ( !mA.containsKey(ver) ) {
                mA.put( ver, new HashSet<Apis>() );
            }

            mA.get(ver).add(api);
        }

        for ( Model mod: mymodels ) {
            String ver = mod.apiVersion;
            if ( !mM.containsKey(ver) ) {
                mM.put( ver, new HashSet<Model>() );
            }

            mM.get(ver).add(mod);
        }

        for ( String ver: mM.keySet() ) {
            //writeProperties(ver, mA.get(ver), mM.get(ver));
            writeAriBuilder(ver, abi, mA.get(ver), mM.get(ver));
        }

    }

    /**
     * Generates the translators from the abstract class to the concrete one.
     * 
     * @param abi
     * @throws IOException 
     */
    
    public void generateImplementationClasses( AriBuilderInterface abi ) throws IOException {

        Map<String,ClassTranslator> mTranslators = new HashMap<String,ClassTranslator>();
        
       for ( Apis api: myAPIs ) {
            String ver = api.apiVersion;
            ClassTranslator ct = getClassTranslator(mTranslators, ver);
            ct.setClass( api.className, api.className + "_impl_" + ver );
        }

        for ( Model mod: mymodels ) {
            String ver = mod.apiVersion;
            ClassTranslator ct = getClassTranslator(mTranslators, ver);
            ct.setClass( mod.className, mod.className + "_impl_" + ver );
        }
        
        for ( ClassTranslator ct: mTranslators.values() ) {
                saveToDisk(ct);
        }
    }
    
    
    
    private ClassTranslator getClassTranslator( Map<String,ClassTranslator> mTranslators, String apiVer ) {
        if ( !mTranslators.containsKey( apiVer ) ) {
            ClassTranslator ct = new ClassTranslator();
            ct.apiVersion = apiVer;
            mTranslators.put( apiVer, ct );
        }
        return mTranslators.get( apiVer);
    }
    

    /**
     *
     * @param f
     * @return
     */

    private String genActionClassName(File f) {
        String fileName = f.getName().replace(".json", "");
        return JavaGen.addPrefixAndCapitalize( "Action", fileName );
    }

    private List<Model> loadModels(JsonNode models, File f, String apiVersion) throws IOException {

        List<Model> lModelsAdded = new ArrayList<Model>();

        for (JsonNode modelName : models) {
            // Creazione di un modello
            Model currentModel = new Model();
            String thisModel = txt(modelName.get("id"));
            String thisModelDesc = txt(modelName.get("description"));

            currentModel.setPackageInfo(thisModel, apiVersion);
            currentModel.description = thisModelDesc;
            currentModel.comesFromFile = f.getName();
            currentModel.extendsModel = extendsObject(modelName);
            currentModel.subTypes = subTypes(modelName);
            knownModels.add(thisModel);

            JsonNode properties = modelName.get("properties");
            Iterator<String> propNames = properties.fieldNames();
            while (propNames.hasNext()) {
                String field = propNames.next();
                JsonNode property = properties.get(field);

                String javaType = remapAbstractType(txt(property.get("type")));
                String javaConcreteType = remapConcreteType(txt(property.get("type")), apiVersion);

                String comment = txt(property.get("description"));
                ModelField mf = new ModelField();
                mf.field = field;
                mf.typeInterface = javaType;
                mf.typeConcrete = javaConcreteType;
                mf.comment = comment;
                currentModel.fields.add(mf);
            }

            lModelsAdded.add(currentModel);
            
        }
        for (Model m : lModelsAdded) {
            if (m.subTypes != null) {
                for (Model mm : lModelsAdded) {
                    if (m.subTypes.contains(mm.className)) {
                        mm.extendsModel = remapAbstractType(m.className);
                    }
                }
            }
        }

        return lModelsAdded;
    }


    private Apis loadApis(JsonNode apis, File f, String apiVersion ) throws IOException {

        Apis api = new Apis();

        api.setPackageInfo( genActionClassName(f), apiVersion);


        for (JsonNode apiEntry : apis) {

            Action action = new Action();
            action.path = txt(apiEntry.get("path"));
            action.description = txt(apiEntry.get("description"));
            action.javaFile = f.getName();

            for (JsonNode operation : apiEntry.get("operations")) {
                Operation op = new Operation();
                action.operations.add(op);
                op.method = txt(operation.get("httpMethod"));
                if ("websocket".equalsIgnoreCase(txt(operation.get("upgrade")))) {
                	op.wsUpgrade = true;
                }
                op.nickname = txt(operation.get("nickname"));                
                op.responseInterface = remapAbstractType(txt(operation.get("responseClass")));
                op.responseConcreteClass = remapConcreteType(txt(operation.get("responseClass")), apiVersion);
                op.description = txt(operation.get("summary") ) + "\n" + txt(operation.get("notes") );

                JsonNode parameters = operation.get("parameters");
                if (parameters != null) {
                    for (JsonNode parameter : parameters) {
                        Operation.Param p = new Operation.Param();
                        p.javaType = remapAbstractType(txt(parameter.get("dataType")));
                        p.name = txt(parameter.get("name"));
                        p.required = txt(parameter.get("required")).equalsIgnoreCase("true");
                        p.type =  Operation.ParamType.build( txt(parameter.get("paramType")));

                        op.parms.add(p);

                    }
                }

                JsonNode errorResponses = operation.get("errorResponses");
                if (errorResponses != null) {
                    for (JsonNode errorResponse : errorResponses) {
                        Operation.ErrorResp err = new Operation.ErrorResp();
                        err.code = errorResponse.get("code").asInt();
                        err.reason = errorResponse.get("reason").asText();
                        op.errorCodes.add(err);
                    }
                }


            }

            //System.out.println( action.toString() );
            api.actions.add(action);
        }

        myAPIs.add(api);
        
        return api;

    }

    public void saveToDisk( String baseJavaClasses, String pkgName, String className, String classText  ) throws IOException {

        String fName = myAbsoluteProjectFolder + "/" 
                     + baseJavaClasses
                     + pkgName.replace(".", "/" )
                     + "/"
                     + className + ".java";

        FileWriter outFile = new FileWriter( fName );
        PrintWriter out = new PrintWriter(outFile);
        out.println( classText );
        out.close();

    }

    public void saveToDisk( Model model ) throws IOException {
        saveToDisk( "classes/", model.getModelPackage(), model.getImplName(), model.toString() );
    }

    public void saveToDisk( Apis api ) throws IOException {
        saveToDisk( "classes/", api.getActionsPackage(), api.getImplName(), api.toString() );
    }

    public void saveToDisk( JavaInterface ji ) throws IOException {
        saveToDisk( "classes/", "ch.loway.oss.ari4java.generated", ji.className, ji.toString() );
    }

    public void saveToDisk( ClassTranslator ct ) throws IOException {
        saveToDisk( "classes/", ct.getBaseApiPackage(), ct.getImplName(), ct.toString() );
    }

    
    public void clean(String apiVersion) throws IOException {
    	String base = "classes/ch/loway/oss/ari4java/generated";
    	cleanPath(base+"/"+apiVersion+"/actions");
    	cleanPath(base+"/"+apiVersion+"/models");
    }
    
    private void cleanPath(String path) throws IOException {
    	System.out.println("clean: "+path);
    	File p = new File(path);
    	p.mkdirs();
    	for (File f : p.listFiles()) {
    		if (f.isFile()) {
    			f.delete();
    		}
    	}
    }


    private String txt( JsonNode n ) {
        if ( n == null ) {
            return "";
        } else {
            return n.asText();
        }
    }

    public String extendsObject( JsonNode model ) {
        if ( model.get( "extends" ) != null ) {
            return remapAbstractType( model.get("extends").asText() );
        }
        return "";
    }
    
    public Set<String> subTypes( JsonNode model ) {
    	Set<String> result = new HashSet<String>();
    	if (model.get("subTypes") != null) {
    		JsonNode st = model.get("subTypes");
    		if (st instanceof ArrayNode) {
    			ArrayNode sta = (ArrayNode) st;
    			for (int i = 0; i < sta.size(); i++) {
    				result.add(sta.get(i).asText());
    			}
    		}
    	}
    	return result;
    }

    public String remapAbstractType( String jsonType ) {
        return innerRemapType(jsonType, false, "");
    }

    public String remapConcreteType( String jsonType, String apiVersion ) {
        return innerRemapType(jsonType, true, apiVersion);
    }


    public String innerRemapType( String jsonType, boolean concrete, String apiVersion ) {

        String listAry = "List[";

        if ( jsonType.startsWith( listAry ) ) {
            return (concrete ? "List<" : "List<") + innerRemapType( jsonType.substring(listAry.length(), jsonType.length()-1 ), concrete, apiVersion ) + ">";
        }
        else
        if ( JavaPkgInfo.TypeMap.containsKey( jsonType.toLowerCase() ) ) {
            return JavaPkgInfo.TypeMap.get( jsonType.toLowerCase() );
        }
        else
        {
            return jsonType + ( concrete ? "_impl_" + apiVersion : "" );
        }
    }

//    /**
//     * Writes implemenattion mappings.
//     *
//     *
//     * @param apiVersion
//     * @throws IOException
//     */
//
//    private void writeProperties( String apiVersion, Collection<Apis> apis, Collection<Model> models ) throws IOException {
//    	String base = myAbsoluteProjectFolder 
//                + "/classes"
//                + "/ch/loway/oss/ari4java/generated";
//        String fName = base + "/" + apiVersion + ".properties";
//        FileWriter outFile = new FileWriter(fName);
//        PrintWriter out = new PrintWriter(outFile);
//        out.println("# Implementation mapping for "+apiVersion);
//        for (Apis api : apis) {
//        	String prop = "ch.loway.oss.ari4java.generated."
//        		+ api.getInterfaceName() 
//        		+ " = " + api.getActionsPackage()+"."+api.getImplName();
//        	out.println(prop);
//        }
//        for (Model m : models) {
//        	String prop = "ch.loway.oss.ari4java.generated."
//            	+ m.getInterfaceName() 
//            	+ " = " + m.getModelPackage()+"."+m.getImplName();
//            	out.println(prop);
//        }
//        out.close();
//    }


    private void writeAriBuilder( String apiVersion, AriBuilderInterface abi, Collection<Apis> apis, Collection<Model> models ) throws IOException {

        String thisClass = "AriBuilder_impl_" + apiVersion;
        List<String> ifToImplement = new ArrayList<String>( abi.knownInterfaces );

        StringBuilder sb = new StringBuilder();
        JavaGen.importClasses(sb, "ch.loway.oss.ari4java.generated." + apiVersion,
                Arrays.asList( new String[] {
                    "ch.loway.oss.ari4java.generated." + apiVersion + ".models.*" ,
                    "ch.loway.oss.ari4java.generated." + apiVersion + ".actions.*",
                    "ch.loway.oss.ari4java.generated.Module",
                    "ch.loway.oss.ari4java.generated.*",
                    "ch.loway.oss.ari4java.ARI"
        }));

        sb.append("public class ").append( thisClass ).append( " implements AriBuilder {\n\n");

        for (Apis api : apis) {
            String ifc = api.getInterfaceName();
            ifToImplement.remove(ifc);
            sb.append( AriBuilderInterface.getMethod( ifc, apiVersion) );
        }

        for (Model m : models) {
            String ifc = m.getInterfaceName();
            ifToImplement.remove(ifc);
            sb.append( AriBuilderInterface.getMethod( ifc, apiVersion) );
        }

        // do we have any unimplemented interface?
        for ( String ifc: ifToImplement ) {
            sb.append( AriBuilderInterface.getUnimplemented(ifc) );
        }

        sb.append( "public ARI.ClassFactory getClassFactory() {\n"
                 + " return new ClassTranslator_impl_" + apiVersion + "();\n"
                 + "};\n\n"
        );
        
        sb.append( "};");

        saveToDisk( "classes/", "ch.loway.oss.ari4java.generated." + apiVersion, thisClass, sb.toString() );

    }

    /**
     * Where the ari4java project resides.
     * 
     * @param baseProjectFolder 
     */
    void setProjectFolder(String baseProjectFolder) {
        myAbsoluteProjectFolder =baseProjectFolder;
    }

}
