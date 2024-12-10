package net.anotheria.anodoc.util.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import net.anotheria.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.List;

/**
 * Linode cloud storage.
 *
 * @author asamoilich.
 */
public class S3CloudStorage implements IStorage {
    /**
     * {@link Logger} instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(S3CloudStorage.class);

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
    public void save(Hashtable<String, Hashtable> toSave, String filePath) {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(toSave);
            oos.flush();
            ByteArrayInputStream input = new ByteArrayInputStream(bos.toByteArray());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(input.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, input, metadata);
            conn.putObject(putObjectRequest);
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
            S3Object object = conn.getObject(new GetObjectRequest(bucketName, filePath));
            ByteArrayInputStream bis = new ByteArrayInputStream(IOUtils.readBytes(object.getObjectContent()));
            oIn = new ObjectInputStream(bis);
            return (Hashtable) oIn.readObject();
        } finally {
            IOUtils.closeIgnoringException(oIn);
        }
    }
}
