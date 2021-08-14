
package ch.loway.oss.ari4java.codegen.models;

import ch.loway.oss.ari4java.codegen.gen.JavaGen;
import ch.loway.oss.ari4java.codegen.gen.JavaInterface;
import ch.loway.oss.ari4java.codegen.gen.JavaPkgInfo;

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
    public List<Param> params = new ArrayList<>();
    public List<ErrorResp> errorCodes = new ArrayList<>();
    public String apiVersion;
    public Action action;
    private JavaInterface ji;

    private String toParmList(boolean withType, boolean forComment) {
        StringBuilder sb = new StringBuilder();
        boolean firstItem = true;
        for (Param p : params) {
            if (p.required) {
                if (forComment) {
                    sb.append(p.getParamComment(null));
                } else {
                    if (firstItem) {
                        firstItem = false;
                    } else {
                        sb.append(", ");
                    }
                    if (withType)
                        sb.append(p.javaType).append(" ");
                    sb.append(p.name);
                }
            }
        }
        if (forComment) {
            sb.append("@return ").append(getInterfaceName()).append("\n");
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        JavaGen.importClasses(sb, getPackage(), Arrays.asList("java.util.Date",
                "java.util.List",
                "java.util.Map",
                "java.util.HashMap",
                "java.util.ArrayList",
                "java.net.URLEncoder",
                "com.fasterxml.jackson.core.type.TypeReference",
                JavaPkgInfo.BASE_PKG_NAME + ".ARI",
                JavaPkgInfo.BASE_PKG_NAME + ".tools.*",
                JavaPkgInfo.GENERATED_PKG_NAME + ".*",
                JavaPkgInfo.GENERATED_PKG_NAME + ".actions.*",
                JavaPkgInfo.GENERATED_PKG_NAME + ".actions.requests.*",
                JavaPkgInfo.GENERATED_PKG_NAME + ".models.Module",
                JavaPkgInfo.GENERATED_PKG_NAME + ".models.*",
                JavaPkgInfo.GENERATED_PKG_NAME + "." + apiVersion + ".actions.*",
                JavaPkgInfo.GENERATED_PKG_NAME + "." + apiVersion + ".actions.requests.*",
                JavaPkgInfo.GENERATED_PKG_NAME + "." + apiVersion + ".models.*"));

        JavaGen.emptyLines(sb, 2);

        JavaInterface interfaces = ji.createScratchCopy();

        sb.append("public class ").append(getImplName())
                .append(" extends BaseAriAction ")
                .append(" implements ").append(getInterfaceName()).append(" {\n");
        for (Param p : params) {
            sb.append("  private ").append(p.javaType).append(" ").append(p.name).append(";\n");
        }
        generateConstructor(sb);
        generateParams(sb, interfaces);

        // 1. Private build method
        generateBuildMethod(sb);
        // 2. Synchronous execute method
        generateActionSync(sb, interfaces);
        // 3. Asynchronous execute method
        generateActionAsync(sb, interfaces);
        // 4. Missing methods
        sb.append(interfaces.getCodeToImplementMissingSignatures());

        sb.append("}\n");
        return sb.toString();

    }

    private void generateActionAsync(StringBuilder sb, JavaInterface interfaces) {
        sb.append("  ").append(getAsyncExecuteDefinition()).append(" {\n");
        sb.append("    httpActionAsync(build(), callback");
        if (!responseInterface.equalsIgnoreCase("void")) {
            String deserializationType = responseConcreteClass + ".class";
            if (responseConcreteClass.startsWith("List<")) {
                deserializationType = "new TypeReference<" + responseConcreteClass + ">() {}";
            }
            sb.append(", ");
            sb.append(deserializationType);
        }
        sb.append(");\n");
        sb.append("  }\n\n");
        interfaces.removeSignature(getAsyncExecuteSignature());
    }

    private void generateActionSync(StringBuilder sb, JavaInterface interfaces) {
        sb.append("  ").append(getSyncExecuteDefinition()).append(" {\n");
        if (wsUpgrade) {
            // Websocket dummy sync method
            sb.append("    throw new RestException(\"No synchronous operation on WebSocket\");\n");
        } else if (responseInterface.equalsIgnoreCase("byte[]")) {
            sb.append("    return httpActionSyncAsBytes(build());\n");
        } else if (responseInterface.equalsIgnoreCase("void")) {
                sb.append("    httpActionSync(build());\n");
        } else {
            sb.append("    String json = httpActionSync(build());\n");
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
        sb.append("  }\n\n");
        interfaces.removeSignature(getSyncExecuteSignature());
    }

    private void generateBuildMethod(StringBuilder sb) {
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
            } else if (p.type == ParamType.BODY) {
                sb.append("    ariRequest.setBodyField(\"").append(p.name)
                        .append("\", ").append(p.name).append(");\n");
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
    }

    private void generateParams(StringBuilder sb, JavaInterface interfaces) {
        for (Param p : params) {
            sb.append(p.getDefinition(getInterfaceName())).append(" {\n")
                    .append("    this.").append(p.name).append(" = ").append(p.name).append(";\n")
                    .append("    return this;\n")
                    .append("  }\n\n");
            interfaces.removeSignature(p.getSignature(getInterfaceName()));
            if (p.javaType.equals("Map<String,String>")) {
                sb.append(p.getDefinitionForAddToMap(getInterfaceName())).append(" {\n")
                        .append("    if (this.").append(p.name).append(" == null) {\n")
                        .append("        this.").append(p.name).append(" = new HashMap<>();\n")
                        .append("    }\n")
                        .append("    this.").append(p.name).append(".put(key, value);\n")
                        .append("    return this;\n")
                        .append("  }\n\n");
                interfaces.removeSignature(p.getSignatureForAddToMap(getInterfaceName()));
            }
        }
    }

    private void generateConstructor(StringBuilder sb) {
        sb.append("\n  ").append(getConstructorDefinition()).append(" {\n");
        for (Param p : params) {
            if (p.required) {
                sb.append("    this.").append(p.name).append(" = ").append(p.name).append(";\n");
            }
        }
        sb.append("  }\n\n");
    }

    private String getSyncExecuteSignature() {
        return responseInterface + " execute";
    }

    private String getSyncExecuteDefinition() {
        return "public " + responseInterface + " execute() throws RestException";
    }

    private String getAsyncExecuteSignature() {
        return "void execute " + JavaGen.addAsyncCallback(responseInterface);
    }

    private String getAsyncExecuteDefinition() {
        return "public void execute(" + JavaGen.addAsyncCallback(responseInterface) + ")";
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
        sb.append(toParmList(false, false)).append(");\n");
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
        return "public " + getImplName() + "(" + toParmList(true, false) + ")";
    }

    public String getDefinition() {
        return "public " + getInterfaceName() + " " + nickname + "(" + toParmList(true, false) +
                ") throws RestException";
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
            j.iKnow(javaSignature, definition,p.getParamComment(getInterfaceName()), apiVersion);
            if (p.javaType.equals("Map<String,String>")) {
                javaSignature = p.getSignatureForAddToMap(getInterfaceName());
                definition = p.getDefinitionForAddToMap(getInterfaceName());
                j.iKnow(javaSignature, definition, "@param key the attribute name\n" +
                        "@param value the attribute value\n" +
                        "@return " + getInterfaceName() + "\n", apiVersion);
            }
        }
        String returnComment = "\n@return " + responseInterface;
        if ("void".equalsIgnoreCase(responseInterface)) {
            returnComment = "";
        }
        j.iKnow(getSyncExecuteSignature(), getSyncExecuteDefinition(), "@throws RestException an error" + returnComment, apiVersion);
        j.iKnow(getAsyncExecuteSignature(), getAsyncExecuteDefinition(), "@param callback AriCallback&gt;" + responseInterface.replaceAll("^void$", "Void") + "&lt;", apiVersion);
    }

    public String getComment() {
        return description + "\n" + toParmList(false, true) + "\n@throws RestException an error";
    }

    /**
     * A parameter as defined in Swagger
     */
    public static class Param {
        public String name = "";
        public String description = "";
        public ParamType type = ParamType.PATH;
        public String javaType = "";
        public String methodArgumentType = "";
        public boolean required = true;

        public String getSignature(String returnType) {
            return returnType + " " + JavaGen.addPrefixAndCapitalize("set", name) + " " + javaType;
        }
        
        public String getDefinition(String returnType) {
            return "  public " + returnType + " " + JavaGen.addPrefixAndCapitalize("set", name) +
                    "(" + methodArgumentType + " " + name + ")";
        }

        public String getSignatureForAddToMap(String returnType) {
            return returnType + " " + JavaGen.addPrefixAndCapitalize("add", name) + " " + javaType;
        }

        public String getDefinitionForAddToMap(String returnType) {
            return "  public " + returnType + " " + JavaGen.addPrefixAndCapitalize("add", name) +
                    "(String key, String value)";
        }

        public String getParamComment(String returnType) {
            StringBuilder sb = new StringBuilder();
            sb.append("@param ").append(name).append(" ");
            if (description == null || description.trim().isEmpty()) {
                sb.append(name);
            } else {
                sb.append(description);
            }
            sb.append("\n");
            if (returnType != null) {
                sb.append("@return ").append(returnType).append("\n");
            }
            return sb.toString();
        }
    }

    public enum ParamType {
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
