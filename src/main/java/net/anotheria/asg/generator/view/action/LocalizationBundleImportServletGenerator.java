package net.anotheria.asg.generator.view.action;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generator class for the index page action in cms.
 *
 * @author abolbat
 * @version $Id: $Id
 */
public class LocalizationBundleImportServletGenerator extends AbstractGenerator {


    /**
     * <p>generate.</p>
     *
     * @return a {@link List} object.
     */
    public List<FileEntry> generate() {
        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);
        appendGenerationPoint("generateBaseAction");
        clazz.setPackageName(getLocalizationBundleImportPagePackageName());

        clazz.addImport("net.anotheria.anodoc.data.StringProperty");
        clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactory");
        clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactoryException");
        clazz.addImport("net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundle");
        clazz.addImport("net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundleDocument");
        clazz.addImport("net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceException");
        clazz.addImport("net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService");
        clazz.addImport("net.anotheria.maf.json.JSONResponse");
        clazz.addImport("org.apache.commons.io.IOUtils");
        clazz.addImport("org.slf4j.Logger");
        clazz.addImport("org.slf4j.LoggerFactory");

        clazz.addImport("javax.servlet.annotation.MultipartConfig");
        clazz.addImport("javax.servlet.annotation.WebServlet");
        clazz.addImport("javax.servlet.http.HttpServlet");
        clazz.addImport("javax.servlet.http.HttpServletRequest");
        clazz.addImport("javax.servlet.http.HttpServletResponse");
        clazz.addImport("javax.servlet.http.Part");
        clazz.addImport("java.io.IOException");
        clazz.addImport("java.io.PrintWriter");
        clazz.addImport("java.nio.charset.StandardCharsets");
        clazz.addImport("java.util.ArrayList");
        clazz.addImport("java.util.HashMap");
        clazz.addImport("java.util.List");
        clazz.addImport("java.util.Map");

        clazz.setParent("HttpServlet");
        clazz.setAnnotations(Arrays.asList("@WebServlet({\"/ImportLocalizationBundle\"})", "@MultipartConfig"));
        clazz.setName(getLocalizationBundleImportServletName());

        startClassBody();
        appendString("private static final Logger LOGGER = LoggerFactory.getLogger(" + getLocalizationBundleImportServletName() + ".class);");
        appendString("private IASResourceDataService resourceDataService;");

        appendString("public LocalizationBundleImportServlet() {\n" +
                "        try {\n" +
                "            resourceDataService = MetaFactory.get(IASResourceDataService.class);\n" +
                "        } catch (MetaFactoryException e) {\n" +
                "        }\n" +
                "    }");

        appendString("public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {\n" +
                "        JSONResponse jsonResponse = new JSONResponse();\n" +
                "        try {\n" +
                "            Part file = request.getPart(\"file\");\n" +
                "            String locale = request.getParameter(\"locale\");\n" +
                "            String result = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);\n" +
                "            String[] values = result.split(\"\\n\");\n" +
                "\n" +
                "            Map<String, Map<String, String>> bundlesMap = new HashMap<>();\n" +
                "            String prevKey = \"\";\n" +
                "            boolean duplicateKey = false;\n" +
                "            for (String row : values) {\n" +
                "                try {\n" +
                "                    String bundleId = row.substring(0, row.indexOf(\".\"));\n" +
                "                    String keyValuePair = row.substring(row.indexOf(\".\") + 1);\n" +
                "                    String key = prevKey;\n" +
                "                    String value = keyValuePair;\n" +
                "                    if (keyValuePair.contains(\"=\") && keyValuePair.indexOf(\"=\") > 0) {\n" +
                "                        String slesh = keyValuePair.substring(keyValuePair.indexOf(\"=\") - 1, keyValuePair.indexOf(\"=\"));\n" +
                "                        if (!slesh.equals(\"\\\\\")) {\n" +
                "                            key = keyValuePair.substring(0, keyValuePair.indexOf(\"=\"));\n" +
                "                            duplicateKey = prevKey.equals(key);\n" +
                "                            prevKey = key;\n" +
                "                            value = keyValuePair.length() > key.length() + 1 ? keyValuePair.substring(keyValuePair.indexOf(\"=\") + 1) : \"\";\n" +
                "                        }\n" +
                "                    }\n" +
                "\n" +
                "                    if (!bundlesMap.containsKey(bundleId)) {\n" +
                "                        bundlesMap.put(bundleId, new HashMap<>());\n" +
                "                    }\n" +
                "\n" +
                "                    Map<String, String> keyValueMap = bundlesMap.get(bundleId);\n" +
                "                    if (keyValueMap.containsKey(key) && !duplicateKey) {\n" +
                "                        keyValueMap.put(key, keyValueMap.get(key) + \"\\n\" + value);\n" +
                "                    } else {\n" +
                "                        keyValueMap.put(key, value);\n" +
                "                    }\n" +
                "                    bundlesMap.put(bundleId, keyValueMap);\n" +
                "                } catch (Exception e) {\n" +
                "                    LOGGER.error(e.getMessage());\n" +
                "                    addErrorToJsonResponse(jsonResponse, RESPONSE.SERVER_ERROR);\n" +
                "                    writeResponse(response, jsonResponse.toJSON().toString());\n" +
                "                    return;\n" +
                "                }\n" +
                "            }\n" +
                "            StringBuilder oldLocalizationNotUpdated = new StringBuilder();\n" +
                "            List<LocalizationBundle> bundlesToUpdate = new ArrayList<>();\n" +
                "            for (Map.Entry<String, Map<String, String>> entry : bundlesMap.entrySet()) {\n" +
                "                Map<String, String> newKeyValuePairs = entry.getValue();\n" +
                "                LocalizationBundle localizationBundle;\n" +
                "                try {\n" +
                "                    localizationBundle = resourceDataService.getLocalizationBundle(entry.getKey());\n" +
                "                } catch (ASResourceDataServiceException e) {\n" +
                "                    LOGGER.error(e.getMessage());\n" +
                "                    addErrorToJsonResponse(jsonResponse, RESPONSE.SERVER_ERROR);\n" +
                "                    continue;\n" +
                "                }\n" +
                "                LocalizationBundleDocument bundleDocument = (LocalizationBundleDocument) localizationBundle;\n" +
                "                StringProperty stringProperty = bundleDocument.getStringProperty(\"messages_\" + locale);\n" +
                "                if (stringProperty == null || stringProperty.getString() == null) {\n" +
                "                    StringBuilder sb = new StringBuilder();\n" +
                "                    for (Map.Entry<String, String> keyValuePair : newKeyValuePairs.entrySet()) {\n" +
                "                        sb.append(keyValuePair.getKey()).append(\"=\").append(keyValuePair.getValue()).append(\"\\n\");\n" +
                "                    }\n" +
                "                    stringProperty = new StringProperty(\"messages_\" + locale, sb.toString());\n" +
                "                    bundleDocument.putStringProperty(stringProperty);\n" +
                "                    bundlesToUpdate.add(bundleDocument);\n" +
                "                    continue;\n" +
                "                }\n" +
                "                StringBuilder keyValuePairsToUpdate = new StringBuilder();\n" +
                "\n" +
                "                boolean alreadyUpdated = false;\n" +
                "                for (String row : stringProperty.getString().split(\"\\n\")) {\n" +
                "                    row = row.trim();\n" +
                "                    if (!row.isEmpty()) {\n" +
                "                        if (!row.contains(\"=\") || (row.indexOf(\"=\") > 0 && \"\\\\\".equals(row.substring(row.indexOf(\"=\") - 1, row.indexOf(\"=\"))))) {\n" +
                "                            if (!alreadyUpdated)\n" +
                "                                keyValuePairsToUpdate.append(row).append(\"\\n\");\n" +
                "                            continue;\n" +
                "                        }\n" +
                "\n" +
                "                        alreadyUpdated = false;\n" +
                "                        String key = row.substring(0, row.indexOf(\"=\"));\n" +
                "                        String oldValue = row.length() > key.length() + 1 ? row.substring(row.indexOf(\"=\") + 1) : \"\";\n" +
                "                        if (newKeyValuePairs.containsKey(key)) {\n" +
                "                            String newValue = newKeyValuePairs.remove(key);\n" +
                "                            keyValuePairsToUpdate.append(key).append(\"=\").append(newValue).append(\"\\n\");\n" +
                "                            alreadyUpdated = true;\n" +
                "                        } else {\n" +
                "                            keyValuePairsToUpdate.append(key).append(\"=\").append(oldValue).append(\"\\n\");\n" +
                "                            oldLocalizationNotUpdated.append(entry.getKey()).append(\".\").append(key).append(\"=\").append(oldValue).append(\"\\n\");\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "                if (!newKeyValuePairs.isEmpty()) {\n" +
                "                    for (Map.Entry<String, String> kv : newKeyValuePairs.entrySet()) {\n" +
                "                        keyValuePairsToUpdate.append(kv.getKey()).append(\"=\").append(kv.getValue()).append(\"\\n\");\n" +
                "                    }\n" +
                "                }\n" +
                "                stringProperty = new StringProperty(\"messages_\" + locale, keyValuePairsToUpdate.toString());\n" +
                "                bundleDocument.putStringProperty(stringProperty);\n" +
                "                bundlesToUpdate.add(bundleDocument);\n" +
                "            }\n" +
                "            try {\n" +
                "                resourceDataService.updateLocalizationBundles(bundlesToUpdate);\n" +
                "            } catch (ASResourceDataServiceException e) {\n" +
                "                LOGGER.error(e.getMessage());\n" +
                "                addErrorToJsonResponse(jsonResponse, RESPONSE.SERVER_ERROR);\n" +
                "            }\n" +
                "            LOGGER.info(\"-------------------------------------------------------\");\n" +
                "            LOGGER.info(\"--------  not imported/replaced localizations  --------\");\n" +
                "            LOGGER.info(oldLocalizationNotUpdated.toString());\n" +
                "        } catch (Exception e) {\n" +
                "            LOGGER.error(e.getMessage());\n" +
                "            addErrorToJsonResponse(jsonResponse, RESPONSE.SERVER_ERROR);\n" +
                "        }\n" +
                "        writeResponse(response, jsonResponse.toJSON().toString());\n" +
                "    }");


        appendString("private void writeResponse(HttpServletResponse response, String jsonString) throws IOException {\n" +
                "        PrintWriter out = response.getWriter();\n" +
                "        response.setContentType(\"application/json\");\n" +
                "        response.setCharacterEncoding(\"UTF-8\");\n" +
                "        out.print(jsonString);\n" +
                "        out.flush();\n" +
                "    }\n" +
                "\n" +
                "    private void addErrorToJsonResponse(JSONResponse jsonResponse, RESPONSE response) {\n" +
                "        jsonResponse.addError(response.name(), response.errorMessage);\n" +
                "    }\n" +
                "    private enum RESPONSE {\n" +
                "        SERVER_ERROR(\"Error on file uploading.\"),\n" +
                "        NOT_MULTIPART_DATA,\n" +
                "        VALIDATION_FAIL(\"Data is not valid.\");\n" +
                "\n" +
                "        private String errorMessage;\n" +
                "\n" +
                "        RESPONSE(String errorMessage) {\n" +
                "            this.errorMessage = errorMessage;\n" +
                "        }\n" +
                "\n" +
                "        RESPONSE() {\n" +
                "        }\n" +
                "        public String getErrorMessage() {\n" +
                "            return errorMessage;\n" +
                "        }\n" +
                "    }");
        emptyline();
        appendString("public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {\n" +
                "    }");

        List<FileEntry> files = new ArrayList<FileEntry>();
        files.add(new FileEntry(clazz));
        return files;
    }

    /**
     * <p>getIndexPagePackageName.</p>
     *
     * @return a {@link String} object.
     */
    public static String getLocalizationBundleImportPagePackageName() {
        return GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED) + ".action";
    }


    /**
     * <p>getLocalizationBundleImportServletName.</p>
     *
     * @return a {@link String} object.
     */
    public static String getLocalizationBundleImportServletName() {
        return "LocalizationBundleImportServlet";
    }
}
