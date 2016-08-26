package net.anotheria.asg.generator.util;


import java.util.ArrayList;

/**
 * <p>IncludedDocuments class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class IncludedDocuments {
    /**
     * list of included documents
     */
    private static ArrayList<IncludeDocumentsBean> list = new ArrayList<IncludeDocumentsBean>();

    /**
     * <p>Constructor for IncludedDocuments.</p>
     */
    public IncludedDocuments() {
    }

    /**
     * get included document by line number.
     *
     * @param line a int.
     * @return a {@link net.anotheria.asg.generator.util.IncludeDocumentsBean} object.
     */
    public IncludeDocumentsBean getIncludeDocumentByLine(int line) {
        if (line < 0) {
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        if (list.get(0).getInsertLine() >= line) {
            return list.get(0);
        }

        for(int i = 0; i < list.size(); i++) {

            if (list.get(i).getInsertLine() <= line) {
                if ((i + 1) == list.size()) {
                    return list.get(i);
                }

                if ((i + 1) < list.size()) {
                    if (list.get(i + 1).getInsertLine() > line) {
                        return list.get(i);
                    }
                }
            }
        }

        return null;
    }

    /**
     * add include document to list.
     *
     * @param documentName a {@link java.lang.String} object.
     * @param insertLine a int.
     */
    public void setNewIncludedDocument(String documentName, int insertLine) {
        list.add(new IncludeDocumentsBean(documentName, insertLine));
    }

    /**
     * clear list of documents.
     */
    public void clearListOfIncludedDocuments() {
        list.clear();
    }

    /**
     * <p>isListEmpty.</p>
     *
     * @return a boolean.
     */
    public boolean isListEmpty() {
        return list.isEmpty();
    }

    /**
     * <p>getLastInsertLine.</p>
     *
     * @return a int.
     */
    public int getLastInsertLine() {
        if (!list.isEmpty()) {
            return list.get(list.size() - 1).getInsertLine();
        }
        return 0;
    }
}
