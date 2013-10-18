
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
    public String responseClass = "";

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
            if ( p.inUri ) {
                stUri = stUri.replace("{" + p.name + "}", "\" + " + p.name + " + \"" );
            }
        }

        sb.append( "String url = \"").append( stUri ).append( "\";\n");
        sb.append( "List<BaseAriAction.HttpParam> lP = new ArrayList<BaseAriAction.HttpParam>();\n");
        sb.append( "List<BaseAriAction.HttpResponse> lE = new ArrayList<BaseAriAction.HttpResponse>();\n");

        for ( Param p: parms ) {
            if ( !p.inUri ) {
                sb.append( "lP.add( BaseAriAction.HttpParam.build( \"").append( p.name)
                        .append( "\", ").append( p.name ).append( ") );\n");
            }
        }

        for ( ErrorResp er: errorCodes ) {
            sb.append( "lE.add( BaseAriAction.HttpResponse.build( ").append( er.code)
                    .append( ", \"").append( er.reason ).append( "\") );\n");
        }

        sb.append( "String json = httpAction( url, \"").append( method ).append( "\", lP, lE);\n");

        if ( !responseClass.equalsIgnoreCase("void")) {

            String responseObj = responseClass;
            if (responseObj.startsWith("List<") ) {
                responseObj = "List";
            }

            sb.append( "return (" )
                    .append( responseClass )
                    .append( ") deserializeJson( json, ")
                    .append(responseObj)
                    .append(".class); \n");
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
        sb.append( responseClass )
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

        sb.append( "public " ).append( responseClass )
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





    public static class Param {
        public String name = "";
        public String type = "";
        public String javaType = "";
        public boolean required = true;
        public boolean inUri = false;
    }

    public static class ErrorResp {
        public int code = 0;
        public String reason = "";
    }

}

// $Log$
//
