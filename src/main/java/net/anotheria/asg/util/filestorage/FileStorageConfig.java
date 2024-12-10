package net.anotheria.asg.util.filestorage;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.LoggerFactory;


/**
 * File storage configuration.
 *
 * @author h3ll
 */
@ConfigureMe(name = "filestorage")
public class FileStorageConfig {

	/**
	 * Default file storage directory.
	 */
	private static final String DEF_FILE_STORAGE_DIR = "/work/data/files/";

	/**
	 * FileStorageConfig instance.
	 */
	private static FileStorageConfig instance;
	/**
	 * Actually storage directory.
	 */
	@Configure
	private String directory;
    /**
     * Bucket name for asg content.
     */
    @Configure
    private String bucketName;
    /**
     * Project id / S3 endpoint.
     */
    @Configure
    private String projectId;
    /**
     * Credential path for google service account file.
     */
    @Configure
    private String credentialsPath;

    /**
     * Storage type.
     */
    @Configure
    private String storageType;
    /**
     * Access key.
     */
    @Configure
    private String accessKey;

    /**
     * Secret key.
     */
    @Configure
    private String secretKey;

	/**
	 * Get instance method.
	 *
	 * @return {@link FileStorageConfig}
	 */
	public static synchronized FileStorageConfig getInstance() {
		if (instance == null) {
			instance = new FileStorageConfig();
			try {
				ConfigurationManager.INSTANCE.configure(instance);
			} catch (Exception e) {
                LoggerFactory.getLogger(FileStorageConfig.class).warn("FileStorage configuration failed! Relying on defaults!", e);
			}
		}
		return instance;
	}

	/**
	 * Constructor.
	 */
	private FileStorageConfig() {
		this.directory = DEF_FILE_STORAGE_DIR;
	}

    public String getDirectory() {
		return directory;
	}

	public void setDirectory(String aFileStorageDir) {
		this.directory = aFileStorageDir;
	}

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCredentialsPath() {
        return credentialsPath;
    }

    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
	public String toString() {
		return "FileStorageConfig{" +
				"directory='" + directory + '\'' +
				'}';
	}
}
