package net.anotheria.asg.generator.model.docs;

import net.anotheria.anodoc.util.mapper.ObjectMapperUtil;
import net.anotheria.asg.generator.CommentGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaLink;
import net.anotheria.asg.generator.meta.MetaListProperty;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.model.AbstractServiceGenerator;
import net.anotheria.asg.generator.model.DataFacadeGenerator;
import net.anotheria.asg.generator.model.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>CMSBasedServiceGenerator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class CMSBasedServiceGenerator extends AbstractServiceGenerator implements IGenerator{

	/** {@inheritDoc} */
	public List<FileEntry> generate(IGenerateable gmodule){

		MetaModule mod = (MetaModule)gmodule;
		List<FileEntry> ret = new ArrayList<FileEntry>();
		ret.add(new FileEntry(generateFactory(mod)));
		ret.add(new FileEntry(generateImplementation(mod)));
		//ret.addAll(generateCRUDServices(mod)); 2019-09-25 removed crud service generation, it is not used anyway and was just generating spam.

		return ret;
	}

	private List<FileEntry> generateCRUDServices(MetaModule module){
		List<FileEntry> ret = new ArrayList<FileEntry>();

		for (MetaDocument doc : module.getDocuments())
			ret.add(new FileEntry(generateCRUDService(module, doc)));

		return ret;
	}

	private String getCRUDServiceName(MetaDocument doc){
		return doc.getName()+"CRUDServiceImpl";
	}

	private GeneratedClass generateCRUDService(MetaModule module, MetaDocument doc){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);

		clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getCRUDServiceName(doc),"The implementation of the "+getCRUDServiceName(doc)+"."));
		clazz.setPackageName(getPackageName(module));

		clazz.addImport("net.anotheria.asg.service.CRUDService");
		clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));
		clazz.addImport("net.anotheria.asg.exception.ASGRuntimeException");

		clazz.setName(getCRUDServiceName(doc));
		clazz.addInterface("CRUDService<"+doc.getName()+">");

		startClassBody();
		appendGenerationPoint("generateCRUDService");
	    appendStatement(getInterfaceName(module)+" service");
	    emptyline();
	    appendString("public ", getCRUDServiceName(doc), "(){");
	    increaseIdent();
	    appendStatement("this("+getFactoryName(module)+".getDefaultInstance())");
	    closeBlockNEW();

	    emptyline();
	    appendString("public ", getCRUDServiceName(doc), "("+getInterfaceName(module)+" aService){");
	    increaseIdent();
	    appendStatement("service = aService");
	    closeBlockNEW();

	    emptyline();
	    appendString("public "+doc.getName()+" create("+doc.getName()+" "+doc.getVariableName()+")  throws ASGRuntimeException {");
	    increaseIdent();
		appendStatement("return service.create"+doc.getName()+"(", doc.getVariableName(), ")");
		closeBlockNEW();
	    emptyline();

		appendString("public void delete(", doc.getName(), " ", doc.getVariableName(), ") throws ASGRuntimeException {");
	    increaseIdent();
	    appendStatement("service.delete",doc.getName(),"(",doc.getVariableName(),")");
		closeBlockNEW();
	    emptyline();

		appendString("public "+doc.getName()+" get(String id) throws ASGRuntimeException {");
	    increaseIdent();
	    appendStatement("return service.get",doc.getName(),"(id)");
		closeBlockNEW();
	    emptyline();

		appendString("public ", doc.getName(), " update(", doc.getName(), " ", doc.getVariableName(), ") throws ASGRuntimeException {");
	    increaseIdent();
	    appendStatement("return service.update",doc.getName(),"(",doc.getVariableName(),")");
		closeBlockNEW();
	    emptyline();


		return clazz;
	}

	private GeneratedClass generateImplementation(MetaModule module){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);

		clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getImplementationName(module),"The implementation of the "+getInterfaceName(module)+"."));
		clazz.setPackageName(getPackageName(module));

		clazz.addImport("java.nio.charset.Charset");
		clazz.addImport("java.util.List");
		clazz.addImport("java.util.ArrayList");
		clazz.addImport("java.util.Set");
		clazz.addImport("net.anotheria.anodoc.data.Module");
		clazz.addImport("net.anotheria.anodoc.data.Property");
		clazz.addImport("net.anotheria.anodoc.data.NoSuchPropertyException");
		clazz.addImport("net.anotheria.util.sorter.SortType");
		clazz.addImport("net.anotheria.util.sorter.StaticQuickSorter");
		clazz.addImport("net.anotheria.util.slicer.Segment");
		clazz.addImport("net.anotheria.util.slicer.Slicer");

		Context context = GeneratorDataRegistry.getInstance().getContext();
		clazz.addImport(context.getPackageName(module)+".data."+ module.getModuleClassName());
		clazz.addImport(context.getServicePackageName(MetaModule.SHARED)+".BasicCMSService");

		clazz.addImport("net.anotheria.anodoc.query2.DocumentQuery");
		clazz.addImport("net.anotheria.anodoc.query2.QueryResult");
		clazz.addImport("net.anotheria.anodoc.query2.QueryResultEntry");
		clazz.addImport("net.anotheria.anodoc.query2.QueryProperty");

		clazz.addImport("net.anotheria.util.StringUtils");
		clazz.addImport("net.anotheria.util.xml.XMLNode");
		clazz.addImport("net.anotheria.util.xml.XMLAttribute");

		clazz.addImport("net.anotheria.asg.util.listener.IModuleListener");

		clazz.addImport("org.codehaus.jettison.json.JSONObject");
		clazz.addImport("org.codehaus.jettison.json.JSONArray");
		clazz.addImport("org.codehaus.jettison.json.JSONException");
		clazz.addImport("com.fasterxml.jackson.core.JsonProcessingException");
		clazz.addImport("java.io.IOException");
		clazz.addImport(ObjectMapperUtil.class);
		clazz.addImport("net.anotheria.anosite.gen.shared.util.DocumentName");
				
	    clazz.setName(getImplementationName(module));
	    clazz.setParent("BasicCMSService");
	    clazz.addInterface(getInterfaceName(module));
	    clazz.addInterface("IModuleListener");

	    startClassBody();

	    appendGenerationPoint("generateImplementation");
	    
	    appendStatement("private static "+getImplementationName(module)+" instance");
	    emptyline();

	    appendString("private "+getImplementationName(module)+"(){");
	    increaseIdent();
	    if (module.getListeners().size()>0){
	    	for (int i=0; i<module.getListeners().size(); i++){
	    		String listClassName = module.getListeners().get(i);
	    		appendStatement("addServiceListener(new "+listClassName+"())");
	    	}
	    }
		appendStatement("addModuleListener("+module.getModuleClassName()+".MODULE_ID, this)");
	    closeBlockNEW();
	    emptyline();

	    appendString("static final "+getImplementationName(module)+" getInstance(){");
	    increaseIdent();
	    appendString("if (instance==null){");
	    increaseIdent();
	    appendStatement("instance = new "+getImplementationName(module)+"()");
	    closeBlockNEW();
	    appendStatement("return instance");
	    closeBlockNEW();
	    emptyline();

	    //generate module handling.
	    appendString("private "+module.getModuleClassName()+" "+getModuleGetterCall(module)+"{");
	    increaseIdent();
	    appendStatement("return ("+module.getModuleClassName()+") getModule("+module.getModuleClassName()+".MODULE_ID)");
	    closeBlockNEW();
	    emptyline();

		//implementing of IModuleListener
		appendString("@Override");
	    appendString("public void moduleLoaded(Module module){");
	    increaseIdent();
	    appendStatement("firePersistenceChangedEvent()");
	    closeBlockNEW();
	    emptyline();

	    boolean containsAnyMultilingualDocs = false;
	    List<MetaDocument> docs = module.getDocuments();

	    for (int i=0; i<docs.size(); i++){
	        MetaDocument doc = docs.get(i);

	        clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));
	        clazz.addImport(DataFacadeGenerator.getXMLHelperImport(context, doc));
	        clazz.addImport(DocumentGenerator.getDocumentImport(context, doc));

	        String listDecl = "List<"+doc.getName()+">";

			appendString("@Override");
	        appendString("public "+listDecl+" get"+doc.getMultiple()+"(){");
	        increaseIdent();
	        appendStatement(listDecl+" "+doc.getMultiple().toLowerCase()+" = new ArrayList<>()");
            appendStatement(doc.getMultiple().toLowerCase()+".addAll("+getModuleGetterCall(module)+".get"+doc.getMultiple()+"())");
	        appendStatement("return "+doc.getMultiple().toLowerCase());
	        closeBlockNEW();
	        emptyline();

			appendString("@Override");
			appendString("public "+listDecl+" get"+doc.getMultiple()+"(SortType sortType){");
			increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"(), sortType)");
			closeBlockNEW();
			emptyline();

			appendComment("Returns the "+doc.getName()+" objects with the specified ids.");
			appendString("public "+listDecl+" get"+doc.getMultiple()+"(List<String> ids){");
	        increaseIdent();
	        appendString("if (ids==null || ids.size()==0)");
	        appendIncreasedStatement("return new ArrayList<>(0)");
	        appendStatement(listDecl, " all = get",doc.getMultiple(), "()");
	        appendStatement(listDecl, " ret = new ArrayList<>", "()");
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : all){");
	        increaseIdent();
	        appendString("if(ids.contains("+doc.getVariableName()+".getId())){");
	        increaseIdent();
	        appendStatement("ret.add("+doc.getVariableName()+")");
	        closeBlockNEW();
	        closeBlockNEW();
	        appendStatement("return ret");
			closeBlockNEW();
			emptyline();

	        appendComment("Returns the "+doc.getName()+" objects with the specified ids, sorted by given sorttype.");
			appendString("public "+listDecl+" get"+doc.getMultiple()+"(List<String> ids, SortType sortType){");
	        increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"(ids), sortType)");
			closeBlockNEW();
	        emptyline();


			appendString("@Override");
	        appendString("public void delete"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+"){");
	        increaseIdent();
	        appendStatement("delete"+doc.getName()+"("+doc.getVariableName()+".getId())");
            appendString("if (hasServiceListeners()){");
	        increaseIdent();
            appendStatement("fireObjectDeletedEvent("+doc.getVariableName()+")");
	        closeBlockNEW();
	        closeBlockNEW();
	        emptyline();

			appendString("@Override");
	        appendString("public void delete"+doc.getName()+"(String id){");
	        increaseIdent();
            appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
            appendStatement(doc.getName()+" varValue = hasServiceListeners()?module.get"+doc.getName()+"(id):null");
	        appendStatement("module.delete"+doc.getName()+"(id)");
	        appendStatement("updateModule(module)");
            appendString("if(varValue!=null){");
            increaseIdent();
            appendStatement("fireObjectDeletedEvent(varValue)");
	        closeBlockNEW();
	        closeBlockNEW();
	        emptyline();


	        //deletemultiple
			appendString("@Override");
	        appendString("public void delete"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();

	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));

	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendStatement("module.delete"+doc.getName()+"("+doc.getVariableName()+".getId())");
	        closeBlockNEW();
	        appendStatement("updateModule(module)");

	        appendString("if (hasServiceListeners()){");
	        increaseIdent();
	        appendString("for (int t=0; t<list.size(); t++)");
	        appendIncreasedStatement("fireObjectDeletedEvent(list.get(t))");
	        closeBlockNEW();

	        closeBlockNEW();
	        emptyline();

			appendString("@Override");
	        appendString("public "+doc.getName()+" get"+doc.getName()+"(String id){");
	        increaseIdent();
	        appendStatement("return "+getModuleGetterCall(module)+".get"+doc.getName()+"(id)");
	        closeBlockNEW();
	        emptyline();

	        //import
			appendString("@Override");
	        appendString("public "+doc.getName()+" import"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+"){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
	        appendStatement("module.import"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        appendStatement("updateModule(module)");
            appendString("if (hasServiceListeners()){");
            increaseIdent();
            appendStatement("fireObjectImportedEvent("+doc.getVariableName()+")");
            closeBlockNEW();
	        appendStatement("return "+doc.getVariableName());
	        closeBlockNEW();
	        emptyline();

            //importList
			appendString("@Override");
	        appendString("public "+listDecl+" import"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
            appendStatement(listDecl+" ret = new ArrayList<>()");
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendStatement(doc.getName()+" imported = module.import"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
            appendStatement("ret.add(imported)");
	        closeBlockNEW();
	        appendStatement("updateModule(module)");
            appendString("if (hasServiceListeners()){");
            increaseIdent();
            appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : ret)");
            increaseIdent();
            appendStatement("fireObjectImportedEvent("+doc.getVariableName()+")");
            decreaseIdent();
            closeBlockNEW();
	        appendStatement("return ret");
	        closeBlockNEW();
	        emptyline();

	        //create
			appendString("@Override");
	        appendString("public "+doc.getName()+" create"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+"){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
	        appendStatement("module.create"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        appendStatement("updateModule(module)");
	        appendStatement("fireObjectCreatedEvent("+doc.getVariableName()+")");
	        appendStatement("return "+doc.getVariableName());
	        closeBlockNEW();
	        emptyline();



	        //create multiple
			appendString("@Override");
	        appendComment("Creates multiple new "+doc.getName()+" objects.\nReturns the created versions.");
			appendString("public "+listDecl+" create"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
	        appendStatement(listDecl+" ret = new ArrayList<>()");
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendStatement(doc.getName()+" created = module.create"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        appendStatement("ret.add(created)");
	        closeBlockNEW();

	        appendStatement("updateModule(module)");

	        appendString("if (hasServiceListeners()){");
	        increaseIdent();
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : ret)");
	        appendIncreasedStatement("fireObjectCreatedEvent("+doc.getVariableName()+")");
	        closeBlockNEW();

	        appendStatement("return ret");
	        closeBlockNEW();
	        emptyline();


			appendString("@Override");
	        appendString("public ",doc.getName()," update",doc.getName(),"(",doc.getName()," ",doc.getVariableName(),"){");
	        increaseIdent();
	        appendStatement(doc.getName()+" oldVersion = null");
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));

	        appendString("if (hasServiceListeners())");
	        appendIncreasedStatement("oldVersion = module.get"+doc.getName()+"("+doc.getVariableName()+".getId())");

	        appendStatement("module.update"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        appendStatement("updateModule(module)");

	        appendString("if (oldVersion != null){");
	        increaseIdent();
	        appendStatement("fireObjectUpdatedEvent(oldVersion, "+doc.getVariableName()+")");
	        closeBlockNEW();

	        appendStatement("return "+doc.getVariableName());
	        closeBlockNEW();
	        emptyline();


	        //updatemultiple
			appendString("@Override");
	        appendString("public "+listDecl+" update"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();
	        appendStatement(listDecl+" oldList = null");
	        appendString("if (hasServiceListeners())");
	        appendIncreasedStatement("oldList = new ArrayList<>(list.size())");

	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));

	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendString("if (oldList!=null)");
	        appendIncreasedStatement("oldList.add(module.get"+doc.getName()+"("+doc.getVariableName()+".getId()))");
	        appendStatement("module.update"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        closeBlockNEW();
	        appendStatement("updateModule(module)");

	        appendString("if (oldList!=null){");
	        increaseIdent();
	        appendString("for (int t=0; t<list.size(); t++)");
	        appendIncreasedStatement("fireObjectUpdatedEvent(oldList.get(t), list.get(t))");
	        closeBlockNEW();

	        appendStatement("return list");
	        closeBlockNEW();
	        emptyline();



			appendString("@Override");
	        appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value){");
	        increaseIdent();
	        appendStatement(listDecl+" all"+doc.getMultiple()+" = get"+doc.getMultiple()+"()");
	        appendStatement(listDecl+" ret = new ArrayList<>()");
	        appendString("for (int i=0; i<all"+doc.getMultiple()+".size(); i++){");
	        increaseIdent();
	        appendStatement(doc.getName()+" "+doc.getVariableName()+" = all"+doc.getMultiple()+".get(i)");
	        appendString("try{");
	        increaseIdent();
	        appendStatement("Property property = (("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+").getProperty(propertyName)");
			appendString("if (property.getValue()==null && value==null){");
	        appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
	        appendString("}else{");
	        increaseIdent();
	        appendString("if (value!=null && property.getValue().equals(value))");
	        appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
	        closeBlockNEW();
	        decreaseIdent();
			appendString("}catch(NoSuchPropertyException nspe){");
			increaseIdent();
			appendString("if (value==null)");
			appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
			decreaseIdent();
	        appendString("}catch(Exception ignored){}");

	        closeBlockNEW();
	        appendString("return ret;");
	        closeBlockNEW();
	        emptyline();

			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value, SortType sortType){");
			increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"ByProperty(propertyName, value), sortType)");
			closeBlockNEW();

			appendComment("Executes a query on "+doc.getMultiple());
			appendString("public QueryResult executeQueryOn"+doc.getMultiple()+"(DocumentQuery query){");
			increaseIdent();
			appendStatement(listDecl+" all"+doc.getMultiple()+" = get"+doc.getMultiple()+"()");
			appendStatement("QueryResult result = new QueryResult()");
			appendString("for (int i=0; i<all"+doc.getMultiple()+".size(); i++){");
			increaseIdent();
			appendStatement("List<QueryResultEntry> partialResult = query.match(all"+doc.getMultiple()+".get(i))");
			appendStatement("result.add(partialResult)");
			closeBlockNEW();

			appendStatement("return result");
			closeBlockNEW();
			emptyline();

			appendComment("Returns all "+doc.getName()+" objects, where property matches.");
//	        appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(QueryProperty... property){");
//	        increaseIdent();
//	        appendStatement("throw new RuntimeException(\"Not yet implemented\")");
//	        closeBlockNEW();
//	        emptyline();
//	        
//			appendComment("Returns all "+doc.getName()+" objects, where property matches, sorted");
//			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(SortType sortType, QueryProperty... property){");
//	        increaseIdent();
//	        appendStatement("throw new RuntimeException(\"Not yet implemented\")");
//	        closeBlockNEW();
//			emptyline();

			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(QueryProperty... property){");
			increaseIdent();
			appendString("//first the slow version, the fast version is a todo.");
			appendStatement(listDecl+" ret = new ArrayList<>()");
			appendStatement(listDecl+" src = get"+doc.getMultiple()+"()");
			appendString("for ( "+doc.getName()+" "+doc.getVariableName() +" : src){");
			increaseIdent();
			appendStatement("boolean mayPass = true");
			appendString("for (QueryProperty qp : property){");
			increaseIdent();
			appendStatement("mayPass = mayPass && qp.doesMatch("+doc.getVariableName()+".getPropertyValue(qp.getName()))");
			closeBlockNEW();

			appendString("if (mayPass)");
			appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
			closeBlockNEW();

			appendStatement("return ret");
			closeBlockNEW();
	        emptyline();

			appendComment("Returns all "+doc.getName()+" objects, where property matches, sorted");
			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(SortType sortType, QueryProperty... property){");
	        increaseIdent();
	        appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"ByProperty(property), sortType)");
	        closeBlockNEW();
			emptyline();
			
			// get elements COUNT
			appendComment("Returns " + doc.getName() + " objects count.");
			appendString("public int get" + doc.getMultiple() + "Count() {");
			increaseIdent();
			appendStatement("return " + getModuleGetterCall(module) + ".get" + doc.getMultiple() + "().size()");
			closeBlockNEW();
			emptyline();
			// end get elements COUNT

			// get elements Segment
			appendComment("Returns " + doc.getName() + " objects segment.");
			appendString("public " + listDecl + " get" + doc.getMultiple() + "(Segment aSegment) {");
			increaseIdent();
			appendStatement("return Slicer.slice(aSegment, get" + doc.getMultiple() + "()).getSliceData()");
			closeBlockNEW();
			emptyline();
			// end get elements Segment

			// get elements Segment with FILTER
			appendComment("Returns " + doc.getName() + " objects segment, where property matched.");
			appendString("public " + listDecl + " get" + doc.getMultiple() + "ByProperty(Segment aSegment, QueryProperty... property) {");
			increaseIdent();
			appendStatement("int pLimit = aSegment.getElementsPerSlice()");
			appendStatement("int pOffset = aSegment.getSliceNumber() * aSegment.getElementsPerSlice() - aSegment.getElementsPerSlice()");
			appendStatement(listDecl + " ret = new ArrayList<>()");
			appendStatement(listDecl + " src = get" + doc.getMultiple() + "()");
			appendString("for (" + doc.getName() + " " + doc.getVariableName() + " : src) {");
			increaseIdent();
			appendStatement("boolean mayPass = true");
			appendString("for (QueryProperty qp : property) {");
			increaseIdent();
			appendStatement("mayPass = mayPass && qp.doesMatch(" + doc.getVariableName() + ".getPropertyValue(qp.getName()))");
			closeBlockNEW();
			appendString("if (mayPass)");
			appendIncreasedStatement("ret.add(" + doc.getVariableName() + ")");
			appendString("if (ret.size() > pOffset + pLimit)");			
			appendIncreasedStatement("break");
			closeBlockNEW();
			appendStatement("return Slicer.slice(aSegment, ret).getSliceData()");
			closeBlockNEW();
			emptyline();
			// end get elements Segment with FILTER
			
			// get elements Segment with SORTING, FILTER
			appendComment("Returns " + doc.getName() + " objects segment, where property matched, sorted.");
			appendString("public " + listDecl + " get" + doc.getMultiple()
					+ "ByProperty(Segment aSegment, SortType aSortType, QueryProperty... aProperty){");
			increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get" + doc.getMultiple() + "ByProperty(aSegment, aProperty), aSortType)");
			closeBlockNEW();
			emptyline();
			// end get elements Segment with SORTING, FILTER

			String throwsClause = " throws "+ ServiceGenerator.getExceptionName(module)+" ";
			//start fetch document with dependencies
			appendString("@Override");
			appendString("public void fetch" + doc.getName() + "(final String id, Set<String> addedDocuments, JSONArray data)" + throwsClause + "{");
			increaseIdent();
			appendString("if (id.isEmpty() || addedDocuments.contains(\"" + doc.getName() + "\" + id))");
			increaseIdent();
			appendStatement("return");
			decreaseIdent();
			emptyline();
			openTry();
			appendStatement("final " + doc.getName() + "Document " + doc.getVariableName() + " = " + getModuleGetterCall(module)+".get"+doc.getName()+"(id)");
			appendStatement("addedDocuments.add(\"" + doc.getName() + "\" + id)");
			emptyline();
			Set<MetaModule> metaModules = new HashSet<>();
			for (MetaProperty property: doc.getLinks()) {
				if (property.isLinked()) {
					MetaLink link = (MetaLink) property;
					MetaModule targetModule = link.getLinkTarget().indexOf('.') == -1 ? doc.getParentModule() : GeneratorDataRegistry.getInstance().getModule(link.getTargetModuleName());
					if (targetModule == null) {
						throw new RuntimeException("Can`t resolve link: " + property + " in document " + doc.getName());
					}
					metaModules.add(targetModule);

					appendString("if (!StringUtils.isEmpty(" + doc.getVariableName() + ".get" + link.getAccesserName() + "()))");
					increaseIdent();
					appendStatement("get"+ targetModule.getName() + "Service().fetch" + link.getTargetDocumentName() +
							"(" + doc.getVariableName() + ".get" + link.getAccesserName() + "(), addedDocuments, data)");
					decreaseIdent();
				}
			}
			emptyline();
			for (MetaProperty property: doc.getProperties()) {
				if (property instanceof MetaListProperty) {
					MetaListProperty listProperty = (MetaListProperty) property;
					if (listProperty.getContainedProperty().isLinked()) {
						MetaLink link = (MetaLink) listProperty.getContainedProperty();
						MetaModule targetModule = link.getLinkTarget().indexOf('.') == -1 ? doc.getParentModule() : GeneratorDataRegistry.getInstance().getModule(link.getTargetModuleName());
						if (targetModule == null) {
							throw new RuntimeException("Can`t resolve link: " + property + " in document " + doc.getName());
						}
						metaModules.add(targetModule);
						MetaDocument targetDocument = targetModule.getDocumentByName(link.getTargetDocumentName());

						appendString("if (!" + doc.getVariableName() + ".get" + listProperty.getAccesserName() +"().isEmpty()) {");
						increaseIdent();
						appendString("for (String a" + listProperty.getAccesserName() +"Id: " + doc.getVariableName() + ".get" + listProperty.getAccesserName() + "()) {");
						increaseIdent();
						appendStatement("get" + targetModule.getName() + "Service().fetch" + targetDocument.getName() + "(a" + listProperty.getAccesserName() +"Id, addedDocuments, data)");
						closeBlockNEW();
						closeBlockNEW();
					}
				}
			}
			emptyline();
			appendStatement("JSONObject dataObject = new JSONObject()");
			appendStatement("String jsonObject = ObjectMapperUtil.getMapperInstance().writeValueAsString(" + doc.getVariableName() + ")");
			appendStatement("dataObject.put(\"object\", jsonObject)");
			appendStatement("dataObject.put(\"service\", \"" + module.getName() + "\")");
			appendStatement("dataObject.put(\"document\", \"" + module.getName() + "_" + doc.getName() + "\")");
			emptyline();
			appendStatement("data.put(dataObject)");
			emptyline();

			for (MetaProperty p: doc.getProperties()) {
				if (p.getType() == MetaProperty.Type.IMAGE || (p.getType() == MetaProperty.Type.LIST && ((MetaListProperty) p).getContainedProperty().getType() == MetaProperty.Type.IMAGE)) {
					clazz.addImport("net.anotheria.webutils.filehandling.actions.FileStorage");
					clazz.addImport("java.io.File");
					clazz.addImport("java.io.FileNotFoundException");
					clazz.addImport("java.io.IOException");
					clazz.addImport("javax.ws.rs.client.Client");
					clazz.addImport("javax.ws.rs.client.Entity");
					clazz.addImport("javax.ws.rs.client.WebTarget");
					clazz.addImport("javax.ws.rs.core.MediaType");
					clazz.addImport("javax.ws.rs.core.Response");
					clazz.addImport("net.anotheria.anosite.util.staticutil.JerseyClientUtil");
					clazz.addImport("net.anotheria.anosite.config.DocumentTransferConfig");
					clazz.addImport("org.glassfish.jersey.media.multipart.FormDataMultiPart");
					clazz.addImport("org.glassfish.jersey.media.multipart.file.FileDataBodyPart");


					appendStatement("File imageFile = FileStorage.getFile(" + doc.getVariableName()  + "." + p.toBeanGetter() + "())");
					appendStatement("Client client = JerseyClientUtil.getClientInstance()");
					appendString("for (String domain :DocumentTransferConfig.getInstance().getDomains()) {");
					increaseIdent();

					appendStatement("final FileDataBodyPart filePart = new FileDataBodyPart(\"file\", imageFile)");
					appendStatement("FormDataMultiPart formDataMultiPart = new FormDataMultiPart()");
					appendStatement("final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart)");
					appendStatement("final WebTarget target = client.target(domain + \"/api/asgimage/upload\")");
					appendStatement("final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()))");
					appendStatement("String responseResult = response.readEntity(String.class)");
					appendStatement("log.info(responseResult)");
					appendStatement("formDataMultiPart.close()");
					appendStatement("multipart.close()");
					closeBlockNEW();

					appendCatch("FileNotFoundException");
					appendStatement("throw new " + ServiceGenerator.getExceptionName(doc.getParentModule()) + " (\"Problem with getting image file for " + doc.getName() + "\" + e.getMessage())");
				}
			}

			boolean attachedDocumentExists = metaModules.size() != 0;
			if(attachedDocumentExists) {
				Iterator<MetaModule> moduleIteratorFetch = metaModules.iterator();
				while (moduleIteratorFetch.hasNext()) {
					MetaModule metaModule = moduleIteratorFetch.next();
					clazz.addImport(ServiceGenerator.getExceptionImport(metaModule));
					appendCatch(ServiceGenerator.getExceptionName(metaModule));
					appendStatement("throw new " + ServiceGenerator.getExceptionName(module) + "(\"Problem with getting document from " + metaModule.getName() + "\" + e.getMessage())");
				}
			}
			appendCatch("IOException");
			appendStatement("throw new " + ServiceGenerator.getExceptionName(doc.getParentModule()) + " (\"Problem with fetching data for this " + doc.getName() + " instance object:\" + e.getMessage())");
			appendCatch("JSONException");
			appendStatement("throw new " + ServiceGenerator.getExceptionName(doc.getParentModule()) + " (\"Problem with fetching data for this " + doc.getName() + " instance in json :\" + e.getMessage())");


			closeBlockNEW();
			closeBlockNEW();
			emptyline();
			//end fetch document with dependencies

			//start parse document with dependencies
			appendString("private void saveTransferred" + doc.getName() + "(final JSONObject data)" + throwsClause + "{");
			increaseIdent();
			openTry();
			appendStatement("String objectData = data.getString(\"object\")");
			appendStatement(doc.getName() + " " + doc.getVariableName() + " = ObjectMapperUtil.getMapperInstance().readValue(objectData.getBytes(Charset.forName(\"UTF-8\")), " + doc.getName() + "Document.class)");
			emptyline();
			openTry();
			appendStatement("update" + doc.getName() + "(" + doc.getVariableName() + ")");
			appendCatch("Exception");
			appendStatement("import" + doc.getName() + "(" + doc.getVariableName() + ")");
			closeBlockNEW();
			appendCatch("JSONException");
			appendStatement("throw new " + ServiceGenerator.getExceptionName(doc.getParentModule()) + "(\"Problem with getting data from json " + doc.getName() + " instance :\" + e.getMessage())");
			appendCatch("IOException");
			appendStatement("throw new " + ServiceGenerator.getExceptionName(doc.getParentModule()) + "(\"Problem with parsing data for this " + doc.getName() + " instance :\" + e.getMessage())");
			closeBlockNEW();
			closeBlockNEW();
			emptyline();

			if (GeneratorDataRegistry.hasLanguageCopyMethods(doc)){
				containsAnyMultilingualDocs = true;
				appendCommentLine("This method is not very fast, since it makes an update (eff. save) after each doc.");
				appendString("public void copyMultilingualAttributesInAll"+doc.getMultiple()+"(String sourceLanguage, String targetLanguage){");
				increaseIdent();
				appendStatement("List<"+doc.getName()+"> allDocumentsSrc = get"+doc.getMultiple()+"()");
				appendStatement("List<"+doc.getName()+"> allDocuments = new ArrayList<>(allDocumentsSrc.size())");
				appendStatement("allDocuments.addAll(allDocumentsSrc)");
				appendString("for ("+doc.getName()+" document : allDocuments){");
				increaseIdent();
				appendStatement("document.copyLANG2LANG(sourceLanguage, targetLanguage)");
//				appendStatement("update"+doc.getName()+"(document)");
				closeBlockNEW();
				appendStatement("update"+doc.getMultiple()+"(allDocuments)");
				closeBlockNEW();
				emptyline();

			}


	    }

		appendString("public void executeParsingForDocument (final DocumentName documentName, final JSONObject data) throws " + ServiceGenerator.getExceptionName(module)+ " {");
		increaseIdent();
		appendString("switch(documentName) {");
		increaseIdent();
		for (MetaDocument doc: docs) {
			appendString("case DOCUMENT_" + module.getName().toUpperCase() +"_" + doc.getName().toUpperCase() + ":");
			increaseIdent();
			appendStatement("saveTransferred" + doc.getName() + "(data)");
			appendStatement("break");
			decreaseIdent();
		}
		appendString("default:");
		increaseIdent();
		appendStatement("log.info(\"There is no correct document: \" + documentName + \"in this service\")");
		appendStatement("throw new " + ServiceGenerator.getExceptionName(module) + "(\"No such document\")");
		decreaseIdent();
		closeBlockNEW();
		closeBlockNEW();
		emptyline();

		if (containsAnyMultilingualDocs){
			appendComment("Copies all multilingual fields from sourceLanguage to targetLanguage in all data objects (documents, vo) which are part of this module and managed by this service");
			appendString("public void copyMultilingualAttributesInAllObjects(String sourceLanguage, String targetLanguage){");
			increaseIdent();
			for (MetaDocument doc : docs){
				if (GeneratorDataRegistry.hasLanguageCopyMethods(doc))
					appendStatement("copyMultilingualAttributesInAll"+doc.getMultiple()+"(sourceLanguage, targetLanguage)");
			}
			closeBlockNEW();
			emptyline();
	    }

	    appendComment("Executes a query on all data objects (documents, vo) which are part of this module and managed by this service");
		appendString("public QueryResult executeQueryOnAllObjects(DocumentQuery query){");
		increaseIdent();
		appendStatement("QueryResult ret = new QueryResult()");
		for (MetaDocument doc : docs){
			appendStatement("ret.add(executeQueryOn"+doc.getMultiple()+"(query).getEntries())");
		}
		appendStatement("return ret");
		closeBlock("executeQueryOnAllObjects");
		emptyline();
		
	    //generate export function
	    emptyline();
	    for (MetaDocument d : docs){
			appendString("public XMLNode export"+d.getMultiple()+"ToXML(){");
	    	increaseIdent();
	    	appendStatement("XMLNode ret = new XMLNode("+quote(d.getMultiple())+")");
	    	appendStatement("List<"+d.getName()+"> list = get"+d.getMultiple()+"()");
	    	appendStatement("ret.addAttribute(new XMLAttribute("+quote("count")+", list.size()))");
	    	appendString("for ("+d.getName()+" object : list)");
	    	appendIncreasedStatement("ret.addChildNode("+DataFacadeGenerator.getXMLHelperName(d)+".toXML(object))");
	    	appendStatement("return ret");
	    	closeBlockNEW();
	    	emptyline();

			appendString("public XMLNode export"+d.getMultiple()+"ToXML(List<"+d.getName()+"> list){");
	    	increaseIdent();
	    	appendStatement("XMLNode ret = new XMLNode("+quote(d.getMultiple())+")");
	    	appendStatement("ret.addAttribute(new XMLAttribute("+quote("count")+", list.size()))");
	    	appendString("for ("+d.getName()+" object : list)");
	    	appendIncreasedStatement("ret.addChildNode("+DataFacadeGenerator.getXMLHelperName(d)+".toXML(object))");
	    	appendStatement("return ret");
	    	closeBlockNEW();
	    	emptyline();


		    if (containsAnyMultilingualDocs && GeneratorDataRegistry.getInstance().getContext().areLanguagesSupported()){
				appendString("public XMLNode export"+d.getMultiple()+"ToXML(String[] languages){");
		    	increaseIdent();
		    	appendStatement("XMLNode ret = new XMLNode("+quote(d.getMultiple())+")");
		    	appendStatement("List<"+d.getName()+"> list = get"+d.getMultiple()+"()");
		    	appendStatement("ret.addAttribute(new XMLAttribute("+quote("count")+", list.size()))");
		    	appendString("for ("+d.getName()+" object : list)");
		    	appendIncreasedStatement("ret.addChildNode("+DataFacadeGenerator.getXMLHelperName(d)+".toXML(object, languages))");
		    	appendStatement("return ret");
		    	closeBlockNEW();
		    	emptyline();

				appendString("public XMLNode export"+d.getMultiple()+"ToXML(String[] languages, List<"+d.getName()+"> list){");
		    	increaseIdent();
		    	appendStatement("XMLNode ret = new XMLNode("+quote(d.getMultiple())+")");
		    	appendStatement("ret.addAttribute(new XMLAttribute("+quote("count")+", list.size()))");
		    	appendString("for ("+d.getName()+" object : list)");
		    	appendIncreasedStatement("ret.addChildNode("+DataFacadeGenerator.getXMLHelperName(d)+".toXML(object, languages))");
		    	appendStatement("return ret");
		    	closeBlockNEW();
		    	emptyline();
		    }

	    }


	    appendString("public XMLNode exportToXML(){");
	    increaseIdent();
	    appendStatement("XMLNode ret = new XMLNode("+quote(module.getName())+")");
	    emptyline();
	    for (MetaDocument d : docs){
	    	appendStatement("ret.addChildNode(export"+d.getMultiple()+"ToXML())");
	    }
	    emptyline();
	    appendStatement("return ret");
	    closeBlockNEW();


	    if (containsAnyMultilingualDocs && GeneratorDataRegistry.getInstance().getContext().areLanguagesSupported()){
		    appendString("public XMLNode exportToXML(String[] languages){");
		    increaseIdent();
		    appendStatement("XMLNode ret = new XMLNode("+quote(module.getName())+")");
		    emptyline();
		    for (MetaDocument d : docs){
		    	appendStatement("ret.addChildNode(export"+d.getMultiple()+"ToXML(languages))");
		    }
		    emptyline();
		    appendStatement("return ret");
		    closeBlockNEW();
	    }
	    return clazz;
	}

	private String getModuleGetterMethod(MetaModule module){
	    return "_get"+module.getModuleClassName();
	}

	private String getModuleGetterCall(MetaModule module){
	    return getModuleGetterMethod(module)+"()";
	}

	/** {@inheritDoc} */
	@Override protected String getMoskitoSubsystem(){
		return super.getMoskitoSubsystem()+"-cms";
	}

}
