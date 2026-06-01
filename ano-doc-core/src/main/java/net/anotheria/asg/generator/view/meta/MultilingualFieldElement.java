package net.anotheria.asg.generator.view.meta;

import net.anotheria.util.StringUtils;

import java.util.List;

/**
 * If an element is specified to be multilingual, it's replaced by a multilingual field element for each language. This way
 * generation of language specific data for each language is guaranteed.
 *
 * @author another
 * @version $Id: $Id
 */
public class MultilingualFieldElement extends MetaFieldElement{
	/**
	 * Elements language.
	 */
	private String language;
	/**
	 * The element this copy refers to.
	 */
	private MetaFieldElement mappedElement;
	
	/**
	 * <p>Constructor for MultilingualFieldElement.</p>
	 *
	 * @param aLanguage a {@link java.lang.String} object.
	 * @param aMappedElement a {@link net.anotheria.asg.generator.view.meta.MetaFieldElement} object.
	 */
	public MultilingualFieldElement(String aLanguage, MetaFieldElement aMappedElement){
		super(aMappedElement.getName());
		language = aLanguage;
		mappedElement = aMappedElement;
	}
	
	/**
	 * <p>Getter for the field <code>language</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLanguage(){
		return language;
	}
	
	/**
	 * <p>Getter for the field <code>mappedElement</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.view.meta.MetaFieldElement} object.
	 */
	public MetaFieldElement getMappedElement(){
		return mappedElement;
	}

	/** {@inheritDoc} */
	@Override
	public MetaDecorator getDecorator() {
		return mappedElement.getDecorator();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isComparable() {
		return mappedElement.isComparable();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReadonly() {
		return mappedElement.isReadonly();
	}

    /** {@inheritDoc} */
    @Override
    public boolean isAutocompleteOff() {
        return mappedElement.isAutocompleteOff();
    }
	
	/** {@inheritDoc} */
	@Override
	public boolean isRich() {
		return mappedElement.isRich();
	}
	
	/** {@inheritDoc} */
	@Override 
	public String getCaption() {
		return mappedElement.getCaption();
	}
	
	/** {@inheritDoc} */
	@Override 
	public String getDescription() {
		return mappedElement.getDescription();
	}
	
	/** {@inheritDoc} */
	@Override 
	public List<MetaValidator> getValidators() {
		return mappedElement.getValidators();
	}
	
	/** {@inheritDoc} */
	@Override 
	public boolean isValidated() {
		return mappedElement.isValidated();
	}
	
	/** {@inheritDoc} */
	@Override 
	public boolean isJSValidated() {
		return mappedElement.isJSValidated();
	}
	
	/** {@inheritDoc} */
	@Override
	public String getVariableName(){
		return getName()+StringUtils.capitalize(language);
	}


	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}


}
