package net.anotheria.asg.util.filestorage.storage;

/**
 * Storage factory.
 *
 * @author asamoilich.
 */
public class FileStorageFactory {
    public static IFileStorage createStorage(String fileStorageDir, String storageType, String bucketName, String credentials, String projectId, String accessKey, String secretKey) {
        switch (FileStorageType.getByTypeValue(storageType)) {
            case GCS:
                return new GoogleCloudStorage(bucketName, credentials, projectId);
            case S3:
                return new S3CloudStorage(bucketName, accessKey, secretKey, projectId);
            case FS:
            default:
                return new FsStorage(fileStorageDir);
        }
    }
}
