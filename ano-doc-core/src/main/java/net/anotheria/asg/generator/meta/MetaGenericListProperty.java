package net.anotheria.asg.generator.meta;

/**
 * <p>MetaGenericListProperty class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaGenericListProperty extends MetaGenericProperty{
	/**
	 * <p>Constructor for MetaGenericListProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param contained a {@link net.anotheria.asg.generator.meta.MetaProperty} object.
	 */
	public MetaGenericListProperty(String name, MetaProperty... contained){
		super(name, MetaProperty.Type.LIST, contained);
	}

	/**
	 * <p>toPropertyGetterCall.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertyGetterCall(){
		return "copyTo"+getContainedProperties().get(0).toJavaObjectType()+"List(getList("+toNameConstant()+"))";
	}

	/** {@inheritDoc} */
	public String toPropertyGetterCall(String language){
		return "copyTo"+getContainedProperties().get(0).toJavaObjectType()+"List(getList("+toNameConstant(language)+"))";
	}

	/**
	 * <p>toPropertySetterCall.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertySetterCall(){
		return "setList("+toNameConstant()+", copyFrom"+getContainedProperties().get(0).toJavaObjectType()+"List(value))";
	}

	/** {@inheritDoc} */
	public String toPropertySetterCall(String language){
		return "setList("+toNameConstant(language)+", copyFrom"+getContainedProperties().get(0).toJavaObjectType()+"List(value))";
	}

	/** {@inheritDoc} */
	public String toPropertyGetterCallForCurrentLanguage(String langVariable){
		return "copyTo"+getContainedProperties().get(0).toJavaObjectType()+"List(getList(\""+getName()+"_\"+"+langVariable+"))";
	}

	/** {@inheritDoc} */
	public String toPropertySetterCallForCurrentLanguage(String langVariable){
		return "setList("+"\""+getName()+"_\"+"+langVariable+", copyFrom"+getContainedProperties().get(0).toJavaObjectType()+"List(value))";
	}


}
