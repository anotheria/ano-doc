package net.anotheria.anodoc.util.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageOptions;
import net.anotheria.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Google cloud storage.
 *
 * @author asamoilich.
 */
public class GoogleCloudStorage implements IStorage {
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
    public void save(Hashtable<String, Hashtable> toSave, String filePath) {
        ObjectOutputStream oos = null;
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(toSave);
            oos.flush();

            BlobId blobId = BlobId.of(bucketName, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            cloudStorage.create(blobInfo, bos.toByteArray());
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("save", e);
        } finally {
            IOUtils.closeIgnoringException(oos);
        }
    }

    @Override
    public Hashtable load(String filePath) throws Exception {
        ObjectInputStream oIn = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(cloudStorage.readAllBytes(bucketName, filePath));
            oIn = new ObjectInputStream(bis);
            return (Hashtable) oIn.readObject();
        } finally {
            IOUtils.closeIgnoringException(oIn);
        }
    }
}
