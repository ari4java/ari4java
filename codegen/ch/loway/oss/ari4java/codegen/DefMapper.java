
package ch.loway.oss.ari4java.codegen;

import ch.loway.oss.ari4java.codegen.models.Model;
import ch.loway.oss.ari4java.codegen.models.ModelField;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class DefMapper {

    List<Model> mymodels = new ArrayList<Model>();
    List<String> knownModels = new ArrayList<String>();


    public void load( File f ) throws IOException {

        ObjectMapper om = new ObjectMapper();
        System.out.println( "Loading as: " + f.getAbsolutePath() );

        JsonNode rootNode = om.readTree(f);

        JsonNode models = rootNode.get("models");

        for (JsonNode modelName: models ) {

            // Creazione di un modello
            Model currentModel = new Model();

            String thisModel = txt(modelName.get( "id" ));
            String thisModelDesc = txt( modelName.get("description"));

            currentModel.className = thisModel;
            currentModel.description = thisModelDesc;
            currentModel.comesFromFile = f.getName();            
            currentModel.extendsModel = extendsObject( modelName );            
            knownModels.add(thisModel);
            
            JsonNode properties = modelName.get("properties" );

            Iterator<String> propNames = properties.fieldNames();

            while ( propNames.hasNext() ) {
                String field = propNames.next();
                JsonNode property = properties.get(field);
                String javaType = remapType( txt(property.get("type")) );
                String comment = txt( property.get( "description" )) ;

                ModelField mf = new ModelField();
                mf.field = field;
                mf.type  = javaType;
                mf.comment = comment;

                currentModel.fileds.add(mf);

            }

            mymodels.add( currentModel );
            System.out.println( currentModel.toString() );

            currentModel.toFile("classes/");

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
            return remapType( model.get("extends").asText() );
        }
        return "";

    }

    public String remapType( String jsonType ) {

        String listAry = "List[";

        if ( jsonType.startsWith( listAry ) ) {
            return "List<" + remapType( jsonType.substring(listAry.length(), jsonType.length()-1 )) + ">";
        }
        if ( jsonType.equalsIgnoreCase("string") ) {
            return "String";
        } else
        if ( jsonType.equalsIgnoreCase("long") ) {
            return "long";
        } else
        if ( jsonType.equalsIgnoreCase("int") ) {
            return "int";
        } else
        if ( jsonType.equalsIgnoreCase("Date") ) {
            return "Date";
        } else
        if ( jsonType.equalsIgnoreCase("object") ) {
            return "String";
        } else
        if ( jsonType.equalsIgnoreCase("boolean") ) {
            return "boolean";
        } else
        {
            return jsonType;
        }

//        if ( knownModels.contains(jsonType) ) {
//            return jsonType;
//        }
//        else
//        {
//            throw new IllegalArgumentException("Unknown type: " + jsonType );
//        }
    }







}

// $Log$
//
