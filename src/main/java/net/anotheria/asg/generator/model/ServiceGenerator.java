package net.anotheria.asg.generator.model;

import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.CommentGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.TypeOfClass;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.StorageType;
import net.anotheria.asg.generator.model.db.JDBCBasedServiceGenerator;
import net.anotheria.asg.generator.model.db.JDBCPersistenceServiceGenerator;
import net.anotheria.asg.generator.model.db.PersistenceServiceDAOGenerator;
import net.anotheria.asg.generator.model.docs.CMSBasedServiceGenerator;
import net.anotheria.asg.generator.model.federation.FederationServiceGenerator;
import net.anotheria.asg.generator.model.fixture.FixtureServiceGenerator;
import net.anotheria.asg.generator.model.inmemory.InMemoryServiceGenerator;
import net.anotheria.asg.generator.model.rmi.RMIServiceGenerator;
import net.anotheria.util.ExecutionTimer;
import net.anotheria.util.sorter.SortType;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls different sub generators for generation of the service layer. Generates factories and interfaces.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServiceGenerator extends AbstractGenerator implements IGenerator{
	
	/**
	 * {@inheritDoc}
	 *
	 * Generates the service layer for a MetaModule.
	 */
	public List<FileEntry> generate(IGenerateable gmodule){
		
		MetaModule mod = (MetaModule)gmodule;
		
		List<FileEntry> ret = new ArrayList<FileEntry>();
		
		ExecutionTimer timer = new ExecutionTimer("ServiceGenerator");
		
		
		//timer.startExecution(mod.getName()+"-Interface");
		ret.add(new FileEntry(generateInterface(mod)));
		//timer.stopExecution(mod.getName()+"-Interface");
		//timer.startExecution(mod.getName()+"-Exception");
		ret.add(new FileEntry(generateException(mod)));
		List<GeneratedClass> itemNotFoundExceptions = generateItemNotFoundExceptions(mod);
		for (GeneratedClass c : itemNotFoundExceptions)
			ret.add(new FileEntry(c));
		//timer.stopExecution(mod.getName()+"-Exception");
		
		//add in memory genererator
		timer.startExecution(mod.getName()+"-InMem");
		InMemoryServiceGenerator inMemGen = new InMemoryServiceGenerator();
		ret.addAll(inMemGen.generate(gmodule));
		timer.stopExecution(mod.getName()+"-InMem");
		// - end in memory

		//add fixture generator
		timer.startExecution(mod.getName()+"-Fixture");
		FixtureServiceGenerator fixtureGen = new FixtureServiceGenerator();
		ret.addAll(fixtureGen.generate(gmodule));
		timer.stopExecution(mod.getName()+"-Fixture");
		// - end fixture

		
		
		//addrmi genererator
		timer.startExecution(mod.getName()+"-RMI");
		RMIServiceGenerator rmiGen = new RMIServiceGenerator();
		ret.addAll(rmiGen.generate(gmodule));
		timer.stopExecution(mod.getName()+"-RMI");
		// - end rmiy
		
		
		if (mod.getStorageType()==StorageType.CMS){
			timer.startExecution(mod.getName()+"-CMS");
			CMSBasedServiceGenerator cmsGen = new CMSBasedServiceGenerator();
			ret.addAll(cmsGen.generate(gmodule));
			timer.stopExecution(mod.getName()+"-CMS");
		}
		
		if (mod.getStorageType()==StorageType.DB){
			timer.startExecution(mod.getName()+"-DB");
			
			timer.startExecution(mod.getName()+"-JDBC");
			JDBCPersistenceServiceGenerator jdbcGen = new JDBCPersistenceServiceGenerator();
			ret.addAll(jdbcGen.generate(gmodule));
			timer.stopExecution(mod.getName()+"-JDBC");
			
			timer.startExecution(mod.getName()+"-DAO");
			PersistenceServiceDAOGenerator daoGen = new PersistenceServiceDAOGenerator();
			ret.addAll(daoGen.generate(gmodule));
			timer.stopExecution(mod.getName()+"-DAO");

			timer.startExecution(mod.getName()+"-JDBC-Serv");
			JDBCBasedServiceGenerator servGen = new JDBCBasedServiceGenerator();
			ret.addAll(servGen.generate(gmodule));
			timer.stopExecution(mod.getName()+"-JDBC-Serv");

			//SQLGenerator sqlGen = new SQLGenerator();
			//ret.addAll(sqlGen.generate(gmodule, context));
			timer.stopExecution(mod.getName()+"-DB");
		}
		
		if (mod.getStorageType()==StorageType.FEDERATION){
			timer.startExecution(mod.getName()+"-Fed");
			FederationServiceGenerator cmsGen = new FederationServiceGenerator();
			ret.addAll(cmsGen.generate(gmodule));
			timer.stopExecution(mod.getName()+"-Fed");
		}
		
		//timer.printExecutionTimesOrderedByCreation();

		return ret;
	}
	
	/**
	 * Returns the package name for the service generation.
	 * @param module
	 * @return
	 */
	private String getPackageName(MetaModule module){
		return GeneratorDataRegistry.getInstance().getContext().getPackageName(module)+".service";
	}
	
	
	private List<GeneratedClass> generateItemNotFoundExceptions(MetaModule module){
		ArrayList<GeneratedClass> ret = new ArrayList<GeneratedClass>();
		
		for (MetaDocument doc : module.getDocuments()){
			GeneratedClass clazz = new GeneratedClass();
			startNewJob(clazz);
			
			clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getItemNotFoundExceptionName(doc,module), this));
	 
		    clazz.setPackageName(getPackageName(module));
		    
		    clazz.setClazzComment("Exception for gets over non existing id in "+getInterfaceName(module)+", document: "+doc.getName());
		    //TODO FIXME
		    //appendString("@SuppressWarnings(" + quote("serial") + ")");
		    clazz.setName(getItemNotFoundExceptionName(doc, module));
		    clazz.setParent(getExceptionName(module));
		    
		    startClassBody();
		    appendString("public "+getItemNotFoundExceptionName(doc, module)+" (String id){" );
		    appendIncreasedStatement("super("+quote("No "+doc.getName()+" found with id: ")+"+id)");
		    appendString("}");
		    ret.add(clazz);
		}

	    return ret;
	}

	/**
	 * Generates the base exception class for a module.
	 * @param module
	 * @return
	 */
	private GeneratedClass generateException(MetaModule module){

		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
		clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getExceptionName(module), this));
 
	    clazz.setPackageName(getPackageName(module));
	    clazz.addImport(ASGRuntimeException.class);
	    
	    clazz.setClazzComment("Base class for all exceptions thrown by implementations of "+getInterfaceName(module));
	    //TODO FIXME
	    //appendString("@SuppressWarnings(" + quote("serial") + ")");
	    clazz.setName(getExceptionName(module));
	    clazz.setParent(ASGRuntimeException.class);
	    
	    startClassBody();
	    appendString("public "+getExceptionName(module)+" (String message){" );
	    appendIncreasedStatement("super(message)");
	    appendString("}");
	    emptyline();
	    
	    appendString("public "+getExceptionName(module)+" (Throwable cause){" );
	    appendIncreasedStatement("super(cause)");
	    appendString("}");
	    emptyline();
	    
	    appendString("public "+getExceptionName(module)+" (String message, Throwable cause){" );
	    appendIncreasedStatement("super(message, cause)");
	    appendString("}");

	    return clazz;
	}
	
	/**
	 * Generates the service interface for the module.
	 * @param module module to generate.
	 * @return generated clazz.
	 */
	private GeneratedClass generateInterface(MetaModule module){
	    
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		appendGenerationPoint("generateInterface");
		clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getInterfaceName(module), this));
 
	    clazz.setPackageName(getPackageName(module));
	    clazz.addImport(List.class);
	    clazz.addImport(SortType.class);
	    
	    List<MetaDocument> docs = module.getDocuments();
	    for (int i=0; i<docs.size(); i++){
	        MetaDocument doc = docs.get(i);
	        clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));
	    }
		clazz.addImport("java.util.Set");
		clazz.addImport("org.codehaus.jettison.json.JSONObject");
		clazz.addImport("org.codehaus.jettison.json.JSONArray");
		clazz.addImport("net.anotheria.anosite.gen.shared.util.DocumentName");

	    clazz.addImport(net.anotheria.util.xml.XMLNode.class);
	    clazz.addImport(net.anotheria.util.slicer.Segment.class);
	    clazz.addImport(net.anotheria.anodoc.query2.DocumentQuery.class);
	   
	    clazz.addImport(net.anotheria.anodoc.query2.QueryResult.class);
	    clazz.addImport(net.anotheria.anodoc.query2.QueryProperty.class);
	    clazz.addImport(net.anotheria.asg.service.ASGService.class);

	    clazz.setType(TypeOfClass.INTERFACE);
	    clazz.setName(getInterfaceName(module));
	    clazz.setParent("ASGService");
	    
	    boolean containsAnyMultilingualDocs = false;

	    String throwsClause = " throws "+getExceptionName(module);
	    startClassBody();
	    
	    for (int i=0; i<docs.size(); i++){
	        MetaDocument doc = docs.get(i);
	        String listDecl = "List<"+doc.getName()+">";
	        appendComment("Returns all "+doc.getMultiple()+" objects stored.");
	        appendStatement(listDecl+" get"+doc.getMultiple()+"()"+throwsClause);
	        emptyline();
			appendComment("Returns all "+doc.getMultiple()+" objects sorted by given sortType.");
			appendStatement(listDecl+" get"+doc.getMultiple()+"(SortType sortType)"+throwsClause);
			emptyline();

	        appendComment("Deletes a "+doc.getName()+" object by id.");
	        appendStatement("void delete"+doc.getName()+"(String id)"+throwsClause);
	        emptyline();
	        appendComment("Deletes a "+doc.getName()+" object.");
	        appendStatement("void delete"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+")"+throwsClause);
	        emptyline();
	        appendComment("Deletes multiple "+doc.getName()+" object.");
	        appendStatement("void delete"+doc.getMultiple()+"("+listDecl+" list)"+throwsClause);
	        emptyline();
	        appendComment("Returns the "+doc.getName()+" object with the specified id.");
	        appendStatement(doc.getName()+" get"+doc.getName()+"(String id)"+throwsClause);
	        emptyline();


	        appendComment("Imports a new "+doc.getName()+" object.\nReturns the created version.");
	        appendStatement(doc.getName()+" import"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+")"+throwsClause);
	        emptyline();

            appendComment("Imports multiple new  "+doc.getName()+" object.\nReturns the created versions.");
            appendStatement(listDecl+" import"+doc.getMultiple()+"("+listDecl+" list)"+throwsClause);
	        emptyline();
            

	        appendComment("Creates a new "+doc.getName()+" object.\nReturns the created version.");
	        appendStatement(doc.getName()+" create"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+")"+throwsClause);
	        emptyline();
	        
	        appendComment("Creates multiple new "+doc.getName()+" objects.\nReturns the created versions.");
	        appendStatement(listDecl+" create"+doc.getMultiple()+"("+listDecl+" list)"+throwsClause);
	        emptyline();

	        appendComment("Updates a "+doc.getName()+" object.\nReturns the updated version.");
	        appendStatement(doc.getName()+" update"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+")"+throwsClause);
	        emptyline();

	        appendComment("Updates multiple "+doc.getName()+" objects.\nReturns the updated versions.");
	        appendStatement(listDecl+" update"+doc.getMultiple()+"("+listDecl+" list)"+throwsClause);
	        emptyline();
	        
	        
	        //special functions
	        appendComment("Returns all "+doc.getName()+" objects, where property with given name equals object.");
	        appendStatement(listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value)"+throwsClause);
	        emptyline();
			appendComment("Returns all "+doc.getName()+" objects, where property with given name equals object, sorted.");
			appendStatement(listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value, SortType sortType)"+throwsClause);
			emptyline();
			appendComment("Executes a query.");
			appendStatement("QueryResult executeQueryOn"+doc.getMultiple()+"(DocumentQuery query)"+throwsClause);
			emptyline();
	        appendComment("Returns all "+doc.getName()+" objects, where property matches.");
	        appendStatement(listDecl+" get"+doc.getMultiple()+"ByProperty(QueryProperty... property)"+throwsClause);
	        emptyline();
			appendComment("Returns all "+doc.getName()+" objects, where property matches, sorted.");
			appendStatement(listDecl+" get"+doc.getMultiple()+"ByProperty(SortType sortType, QueryProperty... property)"+throwsClause);
			emptyline();
			
			// get elements COUNT
			appendComment("Returns all " + doc.getMultiple() + " count.");
			appendStatement("int get" + doc.getMultiple() + "Count()" + throwsClause);
			emptyline();
			// end get elements COUNT

			// get elements Segment
			appendComment("Returns " + doc.getMultiple() + " objects segment.");
			appendStatement( listDecl + " get" + doc.getMultiple() + "(Segment aSegment)" + throwsClause);
			emptyline();
			// end get elements Segment

			// get elements Segment with FILTER
			appendComment("Returns " + doc.getName() + " objects segment, where property matches.");
			appendStatement( listDecl + " get" + doc.getMultiple() + "ByProperty(Segment aSegment, QueryProperty... aProperty)"
					+ throwsClause);
			emptyline();
			// end get elements Segment with FILTER

			// get elements Segment with SORTING, FILTER
			appendComment("Returns " + doc.getName() + " objects segment, where property matches, sorted.");
			appendStatement(  listDecl + " get" + doc.getMultiple()
					+ "ByProperty(Segment aSegment, SortType aSortType, QueryProperty... aProperty)" + throwsClause);
			emptyline();
			// end get elements Segment with SORTING, FILTER

			if (GeneratorDataRegistry.hasLanguageCopyMethods(doc)){
				appendComment("In all documents of type "+doc.getName()+" copies all multilingual fields from sourceLanguage to targetLanguage.");
				appendStatement("void copyMultilingualAttributesInAll"+doc.getMultiple()+"(String sourceLanguage, String targetLanguage)"+throwsClause);
				emptyline();
				containsAnyMultilingualDocs = true;
			}

			appendComment("Creates an xml element with selected contained data.");
			appendStatement("XMLNode export"+doc.getMultiple()+"ToXML(List<"+doc.getName()+"> list"+doc.getMultiple()+") "+throwsClause);
			if (containsAnyMultilingualDocs && GeneratorDataRegistry.getInstance().getContext().areLanguagesSupported()) {
				appendComment("creates an xml element with selected contained data but only selected languages in multilingual attributes");
				appendStatement("XMLNode export"+doc.getMultiple()+"ToXML(String[] languages,List<"+doc.getName()+"> list"+doc.getMultiple()+")" + throwsClause);
	    	}

			emptyline();
			appendComment("Create json object list dependencies for this " + doc.getName() + " document.");
			appendStatement("void fetch" + doc.getName() + "(String id, Set<String> addedDocuments, JSONArray data)" + throwsClause);
			emptyline();

			//this method checks whether a document with the given document set exists or no.
	    }

		appendComment("Save transferred document by its own type.");
		appendStatement("void executeParsingForDocument (final DocumentName documentName, final JSONObject data)" + throwsClause);
	    
	    if (containsAnyMultilingualDocs){
			appendComment("Copies all multilingual fields from sourceLanguage to targetLanguage in all data objects (documents, vo) which are part of this module and managed by this service.");
			appendStatement(" void copyMultilingualAttributesInAllObjects(String sourceLanguage, String targetLanguage)"+throwsClause);
			emptyline();
	    }
	    
	    appendComment("Executes a query on all data objects (documents, vo) which are part of this module and managed by this service.");
	    appendStatement("QueryResult executeQueryOnAllObjects(DocumentQuery query)" +throwsClause);
	    
	    
		
	    appendComment("creates an xml element with all contained data.");
		appendStatement("XMLNode exportToXML()"+throwsClause);
		
		emptyline();
	    
	    if (containsAnyMultilingualDocs && GeneratorDataRegistry.getInstance().getContext().areLanguagesSupported()){
	    	appendComment("creates an xml element with all contained data but only selected languages in multilingual attributes.");
	    	appendStatement("XMLNode exportToXML(String[] languages)"+throwsClause);
	    }

	    return clazz;
	}
	
	/**
	 * Returns the name of the base exception class for the service for the given module.
	 *
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return name of exception
	 */
	public static String getExceptionName(MetaModule m){
	    return getServiceName(m)+"Exception";
	}

	/**
	 * <p>getItemNotFoundExceptionName.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getItemNotFoundExceptionName(MetaDocument doc, MetaModule m){
	    return doc.getName()+"NotFoundIn"+getExceptionName(m);
	}

	/**
	 * Returns the interface name for the service for the module.
	 *
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return name of interface
	 */
	public static String getInterfaceName(MetaModule module){
	    return "I"+getServiceName(module);
	}
	
	/**
	 * Returns the import (package and class name) for the service interface for the given module.
	 *
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return import for service
	 */
	public static String getInterfaceImport(MetaModule m){
		return getPackageName(GeneratorDataRegistry.getInstance().getContext(),m)+"."+getInterfaceName(m);
	}
	
	/**
	 * Returns the base exception name for the service for this module.
	 *
	 * @param m the module.
	 * @return exception name
	 */
	public static String getExceptionImport(MetaModule m){
		return getPackageName(GeneratorDataRegistry.getInstance().getContext(),m)+"."+getExceptionName(m);
	}
	
	/**
	 * <p>getItemNotFoundExceptionImport.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getItemNotFoundExceptionImport(MetaDocument doc, MetaModule m){
		return getPackageName(GeneratorDataRegistry.getInstance().getContext(),m)+"."+getItemNotFoundExceptionName(doc, m);
	}

	/**
	 * Returns the service name for this module.
	 *
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return service name
	 */
	public static String getServiceName(MetaModule m){
	    return m.getName()+"Service";
	}

	/**
	 * Returns the factory name for the service for a metamodule.
	 *
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return factory name
	 */
	public static String getFactoryName(MetaModule m){
	    return getServiceName(m)+"Factory";
	}
	/**
	 * Returns the import for the factory for the service for a meta-module.
	 *
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return inport for factory
	 */
	public static String getFactoryImport(MetaModule m){
	    return getPackageName(GeneratorDataRegistry.getInstance().getContext(), m)+"."+getFactoryName(m);
	}

	/**
	 * <p>getImplementationName.</p>
	 *
	 * @param m a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getImplementationName(MetaModule m){
	    return getServiceName(m)+"Impl";
	}
	
	/**
	 * <p>getPackageName.</p>
	 *
	 * @param context a {@link net.anotheria.asg.generator.Context} object.
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getPackageName(Context context, MetaModule module){
	    return context.getServicePackageName(module);
	}
}
