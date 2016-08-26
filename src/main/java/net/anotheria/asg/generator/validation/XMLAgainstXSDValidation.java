package net.anotheria.asg.generator.validation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.anotheria.asg.generator.util.IncludedDocuments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * <p>XMLAgainstXSDValidation class.</p>
 *
 * @author vzarva
 * @version $Id: $Id
 */
public final class XMLAgainstXSDValidation {

	private static Logger log = LoggerFactory.getLogger(XMLAgainstXSDValidation.class);

    private static final String JAXP_SCHEMA_LANGUAGE =  "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /**
     * <p>validateAgainstXSDSchema.</p>
     *
     * @param nameOfFile a {@link java.lang.String} object.
     * @param content a {@link java.lang.String} object.
     * @param inputStream a {@link java.io.InputStream} object.
     * @param includedDocuments a {@link net.anotheria.asg.generator.util.IncludedDocuments} object.
     */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
    public static void validateAgainstXSDSchema(String nameOfFile,String content,InputStream inputStream,IncludedDocuments includedDocuments){
        File tempXSDFile = null;

		try {
        	log.debug("----------Validating "+nameOfFile+" started");
            //System.out.println(;

            // create file xsd from input stream to validate xml against it
            tempXSDFile = createTempFile(inputStream);

            DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE,W3C_XML_SCHEMA );
            factory.setAttribute(SCHEMA_SOURCE,tempXSDFile);

            DocumentBuilder builder = factory.newDocumentBuilder();
            // create and set error handler
            XMLAgainstXSDErrorHandler XMLAgainstXSDErrorHandler = new XMLAgainstXSDErrorHandler(includedDocuments);
            builder.setErrorHandler(XMLAgainstXSDErrorHandler);

            final InputStream contentOfFileAsInputStream = new ByteArrayInputStream(content.getBytes());

            if (contentOfFileAsInputStream != null) {
                builder.parse(new InputSource(contentOfFileAsInputStream));
            } else {
                log.error("-----File "+nameOfFile+" doesn't exist.");
            }


            if (includedDocuments != null){
                includedDocuments.clearListOfIncludedDocuments();
            }
            // the program will terminate if xml file has errors
            if (XMLAgainstXSDErrorHandler.isHasErrors()){
                tempXSDFile.delete();
				log.error("-----Validating "+nameOfFile+" finished with errors.");
                System.err.println("-----Validating "+nameOfFile+" finished with errors.");
                System.exit(-1);
            }

            log.debug("-----Validating "+nameOfFile+" finished successfully.");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("-----Error in parsing  ", e);
            throw new RuntimeException("Error in validation due to ",e);
        } finally {
            if (tempXSDFile != null) {
                tempXSDFile.delete();
            }
        }
    }

    private static File createTempFile(InputStream inputStream){
        try {
            File tempFile = File.createTempFile("temp-valid",".xsd");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile,true);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, read);
            }
            inputStream.close();
            fileOutputStream.close();

            return tempFile;
        } catch (IOException e) {
            log.error("-----Error: IOException" + e.getMessage());
            throw new RuntimeException("IOException.",e);
        }
    }
}
