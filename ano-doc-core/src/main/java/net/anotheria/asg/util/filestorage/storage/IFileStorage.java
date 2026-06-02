package net.anotheria.asg.util.filestorage.storage;

import net.anotheria.asg.util.filestorage.TemporaryFileHolder;

/**
 * Storage interface.
 *
 * @author asamoilich.
 */
public interface IFileStorage {
    /**
     * Store file.
     *
     * @param fileContent file content
     * @param fileName    file name
     * @throws Exception
     */
    void storeFile(byte[] fileContent, String fileName) throws Exception;

    /**
     * Return {@code true} if file exists, {@code false} otherwise.
     *
     * @param fileName file name
     * @return boolean flag
     */
    boolean isFileExists(String fileName);

    /**
     * Clone file.
     *
     * @param sourceFileName      source file name
     * @param destinationFileName destination file name
     * @throws Exception if any errors occurs
     */
    void cloneFile(String sourceFileName, String destinationFileName) throws Exception;

    /**
     * Remove file.
     *
     * @param fileName file name
     * @throws Exception if any errors occurs
     */
    void removeFile(String fileName) throws Exception;

    /**
     * Load file.
     *
     * @param fileName       file name
     * @return {@link TemporaryFileHolder}
     * @throws Exception if any errors occurs
     */
    TemporaryFileHolder loadFile(String fileName) throws Exception;
}
