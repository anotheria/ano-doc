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
import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.asg.util.filestorage.FileStorageConfig;
import net.anotheria.asg.util.filestorage.TemporaryFileHolder;
import net.anotheria.util.queue.IQueueWorker;
import net.anotheria.util.queue.QueuedProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    /**
     * Bucket name for storing photos data.
     */
    private final String bucketName;
    /**
     * {@link Storage} instance.
     */
    private final Storage cloudStorage;
    /**
     * Cache for storing metadata of files.
     */
    private final Cache<String, Blob> blobInfoCache;
    /**
     * A {@link QueuedProcessor} for processing file metadata and storing files in a cache directory.
     */
    private final QueuedProcessor<String> fileProcessor;

    /**
     * Default constructor.
     *
     * @param bucketName        bucket name
     * @param credentialsPath   credentials path for connect to bucket
     * @param projectId         google project id
     */
    public GoogleCloudStorage(String bucketName, String credentialsPath, String projectId) {
        this.bucketName = bucketName;
        this.blobInfoCache = Caches.createHardwiredCache(FileStorageConfig.getInstance().getCacheName(),
                FileStorageConfig.getInstance().getCacheMinSize(),
                FileStorageConfig.getInstance().getCacheMaxSize());
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

        createCacheDirectory();
        this.fileProcessor = new QueuedProcessor<>("ASGFileCacheProcessor", new FileProcessor(), 1_000, LOGGER);
        this.fileProcessor.start();
    }

    private void initializeBucket() {
        Bucket bucket = cloudStorage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.NAME));
        if (bucket == null) {
            //create bucket
            bucket = cloudStorage.create(BucketInfo.newBuilder(bucketName)
                    .setStorageClass(StorageClass.STANDARD)
                    .setLocation("EU")
                    .build());
            LOGGER.info("Bucket created: {}", bucket.toString());
        }
    }

    private void createCacheDirectory() {
        File cacheDirectory = new File(FileStorageConfig.getInstance().getCacheDirectory());
        if (!cacheDirectory.exists()) {
            boolean created = cacheDirectory.mkdirs();
            LOGGER.info("Cache directory created: {}", created);
        }
    }

    @Override
    public void storeFile(byte[] fileContent, String fileName) throws Exception {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        cloudStorage.create(blobInfo, fileContent);
        fileProcessor.addToQueue(fileName);
    }

    @Override
    public boolean isFileExists(String fileName) {
        return blobInfoCache.get(fileName) != null;
    }

    @Override
    public void cloneFile(String sourceFileName, String destinationFileName) throws Exception {
        byte[] data = cloudStorage.readAllBytes(bucketName, sourceFileName);
        BlobId blobId = BlobId.of(bucketName, destinationFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        cloudStorage.create(blobInfo, data);
        fileProcessor.addToQueue(destinationFileName);
    }

    @Override
    public void removeFile(String fileName) throws Exception {
        cloudStorage.delete(bucketName, fileName);
        blobInfoCache.remove(fileName);
        File file = new File(FileStorageConfig.getInstance().getCacheDirectory(), fileName);
        boolean deleted = file.delete();
        LOGGER.info("File deleted: {}", deleted);
    }

    @Override
    public TemporaryFileHolder loadFile(String fileName) throws Exception {
        Blob blob = blobInfoCache.get(fileName);
        byte[] fileData;

        if (blob != null) {
            fileData = Files.readAllBytes(Paths.get(FileStorageConfig.getInstance().getCacheDirectory(), fileName));
        } else {
            blob = cloudStorage.get(bucketName, fileName);
            fileData = cloudStorage.readAllBytes(bucketName, fileName);
        }

        TemporaryFileHolder f = new TemporaryFileHolder();
        f.setData(fileData);
        f.setFileName(fileName);
        f.setMimeType(blob.getContentType());
        f.setLastModified(blob.getUpdateTimeOffsetDateTime().toEpochSecond());
        fileProcessor.addToQueue(fileName);
        return f;
    }

    private class FileProcessor implements IQueueWorker<String>{

        @Override
        public void doWork(String fileName) throws Exception {
            try {
                Blob cached = blobInfoCache.get(fileName);
                Blob actual = cloudStorage.get(bucketName, fileName);
                if ((cached == null && actual != null) || (cached != null && actual != null && !cached.getEtag().equals(actual.getEtag()))) {
                    blobInfoCache.put(fileName, actual);
                    File file = new File(FileStorageConfig.getInstance().getCacheDirectory(), fileName);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(cloudStorage.readAllBytes(bucketName, fileName));
                    }
                }
            } catch (Exception e){
                LOGGER.warn("Unable to process data for file", e);
            }
        }
    }
}
