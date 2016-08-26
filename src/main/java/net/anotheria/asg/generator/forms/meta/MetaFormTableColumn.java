package net.anotheria.asg.generator.forms.meta;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFormTableColumn {
	private MetaFormSingleField field;
	private MetaFormTableHeader header;
	
	/**
	 * <p>Constructor for MetaFormTableColumn.</p>
	 */
	public MetaFormTableColumn(){
		
	}
	
	/**
	 * <p>Constructor for MetaFormTableColumn.</p>
	 *
	 * @param aHeader a {@link net.anotheria.asg.generator.forms.meta.MetaFormTableHeader} object.
	 * @param aField a {@link net.anotheria.asg.generator.forms.meta.MetaFormSingleField} object.
	 */
	public MetaFormTableColumn(MetaFormTableHeader aHeader, MetaFormSingleField aField){
		header = aHeader;
		field = aField;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return "column "+header+", "+field;
	}
	/**
	 * <p>Getter for the field <code>field</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.forms.meta.MetaFormSingleField} object.
	 */
	public MetaFormSingleField getField() {
		return field;
	}

	/**
	 * <p>Getter for the field <code>header</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.forms.meta.MetaFormTableHeader} object.
	 */
	public MetaFormTableHeader getHeader() {
		return header;
	}

	/**
	 * <p>Setter for the field <code>field</code>.</p>
	 *
	 * @param field a {@link net.anotheria.asg.generator.forms.meta.MetaFormSingleField} object.
	 */
	public void setField(MetaFormSingleField field) {
		this.field = field;
	}

	/**
	 * <p>Setter for the field <code>header</code>.</p>
	 *
	 * @param header a {@link net.anotheria.asg.generator.forms.meta.MetaFormTableHeader} object.
	 */
	public void setHeader(MetaFormTableHeader header) {
		this.header = header;
	}

}
