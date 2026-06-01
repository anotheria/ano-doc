package net.anotheria.anodoc.util.storage;

/**
 * Storage factory.
 *
 * @author asamoilich.
 */
public class StorageFactory {
    public static IStorage createStorage(StorageType storageType, String bucketName, String credentials, String projectId, String accessKey, String secretKey) {
        switch (storageType) {
            case GCS:
                return new GoogleCloudStorage(bucketName, credentials, projectId);
            case S3:
                return new S3CloudStorage(bucketName, accessKey, secretKey, projectId);
            case FS:
            default:
                return new FsStorage();
        }
    }
}
