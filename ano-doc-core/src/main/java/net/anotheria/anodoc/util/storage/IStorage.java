package net.anotheria.anodoc.util.storage;

import java.util.Hashtable;

/**
 * Storage interface.
 *
 * @author asamoilich.
 */
public interface IStorage {

    /**
     * Save hashtable.
     *
     * @param toSave   {@link Hashtable}
     * @param filePath file path
     */
    void save(Hashtable<String, Hashtable> toSave, String filePath);

    /**
     * Load hashtable.
     *
     * @param filePath file path
     * @return {@link Hashtable}
     * @throws Exception
     */
    Hashtable load(String filePath) throws Exception;
}
