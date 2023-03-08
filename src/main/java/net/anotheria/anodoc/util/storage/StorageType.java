package net.anotheria.anodoc.util.storage;

/**
 * Storage type values.
 *
 * @author asamoilich.
 */
public enum StorageType {
    /**
     * File system.
     */
    FS("fs"),
    /**
     * Google cloud storage.
     */
    GCS("gcs"),
    /**
     * Linode cloud storage.
     */
    LCS("lcs");
    /**
     * Storage type value.
     */
    private final String typeValue;

    StorageType(String typeValue) {
        this.typeValue = typeValue;
    }

    public static StorageType getByTypeValue(String typeValue) {
        for (StorageType type : StorageType.values()) {
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
