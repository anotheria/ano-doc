package net.anotheria.asg.util.filestorage.storage;

/**
 * Storage type values.
 *
 * @author asamoilich.
 */
public enum FileStorageType {
    /**
     * File system.
     */
    FS("fs"),
    /**
     * Google cloud storage.
     */
    GCS("gcs"),
    /**
     * S3 cloud storage.
     */
    S3("s3");
    /**
     * Storage type value.
     */
    private final String typeValue;

    FileStorageType(String typeValue) {
        this.typeValue = typeValue;
    }

    public static FileStorageType getByTypeValue(String typeValue) {
        for (FileStorageType type : FileStorageType.values()) {
            if (type.getTypeValue().equalsIgnoreCase(typeValue)) {
                return type;
            }
        }
        return FS;
    }

    public String getTypeValue() {
        return typeValue;
    }
}
