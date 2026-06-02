package net.anotheria.asg.util.bean;

import java.util.List;

public class NavigationItemBean {
	private List<NavigationItemBean> subNavi;
	private String caption;
	private String link;
	private boolean active;
	
	public String toString(){
		return caption+", "+link+", "+active;
	}
	/**
	 * @return
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param string
	 */
	public void setCaption(String string) {
		caption = string;
	}

	/**
	 * @param b
	 */
	public void setActive(boolean b) {
		active = b;
	}

	/**
	 * @param string
	 */
	public void setLink(String string) {
		link = string;
	}
	
	public List<NavigationItemBean> getSubNavi() {
		return subNavi;
	}
	
	public void setSubNavi(List<NavigationItemBean> subNavi) {
		this.subNavi = subNavi;
	}

}
