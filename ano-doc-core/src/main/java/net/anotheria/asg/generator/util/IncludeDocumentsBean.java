package net.anotheria.asg.generator.util;

/**
 * Bean of included document.
 *
 * @author another
 * @version $Id: $Id
 */
public class IncludeDocumentsBean {
    /**
     * Document name.
     */
    private String documentName;
    /**
     * Line, where document was included.
     */
    private int insertLine;

    /**
     * <p>Constructor for IncludeDocumentsBean.</p>
     *
     * @param documentName a {@link java.lang.String} object.
     * @param insertLine a int.
     */
    public IncludeDocumentsBean(String documentName, int insertLine) {
        this.documentName = documentName;
        this.insertLine = insertLine;
    }

    /**
     * <p>Getter for the field <code>documentName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * <p>Setter for the field <code>documentName</code>.</p>
     *
     * @param documentName a {@link java.lang.String} object.
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * <p>Getter for the field <code>insertLine</code>.</p>
     *
     * @return a int.
     */
    public int getInsertLine() {
        return insertLine;
    }

    /**
     * <p>Setter for the field <code>insertLine</code>.</p>
     *
     * @param insertLine a int.
     */
    public void setInsertLine(int insertLine) {
        this.insertLine = insertLine;
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "IncludeDocumentsBean{" +
                "documentName='" + documentName + '\'' +
                ", insertLine=" + insertLine +
                '}';
    }
}
