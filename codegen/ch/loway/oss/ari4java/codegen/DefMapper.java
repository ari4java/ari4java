
package ch.loway.oss.ari4java.codegen;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import ch.loway.oss.ari4java.codegen.genJava.JavaInterface;
import ch.loway.oss.ari4java.codegen.genJava.JavaPkgInfo;
import ch.loway.oss.ari4java.codegen.models.Action;
import ch.loway.oss.ari4java.codegen.models.Apis;
import ch.loway.oss.ari4java.codegen.models.Operation;
import ch.loway.oss.ari4java.codegen.models.Model;
import ch.loway.oss.ari4java.codegen.models.ModelField;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class DefMapper {

    List<Model> mymodels = new ArrayList<Model>();
    List<String> knownModels = new ArrayList<String>();

    List<Apis> myAPIs = new ArrayList<Apis>();

    Map<String, JavaInterface> interfaces = new HashMap<String, JavaInterface>();

    public void load( File f, String apiVersion, boolean modelHasEvents ) throws IOException {

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

        // ora salva su disco
        saveToDisk(api1);
        for ( Model m: lModels ) {
            saveToDisk(m);
        }
        

        // Now generate common APIs
        for ( Model m: mymodels ) {
            JavaInterface j = interfaces.get(m.getInterfaceName());
            if ( j == null ) {
                j = new JavaInterface();
                j.pkgName = "ch.loway.oss.ari4java.generated";
                j.className = m.getInterfaceName();
                interfaces.put(m.getInterfaceName(), j);
            }

            m.registerInterfaces(j);
        }

        for ( Apis api: myAPIs ) {
            JavaInterface j = interfaces.get(api.getInterfaceName());
            if ( j == null ) {
                j = new JavaInterface();
                j.pkgName = "ch.loway.oss.ari4java.generated";
                j.className = api.getInterfaceName();
                interfaces.put(api.getInterfaceName(), j);
            }

            api.registerInterfaces(j);
        }



        //
        for ( String ifName: interfaces.keySet() ) {
            JavaInterface ji = interfaces.get(ifName);
            saveToDisk(ji);
        }

        



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

            System.out.println( action.toString() );
            api.actions.add(action);
        }

        myAPIs.add(api);
        
        return api;

    }

    public void saveToDisk( String baseJavaClasses, String pkgName, String className, String classText  ) throws IOException {

        String fName = baseJavaClasses
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

    public String remapAbstractType( String jsonType ) {
        return innerRemapType(jsonType, false, "");
    }

    public String remapConcreteType( String jsonType, String apiVersion ) {
        return innerRemapType(jsonType, true, apiVersion);
    }


    public String innerRemapType( String jsonType, boolean concrete, String apiVersion ) {

        String listAry = "List[";

        if ( jsonType.startsWith( listAry ) ) {
            return "List<" + innerRemapType( jsonType.substring(listAry.length(), jsonType.length()-1 ), concrete, apiVersion ) + ">";
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





}

// $Log$
//
