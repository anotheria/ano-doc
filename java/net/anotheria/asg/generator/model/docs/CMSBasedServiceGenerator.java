package net.anotheria.asg.generator.model.docs;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.CommentGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.model.AbstractServiceGenerator;
import net.anotheria.asg.generator.model.DataFacadeGenerator;

public class CMSBasedServiceGenerator extends AbstractServiceGenerator implements IGenerator{

	public List<FileEntry> generate(IGenerateable gmodule){

		MetaModule mod = (MetaModule)gmodule;
		List<FileEntry> ret = new ArrayList<FileEntry>();
		ret.add(new FileEntry(generateFactory(mod)));
		ret.add(new FileEntry(generateImplementation(mod)));
		ret.addAll(generateCRUDServices(mod));

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
	    appendStatement(getInterfaceName(module)+" service");
	    emptyline();
	    appendString("public ", getCRUDServiceName(doc), "(){");
	    increaseIdent();
	    appendStatement("this("+getFactoryName(module)+".getDefaultInstance())");
	    append(closeBlock());

	    emptyline();
	    appendString("public ", getCRUDServiceName(doc), "("+getInterfaceName(module)+" aService){");
	    increaseIdent();
	    appendStatement("service = aService");
	    append(closeBlock());

	    emptyline();
	    appendString("public "+doc.getName()+" create("+doc.getName()+" "+doc.getVariableName()+")  throws ASGRuntimeException {");
	    increaseIdent();
		appendStatement("return service.create"+doc.getName()+"(", doc.getVariableName(), ")");
		append(closeBlock());
	    emptyline();

		appendString("public void delete(", doc.getName(), " ", doc.getVariableName(), ") throws ASGRuntimeException {");
	    increaseIdent();
	    appendStatement("service.delete",doc.getName(),"(",doc.getVariableName(),")");
		append(closeBlock());
	    emptyline();

		appendString("public "+doc.getName()+" get(String id) throws ASGRuntimeException {");
	    increaseIdent();
	    appendStatement("return service.get",doc.getName(),"(id)");
		append(closeBlock());
	    emptyline();

		appendString("public ", doc.getName(), " update(", doc.getName(), " ", doc.getVariableName(), ") throws ASGRuntimeException {");
	    increaseIdent();
	    appendStatement("return service.update",doc.getName(),"(",doc.getVariableName(),")");
		append(closeBlock());
	    emptyline();


		return clazz;
	}

	private GeneratedClass generateImplementation(MetaModule module){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);

		clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getImplementationName(module),"The implementation of the "+getInterfaceName(module)+"."));
		clazz.setPackageName(getPackageName(module));


		clazz.addImport("java.util.List");
		clazz.addImport("java.util.ArrayList");
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

		clazz.addImport("net.anotheria.util.xml.XMLNode");
		clazz.addImport("net.anotheria.util.xml.XMLAttribute");

	    clazz.setName(getImplementationName(module));
	    clazz.setParent("BasicCMSService");
	    clazz.addInterface(getInterfaceName(module));

	    startClassBody();

	    appendStatement("private static "+getImplementationName(module)+" instance");
	    emptyline();

	    appendString("private "+getImplementationName(module)+"(){");
	    increaseIdent();
	    if (module.getListeners().size()>0){
	    	for (int i=0; i<module.getListeners().size(); i++){
	    		String listClassName = (String)module.getListeners().get(i);
	    		appendStatement("addServiceListener(new "+listClassName+"())");
	    	}
	    }
	    append(closeBlock());
	    emptyline();

	    appendString("static final "+getImplementationName(module)+" getInstance(){");
	    increaseIdent();
	    appendString("if (instance==null){");
	    increaseIdent();
	    appendStatement("instance = new "+getImplementationName(module)+"()");
	    append(closeBlock());
	    appendStatement("return instance");
	    append(closeBlock());
	    emptyline();

	    //generate module handling.
	    appendString("private "+module.getModuleClassName()+" "+getModuleGetterCall(module)+"{");
	    increaseIdent();
	    appendStatement("return ("+module.getModuleClassName()+") getModule("+module.getModuleClassName()+".MODULE_ID)");
	    append(closeBlock());
	    emptyline();

	    boolean containsAnyMultilingualDocs = false;
	    List<MetaDocument> docs = module.getDocuments();

	    for (int i=0; i<docs.size(); i++){
	        MetaDocument doc = docs.get(i);

	        clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));
	        clazz.addImport(DataFacadeGenerator.getXMLHelperImport(context, doc));
	        clazz.addImport(DocumentGenerator.getDocumentImport(context, doc));

	        String listDecl = "List<"+doc.getName()+">";

	        appendString("@SuppressWarnings(\"unchecked\")");
	        appendString("public "+listDecl+" get"+doc.getMultiple()+"(){");
	        increaseIdent();
	        appendStatement("List "+doc.getMultiple().toLowerCase()+" = "+getModuleGetterCall(module)+".get"+doc.getMultiple()+"()");
	        appendStatement("return "+doc.getMultiple().toLowerCase());
	        append(closeBlock());
	        emptyline();

			appendString("public "+listDecl+" get"+doc.getMultiple()+"(SortType sortType){");
			increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"(), sortType)");
			append(closeBlock());
			emptyline();

			appendComment("Returns the "+doc.getName()+" objects with the specified ids.");
	        appendStatement("public "+listDecl+" get"+doc.getMultiple()+"(List<String> ids){");
	        increaseIdent();
	        appendString("if (ids==null || ids.size()==0)");
	        appendIncreasedStatement("return new ArrayList<"+doc.getName()+">(0)");
	        appendStatement(listDecl, " all = get",doc.getMultiple(), "()");
	        appendStatement(listDecl, " ret = new ArrayList<"+doc.getName()+">", "()");
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : all){");
	        increaseIdent();
	        appendString("if(ids.contains("+doc.getVariableName()+".getId())){");
	        increaseIdent();
	        appendStatement("ret.add("+doc.getVariableName()+")");
	        append(closeBlock());
	        append(closeBlock());
	        appendStatement("return ret");
			append(closeBlock());
			emptyline();

	        appendComment("Returns the "+doc.getName()+" objects with the specified ids, sorted by given sorttype.");
	        appendStatement("public "+listDecl+" get"+doc.getMultiple()+"(List<String> ids, SortType sortType){");
	        increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"(ids), sortType)");
			append(closeBlock());
	        emptyline();


	        appendString("public void delete"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+"){");
	        increaseIdent();
	        appendStatement("delete"+doc.getName()+"("+doc.getVariableName()+".getId())");
            appendString("if (hasServiceListeners()){");
	        increaseIdent();
            appendStatement("fireObjectDeletedEvent("+doc.getVariableName()+")");
	        append(closeBlock());
	        append(closeBlock());
	        emptyline();

	        appendString("public void delete"+doc.getName()+"(String id){");
	        increaseIdent();
            appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
            appendStatement(doc.getName()+" varValue = hasServiceListeners()?module.get"+doc.getName()+"(id):null");
	        appendStatement("module.delete"+doc.getName()+"(id)");
	        appendStatement("updateModule(module)");
            appendString("if(varValue!=null){");
            increaseIdent();
            appendStatement("fireObjectDeletedEvent(varValue)");
	        append(closeBlock());
	        append(closeBlock());
	        emptyline();


	        //deletemultiple
	        appendString("public void delete"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();

	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));

	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendStatement("module.delete"+doc.getName()+"("+doc.getVariableName()+".getId())");
	        append(closeBlock());
	        appendStatement("updateModule(module)");

	        appendString("if (hasServiceListeners()){");
	        increaseIdent();
	        appendString("for (int t=0; t<list.size(); t++)");
	        appendIncreasedStatement("fireObjectDeletedEvent(list.get(t))");
	        append(closeBlock());

	        append(closeBlock());
	        emptyline();

	        appendString("public "+doc.getName()+" get"+doc.getName()+"(String id){");
	        increaseIdent();
	        appendStatement("return "+getModuleGetterCall(module)+".get"+doc.getName()+"(id)");
	        append(closeBlock());
	        emptyline();

	        //import
	        appendString("public "+doc.getName()+" import"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+"){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
	        appendStatement("module.import"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        appendStatement("updateModule(module)");
            appendString("if (hasServiceListeners()){");
            increaseIdent();
            appendStatement("fireObjectImportedEvent("+doc.getVariableName()+")");
            append(closeBlock());
	        appendStatement("return "+doc.getVariableName());
	        append(closeBlock());
	        emptyline();

            //importList
	        appendString("public "+listDecl+" import"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
            appendStatement(listDecl+" ret = new ArrayList<"+doc.getName()+">()");
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendStatement(doc.getName()+" imported = module.import"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
            appendStatement("ret.add(imported)");
	        append(closeBlock());
	        appendStatement("updateModule(module)");
            appendString("if (hasServiceListeners()){");
            increaseIdent();
            appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : ret)");
            increaseIdent();
            appendStatement("fireObjectImportedEvent("+doc.getVariableName()+")");
            decreaseIdent();
            append(closeBlock());
	        appendStatement("return ret");
	        append(closeBlock());
	        emptyline();

	        //create
	        appendString("public "+doc.getName()+" create"+doc.getName()+"("+doc.getName()+" "+doc.getVariableName()+"){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
	        appendStatement("module.create"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        appendStatement("updateModule(module)");
	        appendStatement("fireObjectCreatedEvent("+doc.getVariableName()+")");
	        appendStatement("return "+doc.getVariableName());
	        append(closeBlock());
	        emptyline();



	        //create multiple
	        appendComment("Creates multiple new "+doc.getName()+" objects.\nReturns the created versions.");
	        appendStatement("public "+listDecl+" create"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();
	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));
	        appendStatement(listDecl+" ret = new ArrayList<"+doc.getName()+">()");
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendStatement(doc.getName()+" created = module.create"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        appendStatement("ret.add(created)");
	        append(closeBlock());

	        appendStatement("updateModule(module)");

	        appendString("if (hasServiceListeners()){");
	        increaseIdent();
	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : ret)");
	        appendIncreasedStatement("fireObjectCreatedEvent("+doc.getVariableName()+")");
	        append(closeBlock());

	        appendStatement("return ret");
	        append(closeBlock());
	        emptyline();


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
	        append(closeBlock());

	        appendStatement("return "+doc.getVariableName());
	        append(closeBlock());
	        emptyline();


	        //updatemultiple
	        appendString("public "+listDecl+" update"+doc.getMultiple()+"("+listDecl+" list){");
	        increaseIdent();
	        appendStatement(listDecl+" oldList = null");
	        appendString("if (hasServiceListeners())");
	        appendIncreasedStatement("oldList = new ArrayList<"+doc.getName()+">(list.size())");

	        appendStatement(module.getModuleClassName()+" module = "+getModuleGetterCall(module));

	        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
	        increaseIdent();
	        appendString("if (oldList!=null)");
	        appendIncreasedStatement("oldList.add(module.get"+doc.getName()+"("+doc.getVariableName()+".getId()))");
	        appendStatement("module.update"+doc.getName()+"(("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+")");
	        append(closeBlock());
	        appendStatement("updateModule(module)");

	        appendString("if (oldList!=null){");
	        increaseIdent();
	        appendString("for (int t=0; t<list.size(); t++)");
	        appendIncreasedStatement("fireObjectUpdatedEvent(oldList.get(t), list.get(t))");
	        append(closeBlock());

	        appendStatement("return list");
	        append(closeBlock());
	        emptyline();



	        appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value){");
	        increaseIdent();
	        appendStatement(listDecl+" all"+doc.getMultiple()+" = get"+doc.getMultiple()+"()");
	        appendStatement(listDecl+" ret = new ArrayList<"+doc.getName()+">()");
	        appendString("for (int i=0; i<all"+doc.getMultiple()+".size(); i++){");
	        increaseIdent();
	        appendStatement(doc.getName()+" "+doc.getVariableName()+" = all"+doc.getMultiple()+".get(i)");
	        appendString("try{");
	        increaseIdent();
	        appendStatement("Property property = (("+DocumentGenerator.getDocumentName(doc)+")"+doc.getVariableName()+").getProperty(propertyName)");
	        appendStatement("if (property.getValue()==null && value==null){");
	        appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
	        appendString("}else{");
	        increaseIdent();
	        appendString("if (value!=null && property.getValue().equals(value))");
	        appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
	        append(closeBlock());
	        decreaseIdent();
			appendString("}catch(NoSuchPropertyException nspe){");
			increaseIdent();
			appendString("if (value==null)");
			appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
			decreaseIdent();
	        appendString("}catch(Exception ignored){}");

	        append(closeBlock());
	        appendString("return ret;");
	        append(closeBlock());
	        emptyline();

			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value, SortType sortType){");
			increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"ByProperty(propertyName, value), sortType)");
			append(closeBlock());

			appendComment("Executes a query on "+doc.getMultiple());
			appendString("public QueryResult executeQueryOn"+doc.getMultiple()+"(DocumentQuery query){");
			increaseIdent();
			appendStatement(listDecl+" all"+doc.getMultiple()+" = get"+doc.getMultiple()+"()");
			appendStatement("QueryResult result = new QueryResult()");
			appendString("for (int i=0; i<all"+doc.getMultiple()+".size(); i++){");
			increaseIdent();
			appendStatement("List<QueryResultEntry> partialResult = query.match(all"+doc.getMultiple()+".get(i))");
			appendStatement("result.add(partialResult)");
			append(closeBlock());

			appendStatement("return result");
			append(closeBlock());
			emptyline();

			appendComment("Returns all "+doc.getName()+" objects, where property matches.");
//	        appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(QueryProperty... property){");
//	        increaseIdent();
//	        appendStatement("throw new RuntimeException(\"Not yet implemented\")");
//	        append(closeBlock());
//	        emptyline();
//	        
//			appendComment("Returns all "+doc.getName()+" objects, where property matches, sorted");
//			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(SortType sortType, QueryProperty... property){");
//	        increaseIdent();
//	        appendStatement("throw new RuntimeException(\"Not yet implemented\")");
//	        append(closeBlock());
//			emptyline();

			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(QueryProperty... property){");
			increaseIdent();
			appendString("//first the slow version, the fast version is a todo.");
			appendStatement(listDecl+" ret = new ArrayList<"+doc.getName()+">()");
			appendStatement(listDecl+" src = get"+doc.getMultiple()+"()");
			appendStatement("for ( "+doc.getName()+" "+doc.getVariableName() +" : src){");
			increaseIdent();
			appendStatement("boolean mayPass = true");
			appendStatement("for (QueryProperty qp : property){");
			increaseIdent();
			appendStatement("mayPass = mayPass && qp.doesMatch("+doc.getVariableName()+".getPropertyValue(qp.getName()))");
			append(closeBlock());

			appendString("if (mayPass)");
			appendIncreasedStatement("ret.add("+doc.getVariableName()+")");
			append(closeBlock());

			appendStatement("return ret");
			append(closeBlock());
	        emptyline();

			appendComment("Returns all "+doc.getName()+" objects, where property matches, sorted");
			appendString("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(SortType sortType, QueryProperty... property){");
	        increaseIdent();
	        appendStatement("return StaticQuickSorter.sort(get"+doc.getMultiple()+"ByProperty(property), sortType)");
	        append(closeBlock());
			emptyline();
			
			// get elements COUNT
			appendComment("Returns " + doc.getName() + " objects count.");
			appendString("public int get" + doc.getMultiple() + "Count() {");
			increaseIdent();
			appendStatement("return " + getModuleGetterCall(module) + ".get" + doc.getMultiple() + "().size()");
			append(closeBlock());
			emptyline();
			// end get elements COUNT

			// get elements Segment
			appendComment("Returns " + doc.getName() + " objects segment.");
			appendString("public " + listDecl + " get" + doc.getMultiple() + "(Segment aSegment) {");
			increaseIdent();
			appendStatement("return Slicer.slice(aSegment, get" + doc.getMultiple() + "()).getSliceData()");
			append(closeBlock());
			emptyline();
			// end get elements Segment

			// get elements Segment with FILTER
			appendComment("Returns " + doc.getName() + " objects segment, where property matched.");
			appendString("public " + listDecl + " get" + doc.getMultiple() + "ByProperty(Segment aSegment, QueryProperty... property) {");
			increaseIdent();
			appendStatement("int pLimit = aSegment.getElementsPerSlice()");
			appendStatement("int pOffset = aSegment.getSliceNumber() * aSegment.getElementsPerSlice() - aSegment.getElementsPerSlice()");
			appendStatement(listDecl + " ret = new ArrayList<" + doc.getName() + ">()");
			appendStatement(listDecl + " src = get" + doc.getMultiple() + "()");
			appendStatement("for (" + doc.getName() + " " + doc.getVariableName() + " : src) {");
			increaseIdent();
			appendStatement("boolean mayPass = true");
			appendStatement("for (QueryProperty qp : property) {");
			increaseIdent();
			appendStatement("mayPass = mayPass && qp.doesMatch(" + doc.getVariableName() + ".getPropertyValue(qp.getName()))");
			append(closeBlock());
			appendString("if (mayPass)");
			appendIncreasedStatement("ret.add(" + doc.getVariableName() + ")");
			appendString("if (ret.size() > pOffset + pLimit)");			
			appendIncreasedStatement("break");
			append(closeBlock());
			appendStatement("return Slicer.slice(aSegment, ret).getSliceData()");
			append(closeBlock());
			emptyline();
			// end get elements Segment with FILTER
			
			// get elements Segment with SORTING, FILTER
			appendComment("Returns " + doc.getName() + " objects segment, where property matched, sorted.");
			appendString("public " + listDecl + " get" + doc.getMultiple()
					+ "ByProperty(Segment aSegment, SortType aSortType, QueryProperty... aProperty){");
			increaseIdent();
			appendStatement("return StaticQuickSorter.sort(get" + doc.getMultiple() + "ByProperty(aSegment, aProperty), aSortType)");
			append(closeBlock());
			emptyline();
			// end get elements Segment with SORTING, FILTER

			if (GeneratorDataRegistry.hasLanguageCopyMethods(doc)){
				containsAnyMultilingualDocs = true;
				appendCommentLine("This method is not very fast, since it makes an update (eff. save) after each doc.");
				appendString("public void copyMultilingualAttributesInAll"+doc.getMultiple()+"(String sourceLanguage, String targetLanguage){");
				increaseIdent();
				appendStatement("List<"+doc.getName()+"> allDocumentsSrc = get"+doc.getMultiple()+"()");
				appendStatement("List<"+doc.getName()+"> allDocuments = new ArrayList<"+doc.getName()+">(allDocumentsSrc.size())");
				appendStatement("allDocuments.addAll(allDocumentsSrc)");
				appendString("for ("+doc.getName()+" document : allDocuments){");
				increaseIdent();
				appendStatement("document.copyLANG2LANG(sourceLanguage, targetLanguage)");
//				appendStatement("update"+doc.getName()+"(document)");
				append(closeBlock());
				appendStatement("update"+doc.getMultiple()+"(allDocuments)");
				append(closeBlock());
				emptyline();

			}


	    }

	    if (containsAnyMultilingualDocs){
			appendComment("Copies all multilingual fields from sourceLanguage to targetLanguage in all data objects (documents, vo) which are part of this module and managed by this service");
			appendString("public void copyMultilingualAttributesInAllObjects(String sourceLanguage, String targetLanguage){");
			increaseIdent();
			for (MetaDocument doc : docs){
				if (GeneratorDataRegistry.hasLanguageCopyMethods(doc))
					appendStatement("copyMultilingualAttributesInAll"+doc.getMultiple()+"(sourceLanguage, targetLanguage)");
			}
			append(closeBlock());
			emptyline();
	    }


	    //generate export function
	    emptyline();
	    for (MetaDocument d : docs){
	    	appendStatement("public XMLNode export"+d.getMultiple()+"ToXML(){");
	    	increaseIdent();
	    	appendStatement("XMLNode ret = new XMLNode("+quote(d.getMultiple())+")");
	    	appendStatement("List<"+d.getName()+"> list = get"+d.getMultiple()+"()");
	    	appendStatement("ret.addAttribute(new XMLAttribute("+quote("count")+", list.size()))");
	    	appendString("for ("+d.getName()+" object : list)");
	    	appendIncreasedStatement("ret.addChildNode("+DataFacadeGenerator.getXMLHelperName(d)+".toXML(object))");
	    	appendStatement("return ret");

	    	append(closeBlock());
	    	emptyline();

	    	appendStatement("public XMLNode export"+d.getMultiple()+"ToXML(String[] languages){");
	    	increaseIdent();
	    	appendStatement("XMLNode ret = new XMLNode("+quote(d.getMultiple())+")");
	    	appendStatement("List<"+d.getName()+"> list = get"+d.getMultiple()+"()");
	    	appendStatement("ret.addAttribute(new XMLAttribute("+quote("count")+", list.size()))");
	    	appendString("for ("+d.getName()+" object : list)");
	    	appendIncreasedStatement("ret.addChildNode("+DataFacadeGenerator.getXMLHelperName(d)+".toXML(object, languages))");
	    	appendStatement("return ret");

	    	append(closeBlock());
	    	emptyline();
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
	    append(closeBlock());


	    appendString("public XMLNode exportToXML(String[] languages){");
	    increaseIdent();
	    appendStatement("XMLNode ret = new XMLNode("+quote(module.getName())+")");
	    emptyline();
	    for (MetaDocument d : docs){
	    	appendStatement("ret.addChildNode(export"+d.getMultiple()+"ToXML(languages))");
	    }
	    emptyline();
	    appendStatement("return ret");
	    append(closeBlock());
	    return clazz;
	}

	private String getModuleGetterMethod(MetaModule module){
	    return "_get"+module.getModuleClassName();
	}

	private String getModuleGetterCall(MetaModule module){
	    return getModuleGetterMethod(module)+"()";
	}

	@Override protected String getMoskitoSubsystem(){
		return super.getMoskitoSubsystem()+"-cms";
	}

}
