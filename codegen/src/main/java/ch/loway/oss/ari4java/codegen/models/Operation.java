
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.genJava.JavaGen;
import ch.loway.oss.ari4java.codegen.genJava.JavaInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * $Id$
 *
 * @author lenz
 */
public class Operation {

    public String method = "";
    public boolean wsUpgrade = false;
    public String nickname = "";
    public String responseConcreteClass = "";
    public String responseInterface = "";
    public String description = "";
    public List<Param> params = new ArrayList<Param>();
    public List<ErrorResp> errorCodes = new ArrayList<ErrorResp>();
    public String apiVersion;
    public Action action;
    private JavaInterface ji;

    private String toParmList(boolean withType, boolean onlyRequired) {
        StringBuilder sb = new StringBuilder();
        boolean firstItem = true;
        for (Param p : params) {
            if (onlyRequired && !p.required) {
                continue;
            }
            if (firstItem) {
                firstItem = false;
            } else {
                sb.append(", ");
            }
            if (withType)
                sb.append(p.javaType).append(" ");
            sb.append(p.name);
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        JavaGen.importClasses(sb, getPackage(), Arrays.asList(new String[]{
                "java.util.Date",
                "java.util.List",
                "java.util.Map",
                "java.util.ArrayList",
                "java.net.URLEncoder",
                "ch.loway.oss.ari4java.ARI",
                "ch.loway.oss.ari4java.tools.*",
                "com.fasterxml.jackson.core.type.TypeReference",
                "ch.loway.oss.ari4java.generated.*",
                "ch.loway.oss.ari4java.generated.actions.*",
                "ch.loway.oss.ari4java.generated.actions.requests.*",
                "ch.loway.oss.ari4java.generated.models.Module",
                "ch.loway.oss.ari4java.generated.models.*",
                "ch.loway.oss.ari4java.generated." + apiVersion + ".actions.*",
                "ch.loway.oss.ari4java.generated." + apiVersion + ".actions.requests.*",
                "ch.loway.oss.ari4java.generated." + apiVersion + ".models.*"
        }));

        JavaGen.emptyLines(sb, 2);

        JavaInterface interfaces = ji.createScratchCopy();

        sb.append("public class ").append(getImplName())
                .append(" extends BaseAriAction ")
                .append(" implements ").append(getInterfaceName()).append(" {\n");
        for (Param p : params) {
            sb.append("  private ").append(p.javaType).append(" ").append(p.name).append(";\n");
        }
        sb.append("\n  ").append(getConstructorDefinition()).append(" {\n");
        for (Param p : params) {
            if (p.required) {
                sb.append("    this.").append(p.name).append(" = ").append(p.name).append(";\n");
            }
        }
        sb.append("  }\n\n");
        for (Param p : params) {
            sb.append(p.getDefinition(getInterfaceName())).append(" {\n")
                    .append("    this.").append(p.name).append(" = ").append(p.name).append(";\n")
                    .append("    return this;\n")
                    .append("  }\n\n");
            interfaces.removeSignature(p.getSignature(getInterfaceName()));
        }

        // 1. Private build method
        // search and replace URI parameters
        String stUri = action.path;
        for (Param p : params) {
            if (p.type == ParamType.PATH) {
                stUri = stUri.replace("{" + p.name + "}", "\" + encodeUrlPart(" + p.name + ") + \"");
            }
        }
        sb.append("  private AriRequest build() {\n");
        sb.append("    AriRequest ariRequest = new AriRequest(\"").append(stUri).append("\", \"").append(method).append("\");\n");
        for (Param p : params) {
            if (p.type == ParamType.QUERY) {
                sb.append("    ariRequest.addParamQuery(HttpParam.build( \"").append(p.name)
                        .append("\", ").append(p.name).append("));\n");
            } else if (p.type == ParamType.FORM) {
                sb.append("    ariRequest.addParamForm(HttpParam.build( \"").append(p.name)
                        .append("\", ").append(p.name).append("));\n");
            } else if (p.type == ParamType.BODY) {
                if ("Map<String,String>".equals(p.javaType)) {
                    sb.append("    ariRequest.addParamBody(HttpParam.build( \"").append(p.name)
                            .append("\", ").append(p.name).append("));\n");
                } else if ("Object".equals(p.javaType)) {
                    sb.append("    ariRequest.addParamBody(HttpParam.build(\"").append(p.name)
                            .append("\", serializeToJson( ").append(p.name).append(")));\n");
                } else {
                    sb.append("    ariRequest.addParamBody(HttpParam.build( \"").append(p.name)
                            .append("\", ").append(p.name).append("));\n");
                }
            }
        }
        for (ErrorResp er : errorCodes) {
            sb.append("    ariRequest.addErrorResponse(HttpResponse.build(").append(er.code)
                    .append(", \"").append(er.reason).append("\"));\n");
        }
        if (wsUpgrade) {
            sb.append("    ariRequest.setWsUpgrade(true);\n");
        }
        sb.append("    return ariRequest;\n");
        sb.append("  }\n\n");

        // 2. Synchronous execute method
        sb.append("  ").append(getSyncExecuteDefinition()).append(" {\n");
        if (wsUpgrade) {
            // Websocket dummy sync method
            sb.append("    throw new RestException(\"No synchronous operation on WebSocket\");\n");
        } else {
            if (responseInterface.equalsIgnoreCase("byte[]")) {
                sb.append("    return httpActionSyncAsBytes(build());\n");
            } else {
                sb.append("    String json = httpActionSync(build());\n");
                if (!responseInterface.equalsIgnoreCase("void")) {
                    String deserializationType = responseConcreteClass + ".class";
                    if (responseConcreteClass.startsWith("List<")) {
                        deserializationType = "new TypeReference<" + responseConcreteClass + ">() {}";
                        sb.append("    return deserializeJsonAsAbstractList(json, ")
                                .append(deserializationType)
                                .append(");\n");
                    } else {
                        sb.append("    return deserializeJson(json, ")
                                .append(deserializationType)
                                .append(");\n");
                    }
                }
            }
        }
        sb.append("  }\n\n");
        interfaces.removeSignature(getSyncExecuteSignature());

        // 3. Asynchronous execute method
        sb.append("  ").append(getAsyncExecuteDefinition()).append(" {\n");
        sb.append("    httpActionAsync(build(), callback");
        if (!responseInterface.equalsIgnoreCase("void")) {
            String deserializationType = responseConcreteClass + ".class";
            if (responseConcreteClass.startsWith("List<")) {
                deserializationType = "new TypeReference<" + responseConcreteClass + ">() {}";
            }
            sb.append(", " + deserializationType);
        }
        sb.append(");\n");
        sb.append("  }\n\n");
        interfaces.removeSignature(getAsyncExecuteSignature());

        sb.append(interfaces.getCodeToImplementMissingSignatures());

        sb.append("}\n");
        return sb.toString();

    }

    private String getSyncExecuteSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append(responseInterface).append(" execute");
        return sb.toString();
    }

    private String getSyncExecuteDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("public ").append(responseInterface).append(" execute() throws RestException");
        return sb.toString();
    }

    private String getAsyncExecuteSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append("void execute ").append(JavaGen.addAsyncCallback(responseInterface));
        return sb.toString();
    }

    private String getAsyncExecuteDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("public void execute(").append(JavaGen.addAsyncCallback(responseInterface)).append(")");
        return sb.toString();
    }

    public String getPackage() {
        return "ch.loway.oss.ari4java.generated." + apiVersion + ".actions.requests";
    }

    public String toJava() {

        StringBuilder sb = new StringBuilder();
        JavaGen.addBanner(sb, action.description + "\n" + description);

        sb.append("@Override\n");
        sb.append(getDefinition());
        sb.append(" {\n  ");
        sb.append(getImplName()).append("  request = new ").append(getImplName()).append("(");
        sb.append(toParmList(false, true)).append(");\n");
        sb.append("  request.setHttpClient(this.getHttpClient());\n");
        sb.append("  request.setWsClient(this.getWsClient());\n");
        sb.append("  request.setLiveActionList(this.getLiveActionList());\n");
        sb.append("  request.setForcedResponse(this.getForcedResponse());\n");
        sb.append("  return request;\n");
        sb.append("}\n\n");

        return sb.toString();
    }

    public String getSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append(getInterfaceName())
                .append(" ")
                .append(nickname);

        for (Param p : params) {
            if (p.required) {
                sb.append(" ").append(p.javaType);
            }
        }
        return sb.toString();
    }

    public String getConstructorDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("public ").append(getImplName()).append("(")
                .append(toParmList(true, true)).append(")");
        return sb.toString();
    }

    public String getDefinition() {
        StringBuilder sb = new StringBuilder();

        sb.append("public ").append(getInterfaceName())
                .append(" ").append(nickname)
                .append("(");

        sb.append(toParmList(true, true));

        sb.append(") throws RestException");
        return sb.toString();
    }

    public String getInterfaceName() {
        String s = action.api.getInterfaceName().substring(6);
        s += nickname.substring(0, 1).toUpperCase() + nickname.substring(1);
        if (!nickname.equalsIgnoreCase(method)) {
            s += method.substring(0, 1).toUpperCase() + method.substring(1).toLowerCase();
        }
        s += "Request";
        return s;
    }

    public String getImplName() {
        return getInterfaceName() + "_impl_" + apiVersion;
    }

    public void registerInterfaces(JavaInterface j, String apiVersion) {
        ji = j;
        for (Param p : params) {
            String javaSignature = p.getSignature(getInterfaceName());
            String definition = p.getDefinition(getInterfaceName());
            j.iKnow(javaSignature, definition, "", apiVersion);
        }
        j.iKnow(getSyncExecuteSignature(), getSyncExecuteDefinition(), "", apiVersion);
        j.iKnow(getAsyncExecuteSignature(), getAsyncExecuteDefinition(), "", apiVersion);
    }

    /**
     * A parameter as defined in Swagger
     */
    public static class Param {
        public String name = "";
        public ParamType type = ParamType.PATH;
        public String javaType = "";
        public String methodArgumentType = "";
        public boolean required = true;

        public String getSignature(String returnType) {
            StringBuilder sb = new StringBuilder();
            sb.append(returnType).append(" ").append(JavaGen.addPrefixAndCapitalize("set", name));
            sb.append(" ").append(javaType);
            return sb.toString();
        }
        
        public String getDefinition(String returnType) {
            StringBuilder sb = new StringBuilder();
            sb.append("  public ").append(returnType).append(" ").append(JavaGen.addPrefixAndCapitalize("set", name))
                    .append("(").append(methodArgumentType).append(" ").append(name).append(")");
            return sb.toString();
        }
        
    }

    public static enum ParamType {
        PATH,
        QUERY,
        BODY,
        HEADER,
        FORM;

        public static ParamType build(String s) {
            if (s.equalsIgnoreCase("path")) {
                return PATH;
            } else if (s.equalsIgnoreCase("query")) {
                return QUERY;
            } else if (s.equalsIgnoreCase("form")) {
                return FORM;
            } else if (s.equalsIgnoreCase("body")) {
                return BODY;
            } else {
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
