package net.anotheria.asg.generator.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>MetaGenericProperty class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaGenericProperty extends MetaProperty{
	
	private List<MetaProperty> containedProperties;
	
	/**
	 * <p>Constructor for MetaGenericProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link net.anotheria.asg.generator.meta.MetaProperty.Type} object.
	 * @param contained a {@link net.anotheria.asg.generator.meta.MetaProperty} object.
	 */
	public MetaGenericProperty(String name, MetaProperty.Type type, MetaProperty... contained){
		super(name, type);
		containedProperties = new ArrayList<MetaProperty>();
		for (MetaProperty p : contained)
			containedProperties.add(p);
	}
	
	/**
	 * <p>toJavaType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toJavaType(){
		return super.toJavaType()+"<"+getGenericTypeDeclaration()+">";
	}
	
	/**
	 * <p>getGenericTypeDeclaration.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getGenericTypeDeclaration(){
		String ret = "";
		for (MetaProperty p : containedProperties){
			if (ret.length()>0)
				ret += ",";
			ret += p.toJavaObjectType();
		}
			
		return ret;
	}
	
	/**
	 * <p>Getter for the field <code>containedProperties</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	protected List<MetaProperty> getContainedProperties(){
		return containedProperties;
	}
	
	/**
	 * <p>toPropertyGetterCall.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertyGetterCall(){
		throw new RuntimeException("Not supported :-(");
	}
	
	/**
	 * <p>toPropertyGetterCall.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertyGetterCall(String language){
		throw new RuntimeException("Not supported :-(");
	}	

	/**
	 * <p>toPropertyGetterCallForCurrentLanguage.</p>
	 *
	 * @param langVariable a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertyGetterCallForCurrentLanguage(String langVariable){
		throw new RuntimeException("Not supported :-(");
	}	
	
	/**
	 * <p>toPropertySetterCallForCurrentLanguage.</p>
	 *
	 * @param langVariable a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertySetterCallForCurrentLanguage(String langVariable){
		throw new RuntimeException("Not supported :-(");
	}	

	/**
	 * <p>toPropertySetterCall.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertySetterCall(){
		throw new RuntimeException("Not supported :-(");
	}

	/**
	 * <p>toPropertySetterCall.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertySetterCall(String language){
		throw new RuntimeException("Not supported :-(");
	}
}
