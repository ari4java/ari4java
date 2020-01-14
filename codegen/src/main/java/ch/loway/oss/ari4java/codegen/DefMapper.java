
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
 * <p>
 * $Id$
 *
 * @author lenz
 */
public class DefMapper {

    private List<String> vers = new ArrayList<>();
    private List<Model> mymodels = new ArrayList<>();
    private List<Apis> myAPIs = new ArrayList<>();
    private Map<String, JavaInterface> interfaces = new HashMap<>();
    private String outputFolder;
    private ObjectMapper om = new ObjectMapper();

    /**
     * Loads definitions from a module.
     *
     * @param f          The source .json file
     * @param apiVersion The version of the API we are working with
     * @throws IOException
     */
    public void parseJsonDefinition(File f, String apiVersion) throws IOException {

        if (!vers.contains(apiVersion)) {
            vers.add(apiVersion);
        }

        JsonNode rootNode = om.readTree(f);
        List<Model> lModels = loadModels(rootNode.get("models"), f, apiVersion);
        Apis api1 = loadApis(rootNode.get("apis"), f, apiVersion);

        mymodels.addAll(lModels);
        myAPIs.add(api1);

        if ("events.json".equals(f.getName())) {

            Model typeMessage = null;
            List<Model> otherModels = new ArrayList<Model>();

            for (Model m : lModels) {
                if (m.className.equalsIgnoreCase("Message")) {
                    typeMessage = m;
                } else {
                    otherModels.add(m);
                }
            }

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

        // Now generate the interface
        for (Model m : mymodels) {
            JavaInterface jim = interfaces.get(m.getInterfaceName());
            if (jim == null) {
                jim = new JavaInterface();
                jim.pkgName = "ch.loway.oss.ari4java.generated.models";
                jim.className = m.getInterfaceName();
                jim.parent = m.extendsModel;
                interfaces.put(m.getInterfaceName(), jim);
            }
            m.registerInterfaces(jim, apiVersion);
        }

        JavaInterface jia = interfaces.get(api1.getInterfaceName());
        if (jia == null) {
            jia = new JavaInterface();
            jia.pkgName = "ch.loway.oss.ari4java.generated.actions";
            jia.className = api1.getInterfaceName();
            interfaces.put(api1.getInterfaceName(), jia);
        }
        api1.registerInterfaces(jia, apiVersion);

        for (Action a : api1.actions) {
            for (Operation o : a.operations) {
                JavaInterface jio = interfaces.get(o.getInterfaceName());
                if (jio == null) {
                    jio = new JavaInterface();
                    jio.pkgName = "ch.loway.oss.ari4java.generated.actions.requests";
                    jio.className = o.getInterfaceName();
                    interfaces.put(o.getInterfaceName(), jio);
                }
                o.registerInterfaces(jio, apiVersion);
            }
        }

    }

    /**
     * Generate all files.
     *
     * @throws IOException
     */
    public void generateAllClasses() throws IOException {
        AriBuilderInterface abi = generateInterfaces();
        generateModels();
        generateApis();
        generateProperties(abi);
        generateImplementationClasses(abi);
    }

    /**
     * Generate all interfaces.
     *
     * @throws IOException
     */
    public AriBuilderInterface generateInterfaces() throws IOException {
//        System.out.println("generateInterfaces");

        AriBuilderInterface abi = new AriBuilderInterface();
        for (String ifName : interfaces.keySet()) {
            JavaInterface ji = interfaces.get(ifName);
            if (ifName.startsWith("Action")) {
                abi.knownInterfaces.add(ifName);
            }
            saveToDisk(ji);
        }

        // generate the AriBuilder class
        saveToDisk("ch.loway.oss.ari4java.generated", "AriBuilder", abi.toString());
        return abi;
    }

    /**
     * Generates a model.
     * For each model. let's find out the minimal interface it should implement.
     *
     * @throws IOException
     */
    public void generateModels() throws IOException {
//        System.out.println("generateModels");
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
//        System.out.println("generateApis");
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
     *
     * @throws IOException
     */
    public void generateProperties(AriBuilderInterface abi) throws IOException {
//        System.out.println("generateProperties");

        Map<String, Set<Model>> mM = new HashMap<String, Set<Model>>();
        Map<String, Set<Apis>> mA = new HashMap<String, Set<Apis>>();

        for (Apis api : myAPIs) {
            String ver = api.apiVersion;
            if (!mA.containsKey(ver)) {
                mA.put(ver, new HashSet<Apis>());
            }

            mA.get(ver).add(api);
        }

        for (Model mod : mymodels) {
            String ver = mod.apiVersion;
            if (!mM.containsKey(ver)) {
                mM.put(ver, new HashSet<Model>());
            }

            mM.get(ver).add(mod);
        }

        StringBuilder sbVerEnums = new StringBuilder();
        StringBuilder sbVerEnum = new StringBuilder();
        JavaGen.addPackage(sbVerEnum, "ch.loway.oss.ari4java");
        sbVerEnum.append("import ch.loway.oss.ari4java.tools.ARIException;\n");
        sbVerEnum.append("import ch.loway.oss.ari4java.generated.AriBuilder;\n");

        for (String ver : vers) {
            writeAriBuilder(ver, abi, mA.get(ver), mM.get(ver));
            builderEnum(sbVerEnums, ver);
            sbVerEnum.append("import ch.loway.oss.ari4java.generated.").append(ver)
                    .append(".AriBuilder_impl_").append(ver).append(";\n");
        }

        sbVerEnums.append("  IM_FEELING_LUCKY(null, null);");
        sbVerEnum.append("\npublic enum AriVersion {\n\n");
        sbVerEnum.append(sbVerEnums);
        sbVerEnum.append("\n\n");
        sbVerEnum.append("  final String versionString;\n");
        sbVerEnum.append("  final AriBuilder builder;\n");
        sbVerEnum.append("\n");
        sbVerEnum.append("  AriVersion(String versionString, AriBuilder builder) {\n");
        sbVerEnum.append("    this.versionString = versionString;\n");
        sbVerEnum.append("    this.builder = builder;\n");
        sbVerEnum.append("  }\n\n");
        sbVerEnum.append("  public AriBuilder builder() {\n");
        sbVerEnum.append("    if (builder == null) {\n");
        sbVerEnum.append("      throw new IllegalArgumentException(\"This version has no builder. Library error for \" + this.name());\n");
        sbVerEnum.append("    } else {\n");
        sbVerEnum.append("      return builder;\n");
        sbVerEnum.append("    }\n");
        sbVerEnum.append("  }\n\n");
        sbVerEnum.append("  public String version() {\n");
        sbVerEnum.append("    return versionString;\n");
        sbVerEnum.append("  }\n\n");
        sbVerEnum.append("  public static AriVersion fromVersionString(String version) throws ARIException {\n");
        sbVerEnum.append("    for (AriVersion av: AriVersion.values()) {\n");
        sbVerEnum.append("      if (av.builder != null) {\n");
        sbVerEnum.append("        if (av.versionString.equalsIgnoreCase(version)) {\n");
        sbVerEnum.append("          return av;\n");
        sbVerEnum.append("        }\n");
        sbVerEnum.append("      }\n");
        sbVerEnum.append("    }\n");
        sbVerEnum.append("    throw new ARIException(\"Unknown ARI Version object for \" + version );\n");
        sbVerEnum.append("  }\n\n");
        sbVerEnum.append("}\n");
        saveToDisk("ch.loway.oss.ari4java", "AriVersion", sbVerEnum.toString());

    }

    private void builderEnum(StringBuilder sbVerEnum, String ver) {
        String verNum = ver.replace("ari_", "").replace("_", ".");
        sbVerEnum.append("  ")
                .append(ver.toUpperCase())
                .append("(")
                .append("\"")
                .append(verNum)
                .append("\", new AriBuilder_impl_")
                .append(ver)
                .append("()),\n");
    }

    /**
     * Generates the translators from the abstract class to the concrete one.
     *
     * @param abi
     * @throws IOException
     */
    public void generateImplementationClasses(AriBuilderInterface abi) throws IOException {
//        System.out.println("generateImplementationClasses");

        List<ClassTranslator> lTranslators = new ArrayList<ClassTranslator>();

        for (Apis api : myAPIs) {
            String ver = api.apiVersion;
            ClassTranslator ct = getClassTranslator(lTranslators, ver);
            ct.setClass(api.className, api.className + "_impl_" + ver);
        }

        for (Model mod : mymodels) {
            String ver = mod.apiVersion;
            ClassTranslator ct = getClassTranslator(lTranslators, ver);
            ct.setClass(mod.className, mod.className + "_impl_" + ver);
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
     * @param f
     * @return
     */
    private String genActionClassName(File f) {
        String fileName = f.getName().replace(".json", "");
        return JavaGen.addPrefixAndCapitalize("Action", fileName);
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

    private Apis loadApis(JsonNode apis, File f, String apiVersion) throws IOException {

        Apis api = new Apis();

        api.setPackageInfo(genActionClassName(f), apiVersion);


        for (JsonNode apiEntry : apis) {

            Action action = new Action();
            action.path = txt(apiEntry.get("path"));
            action.description = txt(apiEntry.get("description"));
            action.javaFile = f.getName();
            action.api = api;

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

                JsonNode parameters = operation.get("parameters");
                if (parameters != null) {
                    for (JsonNode parameter : parameters) {
                        Operation.Param p = new Operation.Param();
                        p.javaType = remapAbstractType(txt(parameter.get("dataType")));
                        p.methodArgumentType = JavaPkgInfo.primitiveSignature.containsKey(p.javaType) ?
                                JavaPkgInfo.primitiveSignature.get(p.javaType) : p.javaType;
                        p.name = txt(parameter.get("name"));
                        p.required = txt(parameter.get("required")).equalsIgnoreCase("true");
                        p.type = Operation.ParamType.build(txt(parameter.get("paramType")));
                        op.params.add(p);
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

            api.actions.add(action);
        }

        myAPIs.add(api);

        return api;

    }

    public void saveToDisk(String pkgName, String className, String classText) throws IOException {
        String fName = outputFolder
                + pkgName.replace(".", "/")
                + "/"
                + className + ".java";

        File f = new File(fName);
        f.getParentFile().mkdirs();
//        System.out.println("Saving: " + f.getAbsolutePath());
        FileWriter outFile = new FileWriter(f);
        PrintWriter out = new PrintWriter(outFile);
        out.println(classText);
        out.close();
    }

    public void saveToDisk(Model model) throws IOException {
        saveToDisk(model.getModelPackage(), model.getImplName(), model.toString());
    }

    public void saveToDisk(Apis api) throws IOException {
        saveToDisk(api.getActionsPackage(), api.getImplName(), api.toString());
    }

    public void saveToDisk(Operation operation) throws IOException {
        saveToDisk(operation.getPackage(), operation.getImplName(), operation.toString());
    }

    public void saveToDisk(JavaInterface ji) throws IOException {
        saveToDisk(ji.pkgName, ji.className, ji.toString());
    }

    public void saveToDisk(ClassTranslator ct) throws IOException {
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
                    f.delete();
                }
            }
        }
        folder.delete();
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

    public String remapAbstractType(String jsonType) {
        return innerRemapType(jsonType, false, "");
    }

    public String remapConcreteType(String jsonType, String apiVersion) {
        return innerRemapType(jsonType, true, apiVersion);
    }

    public String innerRemapType(String jsonType, boolean concrete, String apiVersion) {

        String listAry = "List[";

        if (jsonType.startsWith(listAry)) {
            return (concrete ? "List<" : "List<") + innerRemapType(jsonType.substring(listAry.length(), jsonType.length() - 1), concrete, apiVersion) + ">";
        } else if (JavaPkgInfo.TypeMap.containsKey(jsonType.toLowerCase())) {
            return JavaPkgInfo.TypeMap.get(jsonType.toLowerCase());
        } else {
            return jsonType + (concrete ? "_impl_" + apiVersion : "");
        }
    }

    private void writeAriBuilder(String apiVersion, AriBuilderInterface abi, Collection<Apis> apis, Collection<Model> models) throws IOException {

        String thisClass = "AriBuilder_impl_" + apiVersion;
        List<String> ifToImplement = new ArrayList<String>(abi.knownInterfaces);

        StringBuilder sb = new StringBuilder();
        JavaGen.importClasses(sb, "ch.loway.oss.ari4java.generated." + apiVersion,
                Arrays.asList(new String[]{
                        "ch.loway.oss.ari4java.ARI",
                        "ch.loway.oss.ari4java.generated.AriBuilder",
                        "ch.loway.oss.ari4java.generated.actions.*",
                        "ch.loway.oss.ari4java.generated." + apiVersion + ".actions.*",
                }));

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

        sb.append("public ARI.ClassFactory getClassFactory() {\n"
                + " return new ClassTranslator_impl_" + apiVersion + "();\n"
                + "};\n\n"
        );

        sb.append("};");

        saveToDisk("ch.loway.oss.ari4java.generated." + apiVersion, thisClass, sb.toString());

    }

    /**
     * @param outputFolder
     */
    void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

}
