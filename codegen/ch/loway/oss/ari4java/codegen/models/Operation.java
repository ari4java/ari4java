
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
    public boolean wsUpgrade = false;
    public String nickname = "";
    public String responseConcreteClass = "";
    public String responseInterface = "";

    public String description = "";
    
    public List<Param> parms = new ArrayList<Param>();
    public List<ErrorResp> errorCodes = new ArrayList<ErrorResp>();
    
    private String toParmList(boolean withType) {
        StringBuilder sb = new StringBuilder();
        boolean firstItem = true;
        for ( Param p: parms ) {
            if ( firstItem ) {
                firstItem = false;
            } else {
                sb.append( ", ");
            }
            if (withType) 
            	sb.append( p.javaType ).append( " " );
            sb.append( p.name );
        }
        return sb.toString();
    }
    
    public String toJava( Action parent ) {

        StringBuilder sb = new StringBuilder();

        JavaGen.addBanner(sb, parent.description + "\n\n" + description );

        // search and replace URI parameters
        String stUri = parent.path;
        for ( Param p: parms ) {
            if ( p.type == ParamType.PATH ) {
                stUri = stUri.replace("{" + p.name + "}", "\" + " + p.name + " + \"" );
            }
        }
        // 1. Private helper method
        String helperName = JavaGen.addPrefixAndCapitalize("build", nickname);
        sb.append( "private void ");
        sb.append(helperName);
        sb.append("("+toParmList(true)+") {\n");
        sb.append( "reset();\n");
        sb.append( "url = \"").append( stUri ).append( "\";\n");
        sb.append( "method = \"").append( method ).append( "\";\n");
        for ( Param p: parms ) {
            if ( p.type == ParamType.QUERY ) {
                sb.append( "lParamQuery.add( HttpParam.build( \"").append( p.name)
                        .append( "\", ").append( p.name ).append( ") );\n");
            } else if ( p.type == ParamType.FORM ) {
                sb.append( "lParamForm.add( HttpParam.build( \"").append( p.name)
                        .append( "\", ").append( p.name ).append( ") );\n");
            } else if ( p.type == ParamType.BODY ) {
                sb.append("lParamBody.addAll( HttpParam.build( \"").append( p.name)
                        .append( "\", ").append( p.name ).append( ") );\n");
            };
        }
        for ( ErrorResp er: errorCodes ) {
            sb.append( "lE.add( HttpResponse.build( ").append( er.code)
                    .append( ", \"").append( er.reason ).append( "\") );\n");
        }
        if (wsUpgrade) {
        	sb.append( "wsUpgrade = true;\n");
        }
        sb.append( "}\n\n");
        
        if (!wsUpgrade) {
	        // 2. Synchronous method
	        sb.append( "@Override\n");
	        sb.append( getDefinition() );
	        sb.append( " {\n");
	        // call helper
	        sb.append( helperName+"("+toParmList(false)+");\n");
	        sb.append( "String json = httpActionSync();\n");
	        if ( !responseInterface.equalsIgnoreCase("void")) {
	            
	            String deserializationType = responseConcreteClass + ".class";
	
	            if (responseConcreteClass.startsWith("List<") ) {
	                //  (List<Interface>) mapper.readValue( string, new TypeReference<List<Concrete>>() {});                
	                deserializationType = "new TypeReference<" + responseConcreteClass + ">() {}";
                        sb.append( "return deserializeJsonAsAbstractList( json,\n   ")
	                    .append( deserializationType )
	                    .append(" ); \n");
	            } else {
                        sb.append( "return deserializeJson( json, ")
	                    .append( deserializationType )
	                    .append(" ); \n");
                    }
	
	            
	
	        }
	        sb.append( "}\n\n");
        } else {
        	// Websocket dummy sync method
	        sb.append( "@Override\n");
	        sb.append( getDefinition() );
	        sb.append( " {\n");
	        sb.append( "throw new RestException(\"No synchronous operation on WebSocket\");\n");
	        sb.append( "}\n\n");
        }
        
        if (!wsUpgrade) {
	        // 3. Asynchronous method
	        sb.append( "@Override\n");
	        sb.append( getDefinitionAsync() );
	        sb.append( " {\n");
	        // call helper
	        sb.append( helperName+"("+toParmList(false)+");\n");
	        sb.append( "httpActionAsync(callback");
	        
	        if (responseInterface.equalsIgnoreCase("void")) {
	        	
	        } else {
	        	String deserializationType = responseConcreteClass + ".class";
		        if (responseConcreteClass.startsWith("List<") ) {
		            //  (List<Interface>) mapper.readValue( string, new TypeReference<List<Concrete>>() {});                
		            deserializationType = "new TypeReference<" + responseConcreteClass + ">() {}";
		        }
		        sb.append( ", "+deserializationType );
	        }
	        sb.append( ");\n");
        } else {
        	// Websocket async method
	        sb.append( "@Override\n");
	        sb.append( getDefinitionAsync() );
	        sb.append( " {\n");
	        // call helper
	        sb.append( helperName+"("+toParmList(false)+");\n");
	        sb.append( "httpActionAsync(callback");
	        
	        if (responseInterface.equalsIgnoreCase("void")) {
	        	
	        } else {
	        	String deserializationType = responseConcreteClass + ".class";
		        if (responseConcreteClass.startsWith("List<") ) {
		            //  (List<Interface>) mapper.readValue( string, new TypeReference<List<Concrete>>() {});                
		            deserializationType = "new TypeReference<" + responseConcreteClass + ">() {}";
		        }
		        sb.append( ", "+deserializationType );
	        }
	        sb.append( ");\n");
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

    public String getSignatureAsync() {
        StringBuilder sb = new StringBuilder();
        sb.append("void ")
           .append( nickname );

        for ( Param p: parms ) {
            sb.append( " " )
               .append( p.javaType );
        }
        
        sb.append(" "+JavaGen.addAsyncCallback(responseInterface));

        return sb.toString();
    }

    public String getDefinition() {
        StringBuilder sb = new StringBuilder();

        sb.append( "public " ).append( responseInterface )
           .append( " " ).append( nickname )
           .append( "(");

        sb.append(toParmList(true));

        sb.append( ") throws RestException" );
        return sb.toString();
    }

    public String getDefinitionAsync() {
        StringBuilder sb = new StringBuilder();

        sb.append( "public void " )
           .append( nickname )
           .append( "(" );

        boolean firstItem = true;
        for ( Param p: parms ) {
            if ( firstItem ) {
                firstItem = false;
            } else {
                sb.append( ", ");
            }
            sb.append( p.javaType ).append( " " ).append( p.name );
        }
        if (!firstItem)
        	sb.append(", ");
        sb.append(JavaGen.addAsyncCallback(responseInterface));

        sb.append( ")" );
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
            if ( s.equalsIgnoreCase("body") ) {
                return BODY;
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
