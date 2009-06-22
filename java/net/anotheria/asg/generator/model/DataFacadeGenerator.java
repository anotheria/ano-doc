package net.anotheria.asg.generator.model;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.TypeOfClass;
import net.anotheria.asg.generator.meta.MetaContainerProperty;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaGenericProperty;
import net.anotheria.asg.generator.meta.MetaListProperty;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.meta.MetaTableProperty;
import net.anotheria.util.StringUtils;

/**
 * This generator generates the data facade - the interface which defines the behaviour of the document and its attributes. It also generates the 
 * sort type and the builder. 
 * @author another
 *
 */
public class DataFacadeGenerator extends AbstractDataObjectGenerator implements IGenerator{

	public static final String PROPERTY_DECLARATION = "public static final String ";	
	
	private Context context;
	
	public List<FileEntry> generate(IGenerateable gdoc, Context context){
		MetaDocument doc = (MetaDocument)gdoc;
		this.context = context;
		
		//System.out.println(ret);
		List<FileEntry> _ret = new ArrayList<FileEntry>();
		_ret.add(new FileEntry(generateDocument(doc)));
		_ret.add(new FileEntry(generateSortType(doc)));
		_ret.add(new FileEntry(generateXMLHelper(doc)));
		_ret.add(new FileEntry(generateBuilder(doc)));
		return _ret;
	}
	
	public String getDocumentName(MetaDocument doc){
		return doc.getName();
	}
	
	public static String getSortTypeName(MetaDocument doc){
		return doc.getName()+"SortType";
	}
	
	public static String getXMLHelperName(MetaDocument doc){
		return doc.getName()+"XMLHelper";
	}
	
	private GeneratedClass generateBuilder(MetaDocument doc){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
	
		clazz.setPackageName(getPackageName(doc));
		clazz.setName(getDocumentBuilderName(doc));
		
		clazz.addInterface("Builder<"+doc.getName()+">");
		clazz.addImport(net.anotheria.asg.data.Builder.class);
		
		startClassBody();
		
		for (int i=0; i<doc.getProperties().size(); i++){
			if (doc.getProperties().get(i) instanceof MetaContainerProperty){
				clazz.addImport("java.util.List");
				break;
			}
		}

		for (MetaProperty p : doc.getProperties()){
			appendStatement("protected "+p.toJavaType()+" "+p.getName());
		}
		for (MetaProperty p : doc.getLinks()){
			appendStatement("protected "+p.toJavaType()+" "+p.getName());
		}
		emptyline();
		
		generateBuilderPropertyAccessMethods(doc);
		emptyline();
		//generateAdditionalMethods(doc);
		//emptyline();
		
		appendString("public "+doc.getName()+" build(){");
		increaseIdent(); 
		appendStatement("return "+doc.getName()+"Factory.create"+doc.getName()+"(this)");
		append(closeBlock());
		
		return clazz;
	}
	
	private GeneratedClass generateSortType(MetaDocument doc){
		
		List<MetaProperty> properties = extractSortableProperties(doc);

		if (properties.size()==0)
			return null;

		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
	
		clazz.setPackageName(getPackageName(doc));
		clazz.addImport("net.anotheria.util.sorter.SortType");
		
		clazz.setName(getSortTypeName(doc));
		clazz.setParent("SortType");

		startClassBody();
		
		int lastIndex = 1;
		for (int i=0; i<properties.size(); i++){
			MetaProperty p = properties.get(i);
			appendStatement("public static final int SORT_BY_"+p.getName().toUpperCase()+" = "+(lastIndex++));
		}
		appendStatement("public static final int SORT_BY_DEFAULT = SORT_BY_ID");

		emptyline();

		appendString("public "+getSortTypeName(doc)+"(){");
		increaseIdent();
		appendString("super(SORT_BY_DEFAULT);");
		append(closeBlock());
		emptyline();

		appendString("public "+getSortTypeName(doc)+"(int method){");
		increaseIdent();
		appendString("super(method);");
		append(closeBlock());
		emptyline();
				
		appendString("public "+getSortTypeName(doc)+"(int method, boolean order){");
		increaseIdent();
		appendString("super(method, order);");
		append(closeBlock());
		emptyline();
		
		appendString("public static int name2method(String name){");
		increaseIdent();
		for (int i=0; i<properties.size(); i++){
			MetaProperty p = properties.get(i);
				appendString("if ("+quote(p.getName())+".equals(name))");
				appendIncreasedStatement("return SORT_BY_"+p.getName().toUpperCase());
		}
		appendStatement("throw new RuntimeException("+quote("Unknown sort type name: ")+"+name)");		
		append(closeBlock());

		return clazz;
	}
	
	private GeneratedClass generateXMLHelper(MetaDocument doc){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
		
		clazz.setPackageName(getPackageName(doc));
		
		clazz.addImport("net.anotheria.util.xml.XMLNode");
		clazz.addImport("net.anotheria.util.xml.XMLAttribute");
		clazz.addImport("net.anotheria.asg.data.XMLHelper");
		
		for (int i=0; i<doc.getProperties().size(); i++){
			if (doc.getProperties().get(i) instanceof MetaContainerProperty){
				clazz.addImport("java.util.List");
				break;
			}
		}
		for (int i=0; i<doc.getProperties().size(); i++){
			if (doc.getProperties().get(i) instanceof MetaTableProperty){
				clazz.addImport("java.util.List");
				break;
			}
		}
		
		clazz.setName(getXMLHelperName(doc));
		
		startClassBody();
		increaseIdent();
		if (context.areLanguagesSupported()){
			String langArray = "";
			for (String l : context.getLanguages()){
				if (langArray.length()>0 )
					langArray += ",";
				langArray += quote(l);
			}
			appendStatement("public static final String[] LANGUAGES = new String[]{"+langArray+"}");
		}

		//generates generic to xml method
		appendString("private static XMLNode _toXML("+doc.getName()+" object, String[] languages){");
		increaseIdent();
		appendStatement("XMLNode ret = new XMLNode("+quote(doc.getName())+")");
		appendStatement("ret.addAttribute(new XMLAttribute("+quote("id")+", object.getId()))");
		emptyline();
		for (MetaProperty p : doc.getProperties()){
			generatePropertyToXMLMethod(p);
		}
		emptyline();
		for (MetaProperty p : doc.getLinks()){
			generatePropertyToXMLMethod(p);
		}
		appendStatement("return ret");
		append(closeBlock());
		emptyline();
		
		appendString("public static XMLNode toXML("+doc.getName()+" object){");
		increaseIdent();
		appendStatement("return _toXML(object, LANGUAGES)");
		append(closeBlock());
		emptyline();

		//generates toXML method for a single language
		appendString("public static XMLNode toXML("+doc.getName()+" object, String... languages){");
		increaseIdent();
		
		appendString("if (languages==null || languages.length==0)");
		appendIncreasedStatement("return toXML(object)");
		appendStatement("return _toXML(object, languages)");
		append(closeBlock());
		emptyline();
		

		appendString("public static "+doc.getName()+" fromXML(XMLNode node){");
		increaseIdent();
		appendStatement("return null");
		append(closeBlock());
		emptyline();
		
		
		
		//ret += generatePropertyAccessMethods(doc);
		//ret += generateAdditionalMethods(doc);
		return clazz;
	}
	
	private void generatePropertyToXMLMethod(MetaProperty p){
		if (p instanceof MetaTableProperty){
			generateTablePropertyGetterMethods((MetaTableProperty)p);
			return;
		}
		if (p instanceof MetaListProperty){
			generateListPropertyToXMLMethods((MetaListProperty)p);
			return;
		}
		if (context.areLanguagesSupported() && p.isMultilingual()){
			generatePropertyToXMLMethodMultilingual(p);
			return;
		}
		
		appendStatement("ret.addChildNode(XMLHelper.createXMLNodeFor"+StringUtils.capitalize(p.getType())+"Value("+quote(p.getName())+", null, object.get"+p.getAccesserName()+"()	))");
	}
	
	
	private void generatePropertyToXMLMethodMultilingual(MetaProperty p){
		String callArr = "";
		
		for (String l : context.getLanguages()){
			if (callArr.length()>0)
				callArr += ", ";
			callArr += "object.get"+p.getAccesserName(l)+"()";
		}
		appendStatement("ret.addChildNode(XMLHelper.createXMLNodeFor"+StringUtils.capitalize(p.getType())+"Value("+quote(p.getName())+", languages , "+callArr+"	))");
	}
	
	private void generateListPropertyToXMLMethods(MetaListProperty p){
		MetaProperty tmp = new MetaGenericProperty(p.getName(), "list", p.getContainedProperty());
		if (p.isMultilingual())
			tmp.setMultilingual(true);
		generatePropertyToXMLMethod(tmp);
	}
	
	
	
	/*
	private String generatePropertyAccessMethods(MetaDocument doc){
		String ret = "";
		
		ret += _generatePropertyAccessMethods(doc.getProperties());
		ret += _generatePropertyAccessMethods(doc.getLinks());
		return ret;
	}
	
	private String _generatePropertyAccessMethods(List<MetaProperty> properties){
		String ret = "";
		
		for (int i=0; i<properties.size(); i++){
			MetaProperty p = properties.get(i);
			ret += generatePropertyGetterMethod(p);
			emptyline();
			if (!p.isReadonly()){
				ret += generatePropertySetterMethod(p);
				emptyline();
			}
		}
		return ret;
	}

	 */
	
	private GeneratedClass generateDocument(MetaDocument doc){

		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
	
		clazz.setPackageName(getPackageName(doc));
		clazz.addImport("net.anotheria.asg.data.DataObject");
		
		for (int i=0; i<doc.getProperties().size(); i++){
			if (doc.getProperties().get(i) instanceof MetaContainerProperty){
				clazz.addImport("java.util.List");
				break;
			}
		}
		for (int i=0; i<doc.getProperties().size(); i++){
			if (doc.getProperties().get(i) instanceof MetaTableProperty){
				clazz.addImport("java.util.List");
				break;
			}
		}

		clazz.setType(TypeOfClass.INTERFACE);
		clazz.setName(doc.getName());
		clazz.setParent("DataObject");
		if (doc.isComparable()){
			clazz.addImport("net.anotheria.util.sorter.IComparable");
			//todo hack
			clazz.setParent(clazz.getParent()+ ", IComparable ");
		}

		startClassBody();
		generatePropertyConstants(doc);
		emptyline();
		generatePropertyAccessMethods(doc);
		emptyline();
		generateAdditionalMethods(doc);
		emptyline();
		if (hasLanguageCopyMethods(doc)){
			generateLanguageCopyMethods(doc);
			emptyline();
		}
		return clazz;
	}
	
	public static final boolean hasLanguageCopyMethods(MetaDocument doc){
		return GeneratorDataRegistry.hasLanguageCopyMethods(doc);
	}
	
	public static final String getCopyMethodName(){
		return getCopyMethodName("LANG", "LANG");
	}
	public static final String getCopyMethodName(String sourceLange, String targetLang){
		return "copy"+sourceLange.toUpperCase()+"2"+targetLang.toUpperCase();
	}
	
	private void generateLanguageCopyMethods(MetaDocument doc){
		appendComment("Copies all multilingual properties from source language to destination language ");
		appendString("public void "+getCopyMethodName()+"(String sourceLanguge, String destLanguage);");
		emptyline();
		for (String srclang : context.getLanguages()){
			for (String targetlang : context.getLanguages()){
				if (!srclang.equals(targetlang)){
					appendComment("Copies all multilingual properties from language "+srclang+" to language "+targetlang);
					appendString("public void "+getCopyMethodName(srclang, targetlang)+"();");
					emptyline();
				}
			}
		}
	}
	
	private void generatePropertyConstants(MetaDocument doc){
		_generatePropertyConstants(doc.getProperties());
		_generatePropertyConstants(doc.getLinks());
	}
	
	private void _generatePropertyConstants(List<MetaProperty> propertyList){
		for (int i=0; i<propertyList.size(); i++){
			MetaProperty p = propertyList.get(i);
			if (p instanceof MetaTableProperty){
				List<MetaProperty> columns = ((MetaTableProperty)p).getColumns();
				for (int t=0; t<columns.size(); t++)
					_generatePropertyConstant(columns.get(t));
			}else{
				_generatePropertyConstant(p);
			}
		}
	}
	
	private void _generatePropertyConstant(MetaProperty p){
		if (context.areLanguagesSupported() && p.isMultilingual()){
			for (String l: context.getLanguages()){
				String decl = PROPERTY_DECLARATION;
				decl += p.toNameConstant(l);
				decl += "\t= \""+p.getName()+"_"+l+"\"";
				appendComment("Constant property name for \""+p.getName()+"\" and domain \""+l+"\" for internal storage and queries.");
				appendStatement(decl);
			}
		}else{
			String r = PROPERTY_DECLARATION;
			r += p.toNameConstant();
			r += "\t= \""+p.getName()+"\"";
			appendComment("Constant property name for \""+p.getName()+"\" for internal storage and queries.");
			appendStatement(r);
		}
	}
	
	
	private void generatePropertyAccessMethods(MetaDocument doc){
		_generatePropertyAccessMethods(doc.getProperties());
		_generatePropertyAccessMethods(doc.getLinks());
	}
	
	private void _generatePropertyAccessMethods(List<MetaProperty> properties){
		for (int i=0; i<properties.size(); i++){
			MetaProperty p = properties.get(i);
			generatePropertyGetterMethod(p);
			emptyline();
			if (!p.isReadonly()){
				generatePropertySetterMethod(p);
				emptyline();
			}
		}
	}
	
	private void generateBuilderPropertyAccessMethods(MetaDocument doc){
		_generateBuilderPropertyAccessMethods(doc, doc.getProperties());
		_generateBuilderPropertyAccessMethods(doc, doc.getLinks());
	}
	
	private void _generateBuilderPropertyAccessMethods(MetaDocument doc, List<MetaProperty> properties){
		for (int i=0; i<properties.size(); i++){
			MetaProperty p = properties.get(i);
			//generateBuilderPropertyGetterMethod(p);
			//emptyline();
			if (!p.isReadonly()){
				generateBuilderPropertySetterMethod(doc, p);
				emptyline();
			}
		}
	}

	private void generatePropertyGetterMethod(MetaProperty p){
		if (p instanceof MetaTableProperty){
			generateTablePropertyGetterMethods((MetaTableProperty)p);
			return;
		}
		if (p instanceof MetaListProperty){
			generateListPropertyGetterMethods((MetaListProperty)p);
			return;
		}
		if (context.areLanguagesSupported() && p.isMultilingual()){
			generatePropertyGetterMethodMultilingual(p);
			return;
		}
		
		appendComment("Returns the value of the "+p.getName()+" attribute.");
		appendString("public "+p.toJavaType()+" get"+p.getAccesserName()+"();");
		
	}
	
	private void generateBuilderPropertyGetterMethod(MetaProperty p){
		if (p instanceof MetaTableProperty){
			generateBuilderTablePropertyGetterMethods((MetaTableProperty)p);
			return;
		}
		if (p instanceof MetaListProperty){
			generateBuilderListPropertyGetterMethods((MetaListProperty)p);
			return;
		}
		
		appendComment("Returns the value of the "+p.getName()+" attribute.");
		appendString("public "+p.toJavaType()+" get"+p.getAccesserName()+"(){");
		appendIncreasedStatement("return "+p.getName());
		appendString("}");
		
	}

	private void generatePropertyGetterMethodMultilingual(MetaProperty p){
		for (String l : context.getLanguages()){
			appendComment("Returns the value of the "+p.getName()+" attribute in the \""+l+"\" domain.");
			appendString("public "+p.toJavaType()+" get"+p.getAccesserName(l)+"();");
			emptyline();
		}
		appendComment("Returns the current value of the "+p.getName()+" attribute.\nCurrent means in the currently selected domain.");
		appendString("public "+p.toJavaType()+" get"+p.getAccesserName()+"();");
		emptyline();
	}
	
	
	private void generateBuilderListPropertyGetterMethods(MetaListProperty p){
		MetaProperty tmp = new MetaGenericProperty(p.getName(), "list", p.getContainedProperty());
		generateBuilderPropertyGetterMethod(tmp);
	}
	
	private void generateListPropertyGetterMethods(MetaListProperty p){
		MetaProperty tmp = new MetaGenericProperty(p.getName(), "list", p.getContainedProperty());
		if (p.isMultilingual())
			tmp.setMultilingual(true);
		generatePropertyGetterMethod(tmp);
	}

	private void generateBuilderTablePropertyGetterMethods(MetaTableProperty p){
		List<MetaProperty> columns = p.getColumns();
		for (int t=0; t<columns.size(); t++)
			generateBuilderPropertyGetterMethod(columns.get(t));
	}
	
	private void generateTablePropertyGetterMethods(MetaTableProperty p){
		List<MetaProperty> columns = p.getColumns();
		for (int t=0; t<columns.size(); t++)
			generatePropertyGetterMethod(columns.get(t));
	}

	private void generatePropertySetterMethod(MetaProperty p){
		if (p instanceof MetaTableProperty){
			generateTablePropertySetterMethods((MetaTableProperty)p);
			return ;
		}
		if (p instanceof MetaListProperty){
			generateListPropertySetterMethods((MetaListProperty)p);
			return;
		}
		if (context.areLanguagesSupported() && p.isMultilingual()){
			generatePropertySetterMethodMultilingual(p);
			return;
		}

		appendComment("Sets the value of the "+p.getName()+" attribute.");
		appendString("public void set"+p.getAccesserName()+"("+p.toJavaType()+" value);");
		
	}
	
	private void generateBuilderPropertySetterMethod(MetaDocument doc, MetaProperty p){
		if (p instanceof MetaTableProperty){
			generateBuilderTablePropertySetterMethods(doc, (MetaTableProperty)p);
			return ;
		}
		if (p instanceof MetaListProperty){
			generateBuilderListPropertySetterMethods(doc, (MetaListProperty)p);
			return;
		}

		appendComment("Sets the value of the "+p.getName()+" attribute.");
		appendString("public "+getDocumentBuilderName(doc)+" "+p.getName()+"("+p.toJavaType()+" value){");
		increaseIdent();
		appendStatement(p.getName(), " = ", "value");
		appendStatement("return this");
		append(closeBlock());
		
	}

	private void generatePropertySetterMethodMultilingual(MetaProperty p){
		for (String l : context.getLanguages()){
			appendComment("Sets the value of the "+p.getName()+" attribute in the domain \""+l+"\"");
			appendString("public void set"+p.getAccesserName(l)+"("+p.toJavaType()+" value);");
			emptyline();
		}
		appendComment("Sets the value of the "+p.getName()+" attribute in the current domain. Current means in the currently selected domain.");
		appendString("public void set"+p.getAccesserName()+"("+p.toJavaType()+" value);");
	}

	private void generateListPropertySetterMethods(MetaListProperty p){
		MetaProperty tmp = new MetaGenericProperty(p.getName(), "list", p.getContainedProperty());
		if (p.isMultilingual())
			tmp.setMultilingual(true);
		generatePropertySetterMethod(tmp);
	}

	private void generateBuilderListPropertySetterMethods(MetaDocument doc, MetaListProperty p){
		MetaProperty tmp = new MetaGenericProperty(p.getName(), "list", p.getContainedProperty());
		generateBuilderPropertySetterMethod(doc, tmp);
	} 

	private void generateTablePropertySetterMethods(MetaTableProperty p){
		List<MetaProperty> columns = p.getColumns();
		for (int t=0; t<columns.size(); t++)
			generatePropertySetterMethod(columns.get(t));
	}

	private void generateBuilderTablePropertySetterMethods(MetaDocument doc, MetaTableProperty p){
		List<MetaProperty> columns = p.getColumns();
		for (int t=0; t<columns.size(); t++)
			generateBuilderPropertySetterMethod(doc, columns.get(t));
	}
	
	public static final String getDocumentImport(Context context, MetaDocument doc){
		return context.getDataPackageName(doc)+"."+doc.getName();
	}
	
	public static final String getXMLHelperImport(Context context, MetaDocument doc){
		return context.getDataPackageName(doc)+"."+getXMLHelperName(doc);
	}

	private void generateAdditionalMethods(MetaDocument doc){
		List <MetaProperty>properties = doc.getProperties();
		for (MetaProperty p : properties){
			if (p instanceof MetaContainerProperty)
				generateContainerMethods((MetaContainerProperty)p);
			if (p instanceof MetaTableProperty)
				generateTableMethods((MetaTableProperty)p);
			if (p instanceof MetaListProperty)
				generateListMethods((MetaListProperty)p);
		}
	}
	
	private void generateContainerMethods(MetaContainerProperty container){
		if (container.isMultilingual())
			generateContainerMethodsMultilingual(container);
		appendComment("Returns the number of elements in the \""+container.getName()+"\" container");
		appendString("public int "+getContainerSizeGetterName(container)+"();");
		emptyline();
		
	}
	
	private void generateContainerMethodsMultilingual(MetaContainerProperty container){
		for (String l : context.getLanguages()){
			appendComment("Returns the number of elements in the \""+container.getName()+"\" container");
			appendString("public int "+getContainerSizeGetterName(container, l)+"();");
			emptyline();
		}
	}

	private void generateListMethods(MetaListProperty list){
		if (list.isMultilingual())
			generateListMethodsMultilingual(list);
		
		MetaProperty c = list.getContainedProperty();
		appendComment("Adds a new element to the list.");
		String decl = "public void "+getContainerEntryAdderName(list)+"(";
		decl += c.toJavaType()+" "+c.getName();
		decl += ");";
		appendString(decl);
		emptyline();
		
		appendComment("Removes the element at position index from the list.");
		appendString("public void "+getContainerEntryDeleterName(list)+"(int index);");
		emptyline();
		
		appendComment("Swaps elements at positions index1 and index2 in the list.");
		appendString("public void "+getContainerEntrySwapperName(list)+"(int index1, int index2);");
		emptyline();
		
		appendComment("Returns the element at the position index in the list.");
		appendString("public "+c.toJavaType()+ " "+getListElementGetterName(list)+"(int index);");
		emptyline();
	}
	
	private void generateListMethodsMultilingual(MetaListProperty list){
		for (String l : context.getLanguages()){
		
			MetaProperty c = list.getContainedProperty();
			appendComment("Adds a new element to the list.");
			String decl = "public void "+getContainerEntryAdderName(list, l)+"(";
			decl += c.toJavaType()+" "+c.getName();
			decl += ");";
			appendString(decl);
			emptyline();
			
			appendComment("Removes the element at position index from the list.");
			appendString("public void "+getContainerEntryDeleterName(list, l)+"(int index);");
			emptyline();
			
			appendComment("Swaps elements at positions index1 and index2 in the list.");
			appendString("public void "+getContainerEntrySwapperName(list, l)+"(int index1, int index2);");
			emptyline();
			
			appendComment("Returns the element at the position index in the list.");
			appendString("public "+c.toJavaType()+ " "+getListElementGetterName(list, l)+"(int index);");
			emptyline();
		}
	}

	private void generateTableMethods(MetaTableProperty table){
		List<MetaProperty> columns = table.getColumns();
		
		String decl = "public void "+getContainerEntryAdderName(table)+"(";
		for (int i =0; i<columns.size(); i++){
			MetaProperty p = columns.get(i);
			decl += "String "+table.extractSubName(p);
			if (i<columns.size()-1)
				decl += ", ";
		}
		decl += ");";
		appendString(decl);
		emptyline();
		
		appendString("public void "+getContainerEntryDeleterName(table)+"(int index);");
		emptyline();
		
		appendString("public List<String> get"+StringUtils.capitalize(table.getName())+"Row(int index);");
		emptyline();

		appendString("public List<List<String>> "+getTableGetterName(table)+"();");
		emptyline();
	}
	
	public static String getContainerSizeGetterName(MetaContainerProperty p){
		return "get"+StringUtils.capitalize(p.getName())+"Size"; 
	}

	public static String getContainerSizeGetterName(MetaContainerProperty p, String language){
		return "get"+StringUtils.capitalize(p.getName(language))+"Size"; 
	}

	public static String getTableGetterName(MetaTableProperty p){
		return "get"+StringUtils.capitalize(p.getName())+"Table"; 
	}
	
	public static String getContainerEntryAdderName(MetaContainerProperty p){
	    return "add"+StringUtils.capitalize(p.getName())+p.getContainerEntryName();	    
	}

	public static String getContainerEntryAdderName(MetaContainerProperty p, String language){
	    return "add"+StringUtils.capitalize(p.getName(language))+p.getContainerEntryName();	    
	}
	
	public static String getContainerEntryDeleterName(MetaContainerProperty p){
		return "remove"+StringUtils.capitalize(p.getName())+p.getContainerEntryName();	    
	}

	public static String getContainerEntryDeleterName(MetaContainerProperty p, String language){
		return "remove"+StringUtils.capitalize(p.getName(language))+p.getContainerEntryName();	    
	}

	public static String getContainerEntrySwapperName(MetaContainerProperty p){
		return "swap"+StringUtils.capitalize(p.getName())+p.getContainerEntryName();	    
	}
	
	public static String getContainerEntrySwapperName(MetaContainerProperty p, String language){
		return "swap"+StringUtils.capitalize(p.getName(language))+p.getContainerEntryName();	    
	}

	public static String getListElementGetterName(MetaListProperty list){
		return "get"+StringUtils.capitalize(list.getName())+list.getContainerEntryName();
	}

	public static String getListElementGetterName(MetaListProperty list, String language){
		return "get"+StringUtils.capitalize(list.getName(language))+list.getContainerEntryName();
	}

	public static String getDocumentFactoryName(MetaDocument doc){
		return doc.getName()+"Factory";
	}

	public static final String getDocumentFactoryImport(Context context, MetaDocument doc){
		return context.getDataPackageName(doc)+"."+getDocumentFactoryName(doc);
	}
	
	public String getDataObjectImplName(MetaDocument doc){
		throw new AssertionError("Shouln't be called, since the facade has no impl");
	}




}
