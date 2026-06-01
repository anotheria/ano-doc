package net.anotheria.asg.generator.validation;

import net.anotheria.asg.generator.util.IncludeDocumentsBean;
import net.anotheria.asg.generator.util.IncludedDocuments;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>XMLAgainstXSDErrorHandler class.</p>
 *
 * @author vzarva
 * @version $Id: $Id
 */
public class XMLAgainstXSDErrorHandler extends DefaultHandler{

    private boolean hasErrors = false;

    private IncludedDocuments includedDocuments = null;

    /**
     * <p>Constructor for XMLAgainstXSDErrorHandler.</p>
     *
     * @param includedDocuments a {@link net.anotheria.asg.generator.util.IncludedDocuments} object.
     */
    public XMLAgainstXSDErrorHandler(IncludedDocuments includedDocuments) {
        this.includedDocuments = includedDocuments;
    }

    /** {@inheritDoc} */
    public void error (SAXParseException e) {
        setHasErrors(true);
        System.out.println(getErrorMessage("error",e,includedDocuments));
    }

    /** {@inheritDoc} */
    public void warning (SAXParseException e) {
        System.out.println(getErrorMessage("warning",e,includedDocuments));
    }

    /** {@inheritDoc} */
    public void fatalError (SAXParseException e) {
        System.out.println(getErrorMessage("fatal",e,includedDocuments));
        e.printStackTrace();
        System.exit(1);
    }

    /**
     * <p>getErrorMessage.</p>
     *
     * @param errorType a {@link java.lang.String} object.
     * @param e a {@link org.xml.sax.SAXParseException} object.
     * @param includedDocuments a {@link net.anotheria.asg.generator.util.IncludedDocuments} object.
     * @return a {@link java.lang.String} object.
     */
    public String getErrorMessage(String errorType,SAXParseException e, IncludedDocuments includedDocuments){
        String message = "Validating "+errorType+" : "+e.getMessage() + " in line : "+e.getLineNumber();
        if (includedDocuments == null){
            return message;
        }
        IncludeDocumentsBean idb = includedDocuments.getIncludeDocumentByLine(e.getLineNumber());
        if (idb == null){
            return message;
        }

        int lineWithError= e.getLineNumber() - idb.getInsertLine();

        message = "Validating "+errorType+" : "+e.getMessage() +" in document : "+ idb.getDocumentName()+" in line : "+lineWithError;
        return message;
    }

    /**
     * <p>isHasErrors.</p>
     *
     * @return a boolean.
     */
    public boolean isHasErrors() {
        return hasErrors;
    }

    /**
     * <p>Setter for the field <code>hasErrors</code>.</p>
     *
     * @param hasErrors a boolean.
     */
    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }
}
