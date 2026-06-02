package net.anotheria.asg.util.bean;

/**
 * Menu item presentation.
 *
 * @author ykalapusha
 * since: 05.12.2024
 */
public class MenuItemBean {
    private String caption;
    private String link;
    private boolean active;
    private String style;


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
