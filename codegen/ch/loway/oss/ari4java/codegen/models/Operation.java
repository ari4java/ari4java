
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * $Id$
 * @author lenz
 */
public class Operation {

    public String method = "";
    public String nickname = "";
    public String responseConcreteClass = "";
    public String responseInterface = "";

    public String description = "";
    
    public List<Param> parms = new ArrayList<Param>();
    public List<ErrorResp> errorCodes = new ArrayList<ErrorResp>();
    
    public String toJava( Action parent ) {

        StringBuilder sb = new StringBuilder();

        JavaGen.addBanner(sb, parent.description + "\n\n" + description );

        sb.append( getDefinition() );
        sb.append( " {\n");

        // search and replace URI parameters
        String stUri = parent.path;
        for ( Param p: parms ) {
            if ( p.type == ParamType.PATH ) {
                stUri = stUri.replace("{" + p.name + "}", "\" + " + p.name + " + \"" );
            }
        }

        sb.append( "String url = \"").append( stUri ).append( "\";\n");
        sb.append( "List<BaseAriAction.HttpParam> lParamQuery = new ArrayList<BaseAriAction.HttpParam>();\n");
        sb.append( "List<BaseAriAction.HttpParam> lParamForm = new ArrayList<BaseAriAction.HttpParam>();\n");
        sb.append( "List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();\n");

        for ( Param p: parms ) {
            if ( p.type == ParamType.QUERY ) {
                sb.append( "lParamQuery.add( BaseAriAction.HttpParam.build( \"").append( p.name)
                        .append( "\", ").append( p.name ).append( ") );\n");
            } else
            if ( p.type == ParamType.FORM ) {
                sb.append( "lParamForm.add( BaseAriAction.HttpParam.build( \"").append( p.name)
                        .append( "\", ").append( p.name ).append( ") );\n");
            };

        }

        for ( ErrorResp er: errorCodes ) {
            sb.append( "lE.add( BaseAriAction.HttpResponse.build( ").append( er.code)
                    .append( ", \"").append( er.reason ).append( "\") );\n");
        }

        sb.append( "String json = httpAction( url, \"").append( method ).append( "\", lParamQuery, lParamForm, lE);\n");

        if ( !responseInterface.equalsIgnoreCase("void")) {
            
            String deserializationType = responseConcreteClass + ".class";

            if (responseConcreteClass.startsWith("List<") ) {
                //  (List<Interface>) mapper.readValue( string, new TypeReference<List<Concrete>>() {});                
                deserializationType = "new TypeReference<" + responseConcreteClass + ">() {}";
            }

            sb.append( "return (" )
                    .append( responseInterface )
                    .append( ") deserializeJson( json, ")
                    .append( deserializationType )
                    .append(" ); \n");

        }


        // String url = "/aaa/" + bbb + "/ccc";
        // List<HttpParms> lP = new ArrayList<HttpParms>();
        // addParm( lP, "name", value );
        // List<HttpErrors> lErr = new ArrayList<HttpError>();
        // addParm( lErr, val, "attr");
        // String json = service.do( url, Service.POST, params, errors );
        // return (myName) jsonBuilder( json, myName.class )


        sb.append( "}\n\n");

        return sb.toString();


    }


    public String getSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append( responseInterface )
           .append( " ")
           .append( nickname );

        for ( Param p: parms ) {
            sb.append( " " )
               .append( p.javaType );
        }

        return sb.toString();

    }


    public String getDefinition() {
        StringBuilder sb = new StringBuilder();

        sb.append( "public " ).append( responseInterface )
           .append( " " ).append( nickname )
           .append( "(");

        boolean firstItem = true;
        for ( Param p: parms ) {
            if ( firstItem ) {
                firstItem = false;
            } else {
                sb.append( ", ");
            }
            sb.append( p.javaType ).append( " " ).append( p.name );
        }

        sb.append( ") throws RestException" );
        return sb.toString();
    }




    /**
     * A parameter as defined in Swagger
     */

    public static class Param {
        public String name = "";
        public ParamType type = ParamType.PATH;
        public String javaType = "";
        public boolean required = true;        
    }

    public static enum ParamType {
        PATH,
        QUERY,
        BODY,
        HEADER,
        FORM;

        public static ParamType build( String s ) {
            if ( s.equalsIgnoreCase("path") ) {
                return PATH;
            } else
            if ( s.equalsIgnoreCase("query") ) {
                return QUERY;
            } else
            if ( s.equalsIgnoreCase("form") ) {
                return FORM;
            } else
            {
                throw new IllegalArgumentException("Would not know how to handle parameter of type " + s);
            }
        }

    }


    public static class ErrorResp {
        public int code = 0;
        public String reason = "";
    }

}

// $Log$
//
