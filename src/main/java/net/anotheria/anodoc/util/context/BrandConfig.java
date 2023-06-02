package net.anotheria.anodoc.util.context;

import java.util.List;

/**
 * Brand configuration.
 *
 * @author ykalapusha
 */
public class BrandConfig {
    /**
     * Name of brand.
     */
    private final String name;
    /**
     * Is this brand default.
     */
    private final boolean defaultBrand;
    /**
     * Urls according to this brand.
     */
    private final List<String> urlsToMap;
    /**
     * Localizations according to this brand.
     */
    private final List<String> localizations;
    /**
     * Logo image id.
     */
    private final String logoId;

    /**
     * Constructor.
     *
     * @param name          name of brand
     * @param defaultBrand  is default
     * @param urlsToMap     urls of this brand
     * @param localizations localizations of this brand
     */
    public BrandConfig(String name, boolean defaultBrand, List<String> urlsToMap, List<String> localizations, String logoId) {
        this.name = name;
        this.defaultBrand = defaultBrand;
        this.urlsToMap = urlsToMap;
        this.localizations = localizations;
        this.logoId = logoId;
    }

    public String getName() {
        return name;
    }

    public boolean isDefaultBrand() {
        return defaultBrand;
    }

    public List<String> getUrlsToMap() {
        return urlsToMap;
    }

    public List<String> getLocalizations() {
        return localizations;
    }

    public String getLogoId() {
        return logoId;
    }

    @Override
    public String toString() {
        return "BrandConfig{" +
                "name='" + name + '\'' +
                ", defaultBrand=" + defaultBrand +
                ", urlsToMap=" + urlsToMap +
                ", localizations=" + localizations +
                ", logoId='" + logoId + '\'' +
                '}';
    }
}
