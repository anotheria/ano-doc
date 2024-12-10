package net.anotheria.asg.util.filestorage.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageOptions;
import net.anotheria.asg.util.filestorage.TemporaryFileHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;

/**
 * Google cloud storage.
 *
 * @author asamoilich.
 */
public class GoogleCloudStorage implements IFileStorage {
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudStorage.class);
    private final String bucketName;
    /**
     * {@link Storage} instance.
     */
    private final Storage cloudStorage;

    public GoogleCloudStorage(String bucketName, String credentialsPath, String projectId) {
        this.bucketName = bucketName;
        try {
            URL url = getClass().getClassLoader().getResource(credentialsPath);
            cloudStorage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(Objects.requireNonNull(url).openStream()))
                    .setProjectId(projectId)
                    .build()
                    .getService();

            initializeBucket();
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize google storage. ", e);
        }
    }

    private void initializeBucket() {
        Bucket bucket = cloudStorage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.NAME));
        if (bucket == null) {
            //create bucket
            bucket = cloudStorage.create(BucketInfo.newBuilder(bucketName)
                    .setStorageClass(StorageClass.STANDARD)
                    .setLocation("EU")
                    .build());
            LOGGER.info("Bucket created: " + bucket.toString());
        }
    }

    @Override
    public void storeFile(byte[] fileContent, String fileName) throws Exception {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        cloudStorage.create(blobInfo, fileContent);
    }

    @Override
    public boolean isFileExists(String fileName) {
        return cloudStorage.get(bucketName, fileName) != null;
    }

    @Override
    public void cloneFile(String sourceFileName, String destinationFileName) throws Exception {
        byte[] data = cloudStorage.readAllBytes(bucketName, sourceFileName);
        BlobId blobId = BlobId.of(bucketName, destinationFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        cloudStorage.create(blobInfo, data);
    }

    @Override
    public void removeFile(String fileName) throws Exception {
        cloudStorage.delete(bucketName, fileName);
    }

    @Override
    public TemporaryFileHolder loadFile(String fileName) throws Exception {
        byte[] fileData = cloudStorage.readAllBytes(bucketName, fileName);
        Blob fileBlob = cloudStorage.get(bucketName, fileName);
        TemporaryFileHolder f = new TemporaryFileHolder();
        f.setData(fileData);
        f.setFileName(fileName);
        f.setMimeType(fileBlob.getContentType());
        f.setLastModified(fileBlob.getUpdateTime());
        return f;
    }
}
