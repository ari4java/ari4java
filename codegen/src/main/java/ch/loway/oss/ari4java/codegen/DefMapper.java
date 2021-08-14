
package ch.loway.oss.ari4java.codegen;

import ch.loway.oss.ari4java.codegen.gen.JavaGen;
import ch.loway.oss.ari4java.codegen.gen.JavaInterface;
import ch.loway.oss.ari4java.codegen.gen.JavaPkgInfo;
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
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The model mapper keeps a list of interfaces and actual implementations.
 * <p>
 * $Id$
 *
 * @author lenz
 */
public class DefMapper {

    private final List<String> vers = new ArrayList<>();
    private final List<Model> myModels = new ArrayList<>();
    private final List<Apis> myAPIs = new ArrayList<>();
    private final Map<String, JavaInterface> interfaces = new HashMap<>();
    private final ObjectMapper om = new ObjectMapper();
    private final Formatter codeFormatter = new Formatter();
    private final Set<String> messageInterfaces = new HashSet<>();
    private String outputFolder;

    /**
     * Loads definitions from a module.
     *
     * @param f          The source .json file
     * @param apiVersion The version of the API we are working with
     * @throws IOException when parse error
     */
    public void parseJsonDefinition(File f, String apiVersion) throws IOException {

        if (!vers.contains(apiVersion)) {
            vers.add(apiVersion);
        }

        JsonNode rootNode = om.readTree(f);

        // check swagger version as the parser, Asterisk is currently using 1.1
        String currentVer = rootNode.get("swaggerVersion").asText();
        if (!"1.1".equalsIgnoreCase(currentVer) && !"1.2".equalsIgnoreCase(currentVer)) {
            throw new RuntimeException("Unsupported Swagger Version: " + rootNode.get("swaggerVersion").asText() +
                    " for file " + f.getAbsolutePath());
        }

        List<Model> lModels = loadModels(rootNode.get("models"), f, apiVersion);
        Apis api1 = loadApis(rootNode.get("apis"), f, apiVersion);
        myModels.addAll(lModels);
        myAPIs.add(api1);

        if ("events.json".equals(f.getName())) {
            extractEvents(lModels);
        }
        extractModels(apiVersion);
        extractActions(apiVersion, api1);
        extractActionOperations(apiVersion, api1);

    }

    private void extractActionOperations(String apiVersion, Apis api1) {
        for (Action a : api1.actions) {
            for (Operation o : a.operations) {
                JavaInterface jio = interfaces.get(o.getInterfaceName());
                if (jio == null) {
                    jio = new JavaInterface();
                    jio.pkgName = JavaPkgInfo.GENERATED_PKG_NAME + ".actions.requests";
                    jio.className = o.getInterfaceName();
                    interfaces.put(o.getInterfaceName(), jio);
                }
                o.registerInterfaces(jio, apiVersion);
            }
        }
    }

    private void extractActions(String apiVersion, Apis api1) {
        JavaInterface jia = interfaces.get(api1.getInterfaceName());
        if (jia == null) {
            jia = new JavaInterface();
            jia.pkgName = JavaPkgInfo.GENERATED_PKG_NAME + ".actions";
            jia.className = api1.getInterfaceName();
            interfaces.put(api1.getInterfaceName(), jia);
        }
        api1.registerInterfaces(jia, apiVersion);
    }

    private void extractModels(String apiVersion) {
        for (Model m : myModels) {
            JavaInterface jim = interfaces.get(m.getInterfaceName());
            if (jim == null) {
                jim = new JavaInterface();
                jim.pkgName = JavaPkgInfo.GENERATED_PKG_NAME + ".models";
                jim.className = m.getInterfaceName();
                jim.parent = m.extendsModel;
                interfaces.put(m.getInterfaceName(), jim);
            }
            m.registerInterfaces(jim, apiVersion);
        }
    }

    private void extractEvents(List<Model> lModels) {
        Model typeMessage = null;
        List<Model> otherModels = new ArrayList<>();

        for (Model m : lModels) {
            if (m.className.equalsIgnoreCase("Message")) {
                typeMessage = m;
            } else {
                otherModels.add(m);
            }
        }

        if (typeMessage != null) {

            StringBuilder defs = new StringBuilder();
            for (Model m : otherModels) {
                if (defs.length() > 0) {
                    defs.append(",\n");
                }
                defs.append("  @Type(value = ")
                        .append(m.getImplName())
                        .append(".class, name = \"")
                        .append(m.getInterfaceName())
                        .append("\")");
                messageInterfaces.add(m.getInterfaceName());
            }

            typeMessage.additionalPreambleText = " @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,"
                    + " property = \"type\", visible = true)\n "
                    + "@JsonSubTypes({\n"
                    + defs
                    + "\n})\n";
            typeMessage.imports.add("com.fasterxml.jackson.annotation.JsonSubTypes");
            typeMessage.imports.add("com.fasterxml.jackson.annotation.JsonSubTypes.Type");
            typeMessage.imports.add("com.fasterxml.jackson.annotation.JsonTypeInfo");
        }
    }

    /**
     * Generate all files.
     * @throws IOException when error saving
     * @throws FormatterException when error formatting
     */
    public void generateAllClasses() throws IOException, FormatterException {
        AriBuilderInterface abi = generateInterfaces();
        generateModels();
        generateApis();
        generateProperties(abi);
        generateImplementationClasses();
        generateAriWSCallback();
    }

    /**
     * Generate all interfaces.
     * @return AriBuilderInterface
     * @throws IOException when error saving
     * @throws FormatterException when error formatting
     */
    public AriBuilderInterface generateInterfaces() throws IOException, FormatterException {
        AriBuilderInterface abi = new AriBuilderInterface();
        for (Map.Entry<String, JavaInterface> entry : interfaces.entrySet()) {
            if (entry.getKey().startsWith("Action")) {
                abi.knownInterfaces.add(entry.getKey());
            }
            saveToDisk(entry.getValue());
        }

        // generate the AriBuilder class
        saveToDisk(JavaPkgInfo.GENERATED_PKG_NAME, "AriBuilder", abi.toString());
        return abi;
    }

    /**
     * Generates a model.
     * For each model. let's find out the minimal interface it should implement.
     * @throws IOException when error saving
     * @throws FormatterException when error formatting
     */
    public void generateModels() throws IOException, FormatterException {
        for (Model m : myModels) {
            String minIf = m.getInterfaceName();
            JavaInterface ji = interfaces.get(minIf);

            m.setMinimalInterface(ji);
            saveToDisk(m);
        }
    }

    /**
     * Save APIs to disk.
     * @throws IOException when error saving
     * @throws FormatterException when error formatting
     */
    public void generateApis() throws IOException, FormatterException {
        for (Apis api : myAPIs) {
            String minIf = api.getInterfaceName();
            JavaInterface ji = interfaces.get(minIf);
            api.setMinimalInterface(ji);
            saveToDisk(api);
            for (Action a : api.actions) {
                for (Operation o : a.operations) {
                    saveToDisk(o);
                }
            }
        }
    }

    /**
     * Write a properties file for each API version.
     * @param abi the builder interface
     * @throws IOException when error saving
     * @throws FormatterException when error formatting
     */
    public void generateProperties(AriBuilderInterface abi) throws IOException, FormatterException {
        Map<String, Set<Model>> mM = new HashMap<>();
        Map<String, Set<Apis>> mA = new HashMap<>();

        for (Apis api : myAPIs) {
            String ver = api.apiVersion;
            if (!mA.containsKey(ver)) {
                mA.put(ver, new HashSet<>());
            }
            mA.get(ver).add(api);
        }

        for (Model mod : myModels) {
            String ver = mod.apiVersion;
            if (!mM.containsKey(ver)) {
                mM.put(ver, new HashSet<>());
            }
            mM.get(ver).add(mod);
        }

        StringBuilder sbVerEnums = new StringBuilder();
        StringBuilder sbVerEnum = new StringBuilder();
        JavaGen.addPackage(sbVerEnum, JavaPkgInfo.BASE_PKG_NAME);
        sbVerEnum.append("import ").append(JavaPkgInfo.BASE_PKG_NAME).append(".tools.ARIException;\n");
        sbVerEnum.append("import ").append(JavaPkgInfo.GENERATED_PKG_NAME).append(".AriBuilder;\n");

        for (String ver : vers) {
            writeAriBuilder(ver, abi, mA.get(ver));
            builderEnum(sbVerEnums, ver);
            sbVerEnum.append("import ").append(JavaPkgInfo.GENERATED_PKG_NAME).append(".").append(ver)
                    .append(".AriBuilder").append(JavaPkgInfo.CLAZZ_IMPL_STRING).append(ver).append(";\n");
        }

        String doubleNewLine = "\n\n";
        sbVerEnums.append("  IM_FEELING_LUCKY(null, null);");
        sbVerEnum.append("\npublic enum AriVersion {");
        sbVerEnum.append(doubleNewLine);
        sbVerEnum.append(sbVerEnums);
        sbVerEnum.append(doubleNewLine);
        sbVerEnum.append("  final String versionString;\n");
        sbVerEnum.append("  final AriBuilder builder;");
        sbVerEnum.append(doubleNewLine);
        sbVerEnum.append("  AriVersion(String versionString, AriBuilder builder) {\n");
        sbVerEnum.append("    this.versionString = versionString;\n");
        sbVerEnum.append("    this.builder = builder;\n");
        sbVerEnum.append("  }");
        sbVerEnum.append(doubleNewLine);
        sbVerEnum.append("  public AriBuilder builder() {\n");
        sbVerEnum.append("    if (builder == null) {\n");
        sbVerEnum.append("      throw new IllegalArgumentException(\"This version has no builder. ");
        sbVerEnum.append("Library error for \" + this.name());\n");
        sbVerEnum.append("    } else {\n");
        sbVerEnum.append("      return builder;\n");
        sbVerEnum.append("    }\n");
        sbVerEnum.append("  }");
        sbVerEnum.append(doubleNewLine);
        sbVerEnum.append("  public String version() {\n");
        sbVerEnum.append("    return versionString;\n");
        sbVerEnum.append("  }");
        sbVerEnum.append(doubleNewLine);
        sbVerEnum.append("  public static AriVersion fromVersionString(String version) throws ARIException {\n");
        sbVerEnum.append("    for (AriVersion av: AriVersion.values()) {\n");
        sbVerEnum.append("      if (av.builder != null) {\n");
        sbVerEnum.append("        if (av.versionString.equalsIgnoreCase(version)) {\n");
        sbVerEnum.append("          return av;\n");
        sbVerEnum.append("        }\n");
        sbVerEnum.append("      }\n");
        sbVerEnum.append("    }\n");
        sbVerEnum.append("    throw new ARIException(\"Unknown ARI Version object for \" + version );\n");
        sbVerEnum.append("  }");
        sbVerEnum.append(doubleNewLine);
        sbVerEnum.append("}\n");

        saveToDisk(JavaPkgInfo.BASE_PKG_NAME, "AriVersion", sbVerEnum.toString());

    }

    private void builderEnum(StringBuilder sbVerEnum, String ver) {
        String verNum = ver.replace("ari_", "").replace("_", ".");
        sbVerEnum.append("  ")
                .append(ver.toUpperCase())
                .append("(")
                .append("\"")
                .append(verNum)
                .append("\", new AriBuilder")
                .append(JavaPkgInfo.CLAZZ_IMPL_STRING)
                .append(ver)
                .append("()),\n");
    }

    /**
     * Generates the translators from the abstract class to the concrete one.
     * @throws IOException when error saving
     * @throws FormatterException when error formatting
     */
    public void generateImplementationClasses() throws IOException, FormatterException {
        List<ClassTranslator> lTranslators = new ArrayList<>();

        for (Apis api : myAPIs) {
            String ver = api.apiVersion;
            ClassTranslator ct = getClassTranslator(lTranslators, ver);
            ct.setClass(api.className, api.className + JavaPkgInfo.CLAZZ_IMPL_STRING + ver);
        }

        for (Model mod : myModels) {
            String ver = mod.apiVersion;
            ClassTranslator ct = getClassTranslator(lTranslators, ver);
            ct.setClass(mod.className, mod.className + JavaPkgInfo.CLAZZ_IMPL_STRING + ver);
        }

        for (ClassTranslator ct : lTranslators) {
            saveToDisk(ct);
        }
    }

    private ClassTranslator getClassTranslator(List<ClassTranslator> lTranslators, String apiVer) {
        int idx = lTranslators.indexOf(new ClassTranslator(apiVer));
        if (idx != -1) {
            return lTranslators.get(idx);
        }
        ClassTranslator ct = new ClassTranslator(apiVer);
        ct.apiVersion = apiVer;
        lTranslators.add(ct);
        return ct;
    }

    /**
     * @param f the file
     * @return an action class name
     */
    private String genActionClassName(File f) {
        String fileName = f.getName().replace(".json", "");
        return JavaGen.addPrefixAndCapitalize("Action", fileName);
    }

    private List<Model> loadModels(JsonNode models, File f, String apiVersion) {

        List<Model> lModelsAdded = new ArrayList<>();

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
                // the API for TextMessage changed from returning an object to a list
                if ("endpoints.json".equals(currentModel.comesFromFile) && "TextMessage".equals(thisModel) &&
                        "variables".equals(field) && javaType.startsWith("List")) {
                    mf.field = field + "List";
                }
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

    private Apis loadApis(JsonNode apis, File f, String apiVersion) {
        Apis api = new Apis();
        api.setPackageInfo(genActionClassName(f), apiVersion);

        for (JsonNode apiEntry : apis) {

            Action action = new Action();
            action.path = txt(apiEntry.get("path"));
            action.description = txt(apiEntry.get("description"));
            action.javaFile = f.getName();
            action.api = api;

            loadApiOperations(apiVersion, apiEntry, action);

            api.actions.add(action);
        }

        myAPIs.add(api);

        return api;

    }

    private void loadApiOperations(String apiVersion, JsonNode apiEntry, Action action) {
        for (JsonNode operation : apiEntry.get("operations")) {
            Operation op = new Operation();
            action.operations.add(op);
            op.action = action;
            op.method = txt(operation.get("httpMethod"));
            if ("websocket".equalsIgnoreCase(txt(operation.get("upgrade")))) {
                op.wsUpgrade = true;
            }
            op.nickname = txt(operation.get("nickname"));
            op.responseInterface = remapAbstractType(txt(operation.get("responseClass")));
            op.responseConcreteClass = remapConcreteType(txt(operation.get("responseClass")), apiVersion);
            op.description = txt(operation.get("summary")) + "\n" + txt(operation.get("notes"));
            op.apiVersion = apiVersion;

            loadApiOperationParams(operation, op);
            loadApiOperationErrors(operation, op);

        }
    }

    private void loadApiOperationErrors(JsonNode operation, Operation op) {
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

    private void loadApiOperationParams(JsonNode operation, Operation op) {
        JsonNode parameters = operation.get("parameters");
        if (parameters != null) {
            for (JsonNode parameter : parameters) {
                Operation.Param p = new Operation.Param();
                p.javaType = remapAbstractType(txt(parameter.get("dataType")));
                p.methodArgumentType = JavaPkgInfo.primitiveSignature.containsKey(p.javaType) ?
                        JavaPkgInfo.primitiveSignature.get(p.javaType) : p.javaType;
                p.name = txt(parameter.get("name"));
                p.description = txt(parameter.get("description"));
                p.required = txt(parameter.get("required")).equalsIgnoreCase("true");
                p.type = Operation.ParamType.build(txt(parameter.get("paramType")));
                op.params.add(p);
            }
        }
    }

    public void saveToDisk(String pkgName, String className, String classText) throws IOException, FormatterException {
        String fName = outputFolder
                + pkgName.replace(".", "/")
                + "/"
                + className + ".java";

        File f = new File(fName);
        //noinspection ResultOfMethodCallIgnored
        f.getParentFile().mkdirs(); //NOSONAR
        FileWriter outFile = new FileWriter(f);
        try (PrintWriter out = new PrintWriter(outFile)) {
            out.println(codeFormatter.formatSource(classText));
        }
    }

    public void saveToDisk(Model model) throws IOException, FormatterException {
        saveToDisk(model.getModelPackage(), model.getImplName(), model.toString());
    }

    public void saveToDisk(Apis api) throws IOException, FormatterException {
        saveToDisk(api.getActionsPackage(), api.getImplName(), api.toString());
    }

    public void saveToDisk(Operation operation) throws IOException, FormatterException {
        saveToDisk(operation.getPackage(), operation.getImplName(), operation.toString());
    }

    public void saveToDisk(JavaInterface ji) throws IOException, FormatterException {
        saveToDisk(ji.pkgName, ji.className, ji.toString());
    }

    public void saveToDisk(ClassTranslator ct) throws IOException, FormatterException {
        saveToDisk(ct.getBaseApiPackage(), ct.getImplName(), ct.toString());
    }

    public void clean() {
        deleteFolder(new File(outputFolder));
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files != null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    //noinspection ResultOfMethodCallIgnored
                    f.delete(); //NOSONAR
                }
            }
        }
        //noinspection ResultOfMethodCallIgnored
        folder.delete(); //NOSONAR
    }

    private String txt(JsonNode n) {
        if (n == null) {
            return "";
        } else {
            return n.asText();
        }
    }

    public String extendsObject(JsonNode model) {
        if (model.get("extends") != null) {
            return remapAbstractType(model.get("extends").asText());
        }
        return "";
    }

    public Set<String> subTypes(JsonNode model) {
        Set<String> result = new HashSet<>();
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

    public String remapAbstractType(String jsonType) {
        return innerRemapType(jsonType, false, "");
    }

    public String remapConcreteType(String jsonType, String apiVersion) {
        return innerRemapType(jsonType, true, apiVersion);
    }

    public String innerRemapType(String jsonType, boolean concrete, String apiVersion) {
        String listAry = "List[";
        if (jsonType.startsWith(listAry)) {
            return "List<" + innerRemapType(jsonType.substring(listAry.length(),
                    jsonType.length() - 1), concrete, apiVersion) + ">";
        } else if (JavaPkgInfo.TypeMap.containsKey(jsonType.toLowerCase())) {
            return JavaPkgInfo.TypeMap.get(jsonType.toLowerCase());
        } else {
            return jsonType + (concrete ? JavaPkgInfo.CLAZZ_IMPL_STRING + apiVersion : "");
        }
    }

    private void writeAriBuilder(String apiVersion, AriBuilderInterface abi, Collection<Apis> apis) throws IOException, FormatterException {

        String thisClass = "AriBuilder" + JavaPkgInfo.CLAZZ_IMPL_STRING + apiVersion;
        List<String> ifToImplement = new ArrayList<>(abi.knownInterfaces);

        StringBuilder sb = new StringBuilder();
        JavaGen.importClasses(sb, JavaPkgInfo.GENERATED_PKG_NAME +  "." + apiVersion,
                Arrays.asList(JavaPkgInfo.BASE_PKG_NAME + ".ARI",
                        JavaPkgInfo.GENERATED_PKG_NAME + ".AriBuilder",
                        JavaPkgInfo.GENERATED_PKG_NAME + ".actions.*",
                        JavaPkgInfo.GENERATED_PKG_NAME + "." + apiVersion + ".actions.*"));
        sb.append("public class ").append(thisClass).append(" implements AriBuilder {\n\n");

        for (Apis api : apis) {
            String ifc = api.getInterfaceName();
            ifToImplement.remove(ifc);
            sb.append(AriBuilderInterface.getMethod(ifc, apiVersion));
        }

        // do we have any unimplemented interface?
        for (String ifc : ifToImplement) {
            sb.append(AriBuilderInterface.getUnimplemented(ifc));
        }

        sb.append("public ARI.ClassFactory getClassFactory() {\n return new ClassTranslator" + JavaPkgInfo.CLAZZ_IMPL_STRING);
        sb.append(apiVersion);
        sb.append("();\n};\n\n};");

        saveToDisk(JavaPkgInfo.GENERATED_PKG_NAME + "." + apiVersion, thisClass, sb.toString());

    }

    private void generateAriWSCallback() throws IOException, FormatterException {
        StringBuilder sb = new StringBuilder();
        StringBuilder methods = new StringBuilder();
        JavaGen.importClasses(sb, JavaPkgInfo.GENERATED_PKG_NAME,
                Arrays.asList(
                        JavaPkgInfo.BASE_PKG_NAME + ".tools.AriConnectionEvent",
                        JavaPkgInfo.BASE_PKG_NAME + ".tools.AriWSCallback",
                        JavaPkgInfo.BASE_PKG_NAME + ".tools.RestException",
                        JavaPkgInfo.GENERATED_PKG_NAME + ".models.*",
                        "org.slf4j.Logger",
                        "org.slf4j.LoggerFactory"));
        sb.append("public abstract class AriWSHelper implements AriWSCallback<Message> {\n\n");
        sb.append("private Logger logger = LoggerFactory.getLogger(AriWSHelper.class);\n\n");
        sb.append("@Override\npublic void onSuccess(Message message) {\n");
        AtomicBoolean first = new AtomicBoolean(true);
        messageInterfaces.forEach(i -> {
            if (!i.equals("Event")) {
                if (first.get()) {
                    first.set(false);
                } else {
                    sb.append(" else ");
                }
                sb.append("if (message instanceof ").append(i).append(") {\non").append(i).append("((").append(i).append(") message);\n}");
                methods.append("protected void on").append(i).append("(final ").append(i).append(" message) {\n");
                methods.append("logger.warn(\"Event ").append(i).append(" Unhandled\");\n");
                methods.append("}\n\n");
            }
        });
        sb.append(" else {\nlogger.error(\"Unknown Event - \" + message.getClass());\n}\n}\n\n");
        sb.append("@Override\npublic void onFailure(RestException e) {\nlogger.error(\"Error: {}\", e.getMessage(), e);\n}\n\n");
        sb.append("@Override\npublic void onConnectionEvent(AriConnectionEvent event) {\nlogger.debug(event.name());\n}\n\n");
        sb.append(methods);
        sb.append("\n}");

        saveToDisk(JavaPkgInfo.GENERATED_PKG_NAME, "AriWSHelper", sb.toString());
    }

    /**
     * @param outputFolder the folder
     */
    void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

}
