package net.anotheria.asg.generator.view.meta;

import java.util.List;

import net.anotheria.util.StringUtils;


/**
 * Represents an element of the view.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaViewElement {
	/**
	 * True if the element is readonly.
	 */
	private boolean readonly;
    /**
     * True if autocomplete for this element in browser is off.
     */
    private boolean autocompleteOff;
	/**
	 * The name of the element.
	 */
	private String name;
	/**
	 * The caption of the element.
	 * Will be displayed in CMS instead of name.
	 */
	private String caption;
	/**
	 * The description of the element.
	 */
	private String description;
	/**
	 * If true the element is comparable.
	 */
	private boolean comparable;
	/**
	 * If true the element is rich element.
	 */
	private boolean rich;
	/**
	 * If true the element is datetime in long.
	 */
	private boolean datetime;
	/**
	 * If true the link to the element source need to be shown.
	 */
	private boolean showLink;
	/**
	 * The decorator for the element.
	 */
	private MetaDecorator decorator;
	/**
	 * Validators that will validate sumbitted value.
	 */
	private List<MetaValidator> validators;

	/**
	 * The sorting type of the element.
	 */
	private SortingType sortingType = SortingType.ALPHABETHICAL;


	/**
	 * Creates a new meta view element.
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	public MetaViewElement(String aName){
		this.name = aName;
	}

	/**
	 * <p>isReadonly.</p>
	 *
	 * @return True if the element is readonly
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * Sets if the element is readonly or not.
	 *
	 * @param b flag to set
	 */
	public void setReadonly(boolean b) {
		readonly = b;
	}

    /**
     * <p>isAutocompleteOff.</p>
     *
     * @return True if autocomplete for this element is off.
     */
    public boolean isAutocompleteOff() {
        return autocompleteOff;
    }

    /**
     * Sets if the element allows autocompletion or not.
     *
     * @param b flag to set
     */
    public void setAutocompleteOff(boolean b) {
        this.autocompleteOff = b;
    }

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return name of the element
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of the element.
	 *
	 * @param string name ot set
	 */
	public void setName(String string) {
		name = string;
	}


	/**
	 * <p>isComparable.</p>
	 *
	 * @return true if the element is comparable, otherwise - false
	 */
	public boolean isComparable() {
		return comparable;
	}

	/**
	 * Sets if document ios comparable.
	 *
	 * @param b flag to set
	 */
	public void setComparable(boolean b) {
		comparable = b;
	}


	/**
	 * <p>Getter for the field <code>decorator</code>.</p>
	 *
	 * @return decorator for the element
	 */
	public MetaDecorator getDecorator() {
		return decorator;
	}

	/**
	 * Sets decorator for the element.
	 *
	 * @param decorator decorator to set
	 */
	public void setDecorator(MetaDecorator decorator) {
		this.decorator = decorator;
		//this is a hack to prevent need for altering all files, fow now.
		if (decorator!=null && name!=null && name.equals("id"))
			sortingType = SortingType.NUMERICAL;

	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o){
		return (o instanceof MetaViewElement) && ((MetaViewElement)o).getName().equals(getName());
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	/**
	 * <p>isRich.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isRich() {
		return rich;
	}

	/**
	 * <p>Setter for the field <code>rich</code>.</p>
	 *
	 * @param rich a boolean.
	 */
	public void setRich(boolean rich) {
		this.rich = rich;
	}

	/**
	 * <p>isDatetime.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isDatetime() {
		return datetime;
	}

	/**
	 * <p>Setter for the field <code>datetime</code>.</p>
	 *
	 * @param datetime a boolean.
	 */
	public void setDatetime(boolean datetime) {
		this.datetime = datetime;
	}
	/**
	 * <p>isShowLink.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isShowLink() {
		return showLink;
	}
	/**
	 * <p>Setter for the field <code>showLink</code>.</p>
	 *
	 * @param showLink a boolean.
	 */
	public void setShowLink(boolean showLink) {
		this.showLink = showLink;
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
	 * <p>Getter for the field <code>caption</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * <p>Setter for the field <code>description</code>.</p>
	 *
	 * @param description a {@link java.lang.String} object.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <p>Getter for the field <code>description</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>Setter for the field <code>validators</code>.</p>
	 *
	 * @param validator a {@link java.util.List} object.
	 */
	public void setValidators(List<MetaValidator> validator) {
		this.validators = validator;
	}

	/**
	 * <p>Getter for the field <code>validators</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<MetaValidator> getValidators() {
		return validators;
	}

	/**
	 * <p>isValidated.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isValidated() {
		return validators != null && !validators.isEmpty();
	}

	/**
	 * <p>isJSValidated.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isJSValidated() {
		if (isValidated()) {
			for (MetaValidator validator : validators){
				if (!StringUtils.isEmpty(validator.getJsValidation())){
					return true;
				}
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "MetaViewElement{" +
				"readonly=" + readonly +
				", autocompleteOff=" + autocompleteOff +
                ", name='" + name + '\'' +
				", caption='" + caption + '\'' +
				", description='" + description + '\'' +
				", comparable=" + comparable +
				", rich=" + rich +
				", datetime=" + datetime +
				", decorator=" + decorator +
				", validators=" + validators +
				", showLink=" + showLink +
				'}';
	}

	/**
	 * <p>Getter for the field <code>sortingType</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.view.meta.SortingType} object.
	 */
	public SortingType getSortingType() {
		return sortingType;
	}

	/**
	 * <p>Setter for the field <code>sortingType</code>.</p>
	 *
	 * @param sortingType a {@link net.anotheria.asg.generator.view.meta.SortingType} object.
	 */
	public void setSortingType(SortingType sortingType) {
		this.sortingType = sortingType;
	}


}
