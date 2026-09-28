package net.anotheria.asg.generator.apputil;

import net.anotheria.asg.generator.*;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.util.filestorage.FileStorage;
import net.anotheria.asg.util.filestorage.TemporaryFileHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates the shared utilities that are not tied to a single module - currently the resource that takes file
 * uploads into the cms file storage.
 *
 * @author ykalapusha
 */
public class BasicServiceUtilGenerator extends AbstractGenerator {

    public List<FileEntry> generate(List<MetaModule> modules) {
        List<FileEntry> entries = new ArrayList<>();
        entries.add(new FileEntry(generateRestResourceForImages()));
        return entries;
    }

    private GeneratedArtefact generateRestResourceForImages() {

        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);
        appendGenerationPoint("generateUploadImageResource");

        clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".rest");

        clazz.addImport("net.anotheria.util.IOUtils");
        clazz.addImport(FileStorage.class);
        clazz.addImport(TemporaryFileHolder.class);
        clazz.addImport("org.glassfish.jersey.media.multipart.FormDataContentDisposition");
        clazz.addImport("org.glassfish.jersey.media.multipart.FormDataParam");
        clazz.addImport("jakarta.ws.rs.Consumes");
        clazz.addImport("jakarta.ws.rs.Produces");
        clazz.addImport("jakarta.ws.rs.POST");
        clazz.addImport("jakarta.ws.rs.Path");
        clazz.addImport("jakarta.ws.rs.core.Context");
        clazz.addImport("jakarta.ws.rs.core.MediaType");
        clazz.addImport("jakarta.ws.rs.core.Response");
        clazz.addImport("jakarta.ws.rs.core.UriInfo");
        clazz.addImport("java.io.IOException");
        clazz.addImport("java.io.InputStream");
        clazz.addImport("org.slf4j.Logger");
        clazz.addImport("org.slf4j.LoggerFactory");
        clazz.addImport("net.anotheria.util.log.LogMessageUtil");

        clazz.addAnnotation("@Path(\"/asgimage\")");
        clazz.setName("UploadImageResource");
        startClassBody();

        appendStatement("private static final Logger LOGGER = LoggerFactory.getLogger(UploadImageResource.class)");
        emptyline();
        emptyline();
        appendString("@Context");
        appendStatement("private UriInfo context");
        emptyline();
        appendString("public UploadImageResource(){}");
        emptyline();
        appendString("@POST");
        appendString("@Path(\"/upload\")");
        appendString("@Consumes(MediaType.MULTIPART_FORM_DATA)");
        appendString("@Produces(MediaType.APPLICATION_JSON)");
        openFun("public Response uploadFile( @FormDataParam(\"file\") InputStream uploadedInputStream, @FormDataParam(\"file\") FormDataContentDisposition fileDetail)");
        appendString("if (uploadedInputStream == null || fileDetail == null)");
        increaseIdent();
        appendStatement("return Response.status(400).entity(\"Invalid form data\").build()");
        decreaseIdent();
        emptyline();
        openTry();
        appendStatement("TemporaryFileHolder temporaryFileHolder = new TemporaryFileHolder()");
        appendStatement("temporaryFileHolder.setFileName(fileDetail.getFileName())");
        appendStatement("temporaryFileHolder.setMimeType(fileDetail.getType())");
        appendStatement("temporaryFileHolder.setData(uploadedInputStream.readAllBytes())");
        appendStatement("FileStorage.storeTemporaryFilePermanently(temporaryFileHolder)");
        appendCatch("IOException");
        appendStatement("String failMsg = LogMessageUtil.failMsg(e)");
        appendStatement("LOGGER.error(failMsg)");
        appendStatement("return Response.status(500).entity(failMsg).build()");
        decreaseIdent();
        appendString("} finally {");
        increaseIdent();
        appendStatement("IOUtils.closeIgnoringException(uploadedInputStream)");
        closeBlockNEW();
        appendStatement("return Response.status(201).build()");
        closeBlockNEW();
        return clazz;
    }
}