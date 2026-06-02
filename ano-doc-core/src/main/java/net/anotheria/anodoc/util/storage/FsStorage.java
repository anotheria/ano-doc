package net.anotheria.anodoc.util.storage;

import net.anotheria.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

/**
 * File system storage.
 *
 * @author asamoilich.
 */
public class FsStorage implements IStorage {
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FsStorage.class);

    @Override
    public void save(Hashtable<String, Hashtable> toSave, String filePath) {
        ObjectOutputStream oOut = null;
        try {
            oOut = new ObjectOutputStream(new FileOutputStream(filePath));
            oOut.writeObject(toSave);
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("save", e);
        } finally {
            IOUtils.closeIgnoringException(oOut);
        }
    }

    @Override
    public Hashtable load(String filePath) throws Exception {
        ObjectInputStream oIn = null;
        try {
            oIn = new ObjectInputStream(new FileInputStream(filePath));
            return (Hashtable) oIn.readObject();
        } finally {
            IOUtils.closeIgnoringException(oIn);
        }
    }
}
