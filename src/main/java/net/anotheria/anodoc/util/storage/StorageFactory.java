package net.anotheria.anodoc.util.storage;

/**
 * Storage factory.
 *
 * @author asamoilich.
 */
public class StorageFactory {
    public static IStorage createStorage(String storageType, String bucketName, String credentials, String projectId, String accessKey, String secretKey) {
        switch (StorageType.getByTypeValue(storageType)) {
            case GCS:
                return new GoogleCloudStorage(bucketName, credentials, projectId);
            case LCS:
                return new LinodeCloudStorage(bucketName, accessKey, secretKey, projectId);
            case FS:
            default:
                return new FsStorage();
        }
    }
}
