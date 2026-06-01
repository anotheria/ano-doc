package net.anotheria.asg.util.filestorage;

import net.anotheria.asg.util.filestorage.storage.FileStorageFactory;
import net.anotheria.asg.util.filestorage.storage.IFileStorage;
import net.anotheria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class FileStorage {
    /**
     * FileStorage 'log'.
     */
    static Logger log = LoggerFactory.getLogger(FileStorage.class);

    /**
     * {@link IFileStorage} instance.
     */
    private static final IFileStorage storage;

    /**
     * Static initialization block.
     */
    static {
        FileStorageConfig config = FileStorageConfig.getInstance();
        storage = FileStorageFactory.createStorage(config.getDirectory(), config.getStorageType(), config.getBucketName(), config.getCredentialsPath(),
                config.getProjectId(), config.getAccessKey(), config.getSecretKey());
    }

    /**
     * Stores a file temporarily in the session.
     */
    public static void storeTemporaryFile(HttpServletRequest req, TemporaryFileHolder fileHolder) {
        req.getSession().setAttribute(IFilesConstants.BEAN_TMP_FILE, fileHolder);
    }

    /**
     * Stores a file temporarily in the session under the specified key.
     * Use of this method allows to store multiple temporary files in session.
     *
     * @param key attribute suffix to use for storing in session
     */
    public static void storeTemporaryFile(HttpServletRequest req, TemporaryFileHolder fileHolder, String key) {
        req.getSession().setAttribute(IFilesConstants.BEAN_TMP_FILE + "." + key, fileHolder);
    }

    /**
     * Returns the temporary file from the session.
     *
     * @param req HttpServletRequest to obtain the session from.
     * @return previously stored tmp file (if any).
     */
    public static TemporaryFileHolder getTemporaryFile(HttpServletRequest req) {
        return (TemporaryFileHolder) req.getSession().getAttribute(IFilesConstants.BEAN_TMP_FILE);
    }

    /**
     * Returns the temporary file from the session.
     *
     * @param req HttpServletRequest to obtain the session from.
     * @param key attribute suffix that was used to store attribute in session
     * @return previously stored tmp file (if any).
     */
    public static TemporaryFileHolder getTemporaryFile(HttpServletRequest req, String key) {
        return (TemporaryFileHolder) req.getSession().getAttribute(IFilesConstants.BEAN_TMP_FILE + "." + key);
    }

    /**
     * Removes the temporary saved file from the session.
     *
     * @param req servlet request.
     */
    public static void removeTemporaryFile(HttpServletRequest req) {
        req.getSession().removeAttribute(IFilesConstants.BEAN_TMP_FILE);
    }

    /**
     * Removes the temporary saved file from the session.
     *
     * @param req servlet request.
     * @param key attribute suffix
     */
    public static void removeTemporaryFile(HttpServletRequest req, String key) {
        req.getSession().removeAttribute(IFilesConstants.BEAN_TMP_FILE + "." + key);
    }

    public static void storeFilePermanently(HttpServletRequest req, String name) {
        storeFilePermanently(req, name, null);
    }

    public static void storeFilePermanently(HttpServletRequest req, String name, String key) {
        try {
            TemporaryFileHolder fileHolder = (key == null) ? getTemporaryFile(req) : getTemporaryFile(req, key);
            log.debug("trying to store(): " + name);
            storage.storeFile(fileHolder.getData(), name);
        } catch (Exception e) {
            log.error("storeFilePermanently", e);
            throw new RuntimeException("FileStorageFailed: " + e.getMessage());
        }
    }

    public static void storeTemporaryFilePermanently(TemporaryFileHolder temporaryFileHolder) {
        try {
            log.debug("trying to TemporaryFileHolder store(): {}", temporaryFileHolder.getFileName());
            storage.storeFile(temporaryFileHolder.getData(), temporaryFileHolder.getFileName());
        } catch (Exception e) {
            log.error("storeTemporaryFilePermanently", e);
            throw new RuntimeException("FileStorageFailed: " + e.getMessage());
        }
    }

    /**
     * Makes copy of source file on file system, with new generated file name but same extension.
     *
     * @param fileName source file to clone
     * @return new file name
     */
    public static String cloneFilePermanently(String fileName) {
        if (StringUtils.isEmpty(fileName) || storage.isFileExists(fileName))
            return "";

        try {
            String generateFileName = generateFileName(fileName);
            storage.cloneFile(fileName, generateFileName);
            return generateFileName;
        } catch (Exception e) {
            log.error("cloneFilePermanently", e);
            throw new RuntimeException("FileStorageFailed: " + e.getMessage());
        }
    }

    /**
     * Removes file from file system.
     *
     * @param name file name to remove
     */
    public static void removeFilePermanently(String name) {
        if (StringUtils.isEmpty(name))
            return;
        try {
            log.debug("trying to remove: " + name);
            storage.removeFile(name);
        } catch (Exception e) {
            log.error("removeFilePermanently()", e);
            throw new RuntimeException("FileStorageFailed: " + e.getMessage());
        }
    }

    public static void loadTemporaryFile(HttpServletRequest req, String name) {
        storeTemporaryFile(req, loadFile(name));
    }

    public static TemporaryFileHolder loadFile(String name) {
        if (StringUtils.isEmpty(name))
            return null;

        try {
            return storage.loadFile(name);
        } catch (Exception e) {
            log.warn("loadFile()", e);
        }
        return null;
    }

    /**
     * Generates file name and appends it with given file extension.
     *
     * @param fileName original file name
     * @return generated file name
     */
    public static String generateFileName(String fileName) {
        int extIndex = fileName.lastIndexOf(".");

        String filePrefix = fileName.substring(0, extIndex);
        String ext = fileName.substring(extIndex);

        // substitute special characters with underscore
        filePrefix = StringUtils.normalize(filePrefix);

        fileName = filePrefix + ext;

        // if file with such name already exist - append index
        for (int i = 1; storage.isFileExists(fileName); i++)
            fileName = filePrefix + i + ext;

        return fileName;
    }
}

