package net.anotheria.asg.generator.view.action;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator.SharedAction;
import net.anotheria.asg.generator.view.meta.MetaModuleSection;
import net.anotheria.asg.generator.view.meta.MetaSection;
import net.anotheria.asg.generator.view.meta.MetaView;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator for SMC search action.
 *
 * @author another
 * @version $Id: $Id
 */
public class CMSSearchActionsGenerator extends AbstractGenerator {

	/**
	 * <p>generate.</p>
	 *
	 * @param views a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<FileEntry> generate(List<MetaView> views) {
		List<FileEntry> files = new ArrayList<FileEntry>();
		files.add(new FileEntry(generateSearchAction(views)));
		files.add(new FileEntry(generateSearchFB()));
		return files;
	}

	/**
	 * <p>getSearchPackageName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getSearchPackageName() {
		return SharedAction.getPackageName();
	}

	/**
	 * <p>getCmsSearchActionName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getCmsSearchActionName() {
		return SharedAction.SEARCH.getClassName();
	}

	/**
	 * <p>getSearchPageFullName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getSearchPageFullName() {
		return getSearchPackageName() + "." + getCmsSearchActionName();
	}

	private GeneratedClass generateSearchAction(List<MetaView> views) {
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		appendGenerationPoint("generateSearchAction");
		clazz.setPackageName(getSearchPackageName());

		clazz.addImport("java.util.ArrayList");
		clazz.addImport("java.util.List");
		clazz.addImport("java.util.Collections");


		clazz.addImport("javax.servlet.http.HttpServletRequest");
		clazz.addImport("javax.servlet.http.HttpServletResponse");
		clazz.addImport("net.anotheria.anodoc.query2.DocumentQuery");
		clazz.addImport("net.anotheria.anodoc.query2.QueryResult");
		clazz.addImport("net.anotheria.anodoc.query2.QueryResultEntry");
		clazz.addImport("net.anotheria.anodoc.query2.ResultEntryBean");
		clazz.addImport("net.anotheria.anodoc.query2.string.ContainsStringQuery");
		clazz.addImport("net.anotheria.asg.exception.ASGRuntimeException");
		clazz.addImport(getSearchFBFullName());
		clazz.addImport("net.anotheria.maf.action.ActionCommand");
		clazz.addImport("net.anotheria.maf.action.ActionMapping");
		clazz.addImport("net.anotheria.maf.bean.FormBean");
		clazz.addImport("net.anotheria.maf.bean.annotations.Form");
		clazz.addImport("net.anotheria.webutils.bean.NavigationItemBean");
		clazz.addImport("net.anotheria.util.StringUtils");
		clazz.addImport("net.anotheria.asg.data.DataObject");

		clazz.setParent(BaseActionGenerator.getBaseActionName());
		clazz.setName(getCmsSearchActionName());

		startClassBody();
/*
		appendString("@Override");
		appendString("public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception{");
		increaseIdent();
		appendString("return super.execute(mapping, formBean, req, res);");
		closeBlock("");
		emptyline();
*/
		appendString("public ActionCommand anoDocExecute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception{");
		increaseIdent();
		appendStatement("String pCriteria = getStringParameter(req, "+quote("criteria")+")");
		appendStatement("String pModule = getStringParameter(req, "+quote("module")+")");
		appendStatement("String pDocument = getStringParameter(req, "+quote("document")+")");
		appendStatement("String pSearchArea = getStringParameter(req, "+quote("searchArea")+")");


		appendString("DocumentQuery query = new ContainsStringQuery(\"*\" + pCriteria + \"*(\\r\\n)?\");");
		appendString("QueryResult result = executeQuery(pModule, pDocument, query, pSearchArea);");
		appendStatement("addBeanToRequest(req, BEAN_DOCUMENT_DEF_NAME, pDocument)");
		appendStatement("addBeanToRequest(req, BEAN_MODULE_DEF_NAME, pModule)");
		appendStatement("req.getSession().setAttribute(BEAN_SEARCH_SCOPE, pSearchArea)");
		appendString("if (result.getEntries().size()==0){");
		increaseIdent();
		appendString("req.setAttribute(\"srMessage\", \"Nothing found.\");");
		decreaseIdent();
		appendString("}else{");
		increaseIdent();
		appendString("List<ResultEntryBean> beans = new ArrayList<ResultEntryBean>(result.getEntries().size());");
		appendString("for (QueryResultEntry entry: result.getEntries()){");
		increaseIdent();
		appendStatement("DataObject doc = (DataObject)entry.getMatchedDocument()");
		appendString("ResultEntryBean bean = new ResultEntryBean();");

		appendString("bean.setEditLink(doc.getDefinedParentName().toLowerCase() + StringUtils.capitalize(doc.getDefinedName()) + \"Edit?pId=\" + doc.getId() + \"&ts=\" + System.currentTimeMillis());");

		appendString("bean.setDocumentId(entry.getMatchedDocument().getId());");
		appendString("bean.setDocumentName(doc.getDefinedName());");
		appendString("bean.setPropertyName(entry.getMatchedProperty().getId());");
		appendString("bean.setInfo(entry.getInfo().toHtml());");
		appendString("beans.add(bean);");
		closeBlock("");
		appendString("req.setAttribute(\"result\", beans);");
		closeBlock("");
		appendString("req.setAttribute(\"criteria\", pCriteria);");
		appendString("return mapping.success();");
		closeBlock("");
		emptyline();
		appendString("private QueryResult executeQuery(String sectionName, String documentName, DocumentQuery query, String searchArea) throws ASGRuntimeException{");
		increaseIdent();
		appendStatement("QueryResult ret = new QueryResult()");
		appendStatement("boolean wholeCms = \"cms\".equals(searchArea)");
		appendStatement("boolean wholeSection = wholeCms || \"section\".equals(searchArea)");

		for(MetaView view: views){
			emptyline();
			appendString("if(wholeCms || sectionName.equals(\""+view.getTitle()+"\")){");
			increaseIdent();
			for(MetaSection section: view.getSections()){
				if(!(section instanceof MetaModuleSection))
					continue;
				MetaModuleSection s = (MetaModuleSection)section;
				increaseIdent();
				appendString("if(wholeSection || documentName.equals(\""+s.getDocument().getName()+"\"))");
				appendIncreasedString("ret.add(get"+s.getModule().getName()+"Service().executeQueryOn"+s.getDocument().getName(true)+"(query).getEntries());");
				decreaseIdent();
			}
			closeBlock("if");
		}

		emptyline();
		appendStatement("return ret");
		closeBlock("executeQuery");
		emptyline();
		appendString("@Override");
		appendString("protected String getActiveMainNavi() {");
		increaseIdent();
		appendStatement("return null");
		closeBlock("");
		emptyline();
		appendString("@Override");
		appendString("protected List<NavigationItemBean> getSubNavigation(){");
		increaseIdent();
		appendStatement("return Collections.emptyList()");
		closeBlock("");
		appendString("@Override");
		appendString("protected String getCurrentDocumentDefName() {");
		increaseIdent();
		appendStatement("return null");
		closeBlock("getCurrentDocumentDefName");
		emptyline();
		appendString("@Override");
		appendString("protected String getCurrentModuleDefName() {");
		increaseIdent();
		appendStatement("return null");
		closeBlock("getCurrentModuleDefName");
		emptyline();
		return clazz;
	}

	/**
	 * <p>getSearchFBPackageName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getSearchFBPackageName() {
		return GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED) + ".bean";
	}

	/**
	 * <p>getSearchFBName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getSearchFBName() {
		return "SearchFB";
	}

	/**
	 * <p>getSearchFBFullName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getSearchFBFullName() {
		return getSearchFBPackageName() + "." + getSearchFBName();
	}

	private GeneratedClass generateSearchFB() {
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		appendGenerationPoint("generateSearchFB");
		clazz.setPackageName(getSearchFBPackageName());

		clazz.addImport("net.anotheria.maf.bean.FormBean");

		clazz.addInterface("FormBean");
		clazz.setName(getSearchFBName());

		startClassBody();

		appendStatement("private String criteria");
		appendStatement("private String module");
		appendStatement("private String document");
		appendStatement("private String searchArea");
		emptyline();

		appendString("public String getCriteria() {");
		increaseIdent();
		appendStatement("return criteria");
		closeBlockNEW();
		emptyline();

		appendString("public void setCriteria(String criteria) {");
		increaseIdent();
		appendStatement("this.criteria = criteria");
		closeBlockNEW();
		emptyline();

		appendString("public String getModule() {");
		increaseIdent();
		appendStatement("return module");
		closeBlock("");
		emptyline();

		appendString("public void setModule(String module) {");
		increaseIdent();
		appendStatement("this.module = module");
		closeBlock("");
		emptyline();

		appendString("public String getDocument() {");
		increaseIdent();
		appendStatement("return document");
		closeBlock("");
		emptyline();

		appendString("public void setDocument(String document) {");
		increaseIdent();
		appendStatement("this.document = document");
		closeBlockNEW();
		emptyline();

		appendString("public String getSearchArea() {");
		increaseIdent();
		appendStatement("return searchArea");
		closeBlockNEW();
		emptyline();

		appendString("public void setSearchArea(String searchArea) {");
		increaseIdent();
		appendStatement("this.searchArea = searchArea");
		closeBlockNEW();
		emptyline();


		emptyline();
		return clazz;
	}


}
