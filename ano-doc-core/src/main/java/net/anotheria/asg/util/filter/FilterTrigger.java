package net.anotheria.asg.util.filter;

/**
 * Triggers which control a filter. For example letter A-Z for the DocumentName filter.
 *
 * @author another
 * @version $Id: $Id
 */
public class FilterTrigger implements Cloneable{
	/**
	 * Caption of the trigger.
	 */
	private String caption;
	/**
	 * Parameter assigned to this trigger.
	 */
	private String parameter;
	/**
	 * If true the trigger is currently selected (active).
	 */
	private boolean isSelected;
	
	/**
	 * <p>Constructor for FilterTrigger.</p>
	 */
	public FilterTrigger(){
		
	}
	
	/**
	 * <p>Constructor for FilterTrigger.</p>
	 *
	 * @param aCaption a {@link java.lang.String} object.
	 * @param aParameter a {@link java.lang.String} object.
	 */
	public FilterTrigger(String aCaption, String aParameter){
		caption = aCaption;
		parameter = aParameter;
	}
	
	/**
	 * <p>Getter for the field <code>caption</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * <p>Setter for the field <code>caption</code>.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * <p>isSelected.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSelected() {
		return isSelected;
	}
	/**
	 * <p>setSelected.</p>
	 *
	 * @param isSelected a boolean.
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	/**
	 * <p>Getter for the field <code>parameter</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * <p>Setter for the field <code>parameter</code>.</p>
	 *
	 * @param parameter a {@link java.lang.String} object.
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "( "+caption+", "+parameter+")";
	}
}
