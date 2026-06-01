package net.anotheria.asg.util.filestorage.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import net.anotheria.asg.util.filestorage.TemporaryFileHolder;
import net.anotheria.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * S3 cloud storage.
 *
 * @author asamoilich.
 */
public class S3CloudStorage implements IFileStorage {

    private final String bucketName;
    /**
     * {@link AmazonS3} instance.
     */
    private final AmazonS3 conn;

    public S3CloudStorage(String bucketName, String accessKey, String secretKey, String endPoint) {
        this.bucketName = bucketName;
        try {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            conn = new AmazonS3Client(credentials);
            conn.setEndpoint(endPoint);
            initializeBucket();
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize google storage. ", e);
        }
    }

    private void initializeBucket() {
        final List<Bucket> buckets = conn.listBuckets();
        for (Bucket bucket : buckets) {
            if (bucket.getName().equals(bucketName)) {
                return;
            }
        }
        conn.createBucket(bucketName);
    }

    @Override
    public void storeFile(byte[] fileContent, String fileName) throws Exception {
        ObjectMetadata metadata = new ObjectMetadata();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        metadata.setContentLength(inputStream.available());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
        conn.putObject(putObjectRequest);
    }

    @Override
    public boolean isFileExists(String fileName) {
        return conn.doesObjectExist(bucketName, fileName);
    }

    @Override
    public void cloneFile(String sourceFileName, String destinationFileName) throws Exception {
        S3Object object = conn.getObject(new GetObjectRequest(bucketName, sourceFileName));
        conn.putObject(bucketName, destinationFileName, new ByteArrayInputStream(IOUtils.readBytes(object.getObjectContent())), new ObjectMetadata());
    }

    @Override
    public void removeFile(String fileName) throws Exception {
        conn.deleteObject(bucketName, fileName);
    }

    @Override
    public TemporaryFileHolder loadFile(String fileName) throws Exception {
        S3Object object = conn.getObject(new GetObjectRequest(bucketName, fileName));
        TemporaryFileHolder f = new TemporaryFileHolder();
        f.setData(IOUtils.readBytes(object.getObjectContent()));
        f.setFileName(fileName);
        f.setMimeType(object.getObjectMetadata().getContentType());
        f.setLastModified(object.getObjectMetadata().getLastModified().getTime());
        return f;
    }
}
