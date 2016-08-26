package net.anotheria.asg.generator.view.meta;


/**
 * This enum lists sorting possibilites for elements in the cms.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum SortingType {
	/**
	 * Default is alphabethical (a,b,c)
	 */
	ALPHABETHICAL,
	/**
	 * Numerical for ids and other numbers.
	 */
	NUMERICAL{
		public String getJavaType(){
			return "int";
		}
		public String getCompareCall(){
			return "compareInt";
		}
		public String convertValue(String variableName){
			return "Integer.parseInt("+variableName+")";
		}
	}
	,
	/**
	 * Containers is for lists and other containers.
	 */
	CONTAINERS{
		public String getJavaType(){
			return "List<String>";
		}
		
		public String getCompareCall(){
			return "compareList";
		}
		 
	}
	;
	/**
	 * <p>getJavaType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getJavaType(){
		return "String"; 
	}
	
	/**
	 * <p>getCompareCall.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCompareCall(){
		return "compareString";
	}
	
	/**
	 * <p>convertValue.</p>
	 *
	 * @param variableName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String convertValue(String variableName){
		return variableName;
	}
}
