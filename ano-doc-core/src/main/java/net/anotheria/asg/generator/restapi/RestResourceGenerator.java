package net.anotheria.asg.generator.restapi;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaContainerProperty;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.model.DataFacadeGenerator;
import net.anotheria.asg.generator.model.ServiceGenerator;
import net.anotheria.asg.util.rest.ReplyObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates one JAX-RS resource class per module, containing CRUD methods for every document in that module.
 * Methods return {@link ReplyObject} directly; JAX-RS serializes it to JSON via the configured provider.
 */
public class RestResourceGenerator extends AbstractGenerator implements IGenerator {

    @Override
    public List<FileEntry> generate(IGenerateable gmodule) {
        MetaModule module = (MetaModule) gmodule;
        List<FileEntry> ret = new ArrayList<>();
        ret.add(new FileEntry(generateResource(module)));
        return ret;
    }

    private GeneratedClass generateResource(MetaModule module) {
        Context context = GeneratorDataRegistry.getInstance().getContext();
        String resourceName = getResourceName(module);
        String serviceInterface = ServiceGenerator.getInterfaceName(module);
        String serviceException = ServiceGenerator.getExceptionName(module);

        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);

        clazz.setPackageName(RestVOGenerator.getPackageName(module));
        clazz.setName(resourceName);

        clazz.addImport("java.util.List");
        clazz.addImport("java.util.ArrayList");
        clazz.addImport("jakarta.ws.rs.Consumes");
        clazz.addImport("jakarta.ws.rs.DELETE");
        clazz.addImport("jakarta.ws.rs.GET");
        clazz.addImport("jakarta.ws.rs.POST");
        clazz.addImport("jakarta.ws.rs.PUT");
        clazz.addImport("jakarta.ws.rs.Path");
        clazz.addImport("jakarta.ws.rs.PathParam");
        clazz.addImport("jakarta.ws.rs.Produces");
        clazz.addImport("jakarta.ws.rs.core.MediaType");
        clazz.addImport("org.slf4j.Logger");
        clazz.addImport("org.slf4j.LoggerFactory");
        clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactory");
        clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactoryException");
        clazz.addImport("io.swagger.v3.oas.annotations.Operation");
        clazz.addImport("io.swagger.v3.oas.annotations.tags.Tag");
        clazz.addImport(ReplyObject.class);
        clazz.addImport(ServiceGenerator.getInterfaceImport(module));
        clazz.addImport(ServiceGenerator.getExceptionImport(module));
        clazz.addImport("net.anotheria.moskito.aop.annotation.Monitor");
        clazz.addImport("net.anotheria.anodoc.data.NoSuchDocumentException");

        for (MetaDocument doc : module.getDocuments()) {
            clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));
            clazz.addImport(DataFacadeGenerator.getDocumentFactoryImport(context, doc));
            clazz.addImport(RestVOGenerator.getVOImport(doc));
        }

        clazz.addAnnotation("@Path(\"/" + module.getName().toLowerCase() + "\")");
        clazz.addAnnotation("@Tag(name = \"CMS " + module.getName() + " API\", description = \"CRUD operations for the " + module.getName() + " module\")");
        clazz.addAnnotation("@Monitor()");

        startClassBody();
        appendGenerationPoint("generateResource");

        appendStatement("private static final Logger LOG = LoggerFactory.getLogger(", resourceName, ".class)");
        appendStatement("private ", serviceInterface, " service");
        emptyline();

        openFun("public " + resourceName + "()");
        openTry();
        appendStatement("service = MetaFactory.get(", serviceInterface, ".class)");
        appendCatch("MetaFactoryException");
        appendStatement("LOG.error(\"Failed to initialize service " + serviceInterface + "\", e)");
        appendStatement("throw new RuntimeException(\"Failed to initialize service " + serviceInterface + "\", e)");
        closeBlockNEW();
        closeBlockNEW();

        for (MetaDocument doc : module.getDocuments()) {
            emptyline();
            generateDocumentCRUD(doc, serviceException, context);
        }

        return clazz;
    }

    private void generateDocumentCRUD(MetaDocument doc, String serviceException, Context context) {
        String docName = doc.getName();
        String multipleDocName = doc.getMultiple();
        String docPath = "/" + docName.toLowerCase();
        String docFactoryName = DataFacadeGenerator.getDocumentFactoryName(doc);
        String voName = RestVOGenerator.getVOName(doc);
        String listKey = quote(multipleDocName.toLowerCase());
        String itemKey = quote(doc.getVariableName());

        // GET list
        appendString("@GET");
        appendString("@Path(\"" + docPath + "\")");
        appendString("@Produces(MediaType.APPLICATION_JSON)");
        appendString("@Operation(summary = \"List all " + multipleDocName + "\")");
        openFun("public ReplyObject list" + multipleDocName + "()");
        openTry();
        appendStatement("List<", docName, "> docs = service.get", multipleDocName, "()");
        appendStatement("List<", voName, "> result = new ArrayList<>()");
        appendStatement("for (", docName, " doc : docs) result.add(", voName, ".from(doc))");
        appendStatement("return ReplyObject.success(", listKey, ", result)");
        appendCatch("Exception");
        appendStatement("LOG.error(\"Failed to list " + multipleDocName + "\", e)");
        appendStatement("return ReplyObject.error(e)");
        closeBlockNEW();
        closeBlockNEW();
        emptyline();

        // GET by id
        appendString("@GET");
        appendString("@Path(\"" + docPath + "/{id}\")");
        appendString("@Produces(MediaType.APPLICATION_JSON)");
        appendString("@Operation(summary = \"Get " + docName + " by id\")");
        openFun("public ReplyObject get" + docName + "(@PathParam(\"id\") String id)");
        openTry();
        appendStatement(docName, " doc = service.get", docName, "(id)");
        appendStatement("return ReplyObject.success(", itemKey, ", ", voName, ".from(doc))");
        appendCatch("Exception");
        appendStatement("LOG.error(\"Failed to get " + docName + " by id: \" + id, e)");
        appendStatement("return ReplyObject.error(e)");
        closeBlockNEW();
        closeBlockNEW();
        emptyline();

        // POST create
        appendString("@POST");
        appendString("@Path(\"" + docPath + "\")");
        appendString("@Consumes(MediaType.APPLICATION_JSON)");
        appendString("@Produces(MediaType.APPLICATION_JSON)");
        appendString("@Operation(summary = \"Create a new " + docName + "\")");
        openFun("public ReplyObject create" + docName + "(" + voName + " vo)");
        openTry();
        appendStatement(docName, " doc = ", docFactoryName, ".create", docName, "()");
        appendCopyVOToDoc(doc, "vo", "doc", context);
        appendStatement(docName, " created = service.create", docName, "(doc)");
        appendStatement("return ReplyObject.success(", itemKey, ", ", voName, ".from(created))");
        appendCatch("Exception");
        appendStatement("LOG.error(\"Failed to create " + docName + "\", e)");
        appendStatement("return ReplyObject.error(e)");
        closeBlockNEW();
        closeBlockNEW();
        emptyline();

        // PUT upsert
        appendString("@PUT");
        appendString("@Path(\"" + docPath + "/{id}\")");
        appendString("@Consumes(MediaType.APPLICATION_JSON)");
        appendString("@Produces(MediaType.APPLICATION_JSON)");
        appendString("@Operation(summary = \"Create or replace the " + docName + " with the given id\")");
        openFun("public ReplyObject update" + docName + "(@PathParam(\"id\") String id, " + voName + " vo)");
        openTry();
        appendStatement(docName, " doc");
        appendStatement("boolean exists = true");
        openTry();
        appendStatement("doc = service.get", docName, "(id)");
        appendCatch("NoSuchDocumentException");
        //a put names the document it writes, so it can create it under that very id. That is what keeps ids
        //stable across instances, and with them every link pointing at this document.
        appendStatement("doc = ", docFactoryName, ".create", docName, "ForImport(id)");
        appendStatement("exists = false");
        closeBlockNEW();
        appendCopyVOToDoc(doc, "vo", "doc", context);
        appendStatement(docName, " saved = exists ? service.update", docName, "(doc) : service.import", docName, "(doc)");
        appendStatement("return ReplyObject.success(", itemKey, ", ", voName, ".from(saved))");
        appendCatch("Exception");
        appendStatement("LOG.error(\"Failed to write " + docName + " with id: \" + id, e)");
        appendStatement("return ReplyObject.error(e)");
        closeBlockNEW();
        closeBlockNEW();
        emptyline();

        // DELETE
        appendString("@DELETE");
        appendString("@Path(\"" + docPath + "/{id}\")");
        appendString("@Produces(MediaType.APPLICATION_JSON)");
        appendString("@Operation(summary = \"Delete " + docName + " by id\")");
        openFun("public ReplyObject delete" + docName + "(@PathParam(\"id\") String id)");
        openTry();
        appendStatement("service.delete", docName, "(id)");
        appendStatement("return ReplyObject.success()");
        appendCatch("Exception");
        appendStatement("LOG.error(\"Failed to delete " + docName + " with id: \" + id, e)");
        appendStatement("return ReplyObject.error(e)");
        closeBlockNEW();
        closeBlockNEW();
    }

    /**
     * Copies every field the VO carries onto the document.
     *
     * <p>Collections are only copied when the payload actually has them. A caller that leaves a list out of
     * the json means "I am not talking about this list", and handing the document a null instead of a list is
     * how you get a NullPointerException three layers down, on read, long after the request is gone.
     */
    private void appendCopyVOToDoc(MetaDocument doc, String voVar, String docVar, Context context) {
        for (MetaProperty p : RestVOGenerator.voProperties(doc))
            appendCopyProperty(p, voVar, docVar, context);

        for (MetaProperty link : doc.getLinks())
            appendCopyProperty(link, voVar, docVar, context);
    }

    private void appendCopyProperty(MetaProperty p, String voVar, String docVar, Context context) {
        if (p.isReadonly())
            return;

        if (p.isMultilingual() && context.areLanguagesSupported()) {
            for (String lang : context.getLanguages())
                appendCopyAccesser(p, p.getAccesserName(lang), voVar, docVar);
            return;
        }

        appendCopyAccesser(p, p.getAccesserName(), voVar, docVar);
    }

    private void appendCopyAccesser(MetaProperty p, String accesserName, String voVar, String docVar) {
        boolean collection = p instanceof MetaContainerProperty || p.getType() == MetaProperty.Type.LIST;
        if (collection) {
            appendString("if (", voVar, ".get", accesserName, "() != null)");
            appendIncreasedStatement(docVar + ".set" + accesserName + "(" + voVar + ".get" + accesserName + "())");
            return;
        }

        appendStatement(docVar, ".set", accesserName, "(", voVar, ".get", accesserName, "())");
    }

    public static String getResourceName(MetaModule module) {
        return module.getName() + "Resource";
    }
}
