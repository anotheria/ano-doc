package net.anotheria.asg.generator.view;


import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.forms.meta.MetaForm;
import net.anotheria.asg.generator.meta.MetaContainerProperty;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaListProperty;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.meta.MetaTableProperty;
import net.anotheria.asg.generator.meta.StorageType;
import net.anotheria.asg.generator.view.action.IndexPageActionGenerator;
import net.anotheria.asg.generator.view.action.ModuleActionsGenerator;
import net.anotheria.asg.generator.view.jsp.IndexPageJspGenerator;
import net.anotheria.asg.generator.view.jsp.JspGenerator;
import net.anotheria.asg.generator.view.meta.MetaDialog;
import net.anotheria.asg.generator.view.meta.MetaModuleSection;
import net.anotheria.asg.generator.view.meta.MetaSection;
import net.anotheria.asg.generator.view.meta.MetaView;
import net.anotheria.util.StringUtils;
import net.anotheria.webutils.filehandling.actions.FileAjaxUpload;
import net.anotheria.webutils.filehandling.actions.GetFile;
import net.anotheria.webutils.filehandling.actions.ShowFile;
import net.anotheria.webutils.filehandling.actions.ShowTmpFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generator class for the CMSFilter.
 *
 * @author dmetelin
 * @version $Id: $Id
 */
public class CMSMappingsConfiguratorGenerator extends AbstractGenerator{
	
	/**
	 * Type op operation for dialogs.
	 * @author lrosenberg
	 *
	 */
	private static enum OperationType{
		SINGLE,
		MULTIPLE_DIALOG,
	}
	
	public enum SectionAction{
		SHOW("Show", "Show", OperationType.SINGLE, true),
		EDIT("Edit", "Edit", OperationType.SINGLE),
		NEW("New", "Edit", OperationType.SINGLE),
		LINKSTOME("LinksToMe", "LinksTo", OperationType.SINGLE, false){
			@Override
			public String getClassName(MetaModuleSection section){
				return "Edit" + section.getDocument().getName() + "Action";
			}
			@Override
			public String getViewName(MetaModuleSection section){
				MetaDocument doc = section.getDocument();
				return "LinksTo"+doc.getName();
			}
		},
		CLOSE("Close", "Show", OperationType.MULTIPLE_DIALOG),
		UPDATE("Update", "Show", OperationType.MULTIPLE_DIALOG),
		DELETE("Delete", "Show", OperationType.MULTIPLE_DIALOG),
		DUPLICATE("Duplicate", "Show", OperationType.MULTIPLE_DIALOG),
		PREVIEW("Preview", "Show", OperationType.MULTIPLE_DIALOG),
		LOCK("Lock", "EditBoxDialog", OperationType.MULTIPLE_DIALOG),
		UNLOCK("UnLock", "EditBoxDialog", OperationType.MULTIPLE_DIALOG),
		TRANSFER("Transfer", "Show", OperationType.SINGLE),
		COPYLANG("CopyLang", "EditBoxDialog", OperationType.MULTIPLE_DIALOG),
		SWITCHMULTILANG("SwitchMultilang", "EditBoxDialog", OperationType.MULTIPLE_DIALOG),
		VERSIONINFO("Versioninfo", "EditBoxDialog", OperationType.MULTIPLE_DIALOG),
		EXPORTtoCSV("Export.csv", "Show", OperationType.SINGLE, true){
			@Override
			public String getClassName(MetaModuleSection section){
				return "Export" + section.getDocument().getName(true) + "Action";
			}
			@Override
			public String getViewName(MetaModuleSection section){
				MetaDocument doc = section.getDocument();
				return "Show"+doc.getName(true) + "AsCSV";
			}
		},
		EXPORTtoXML("Export.xml", "Show", OperationType.SINGLE, true){
			@Override
			public String getClassName(MetaModuleSection section){
				return "Export" + section.getDocument().getName(true) + "Action";
			}
			@Override
			public String getViewName(MetaModuleSection section){
				MetaDocument doc = section.getDocument();
				return "Show"+doc.getName(true) + "AsXML";
			}
		},
		
		;
		
		private String action;
		private String view;
		private OperationType type;
		private boolean multiDocument;
		
		private SectionAction(String anAction, String aView, OperationType aType) {
			this(anAction, aView, aType, false);
		}
		
		private SectionAction(String anAction, String aView, OperationType aType, boolean aListDocument) {
			action = anAction;
			view = aView;
			type = aType;
			multiDocument = aListDocument;
		}
		
		public String getClassName(MetaModuleSection section) {
			switch (type) {
			case SINGLE:
				return action + section.getDocument().getName(multiDocument) + "Action";
			case MULTIPLE_DIALOG:
				return ModuleActionsGenerator.getMultiOpDialogActionName(section);
			}
			throw new AssertionError("Unsuported OperationType!");

		}
		
		public String getMappingName(MetaModuleSection section){
			return getMappingName(section.getDocument());
		}
		
		public String getMappingName(MetaDocument doc){
			return doc.getParentModule().getName().toLowerCase()+StringUtils.capitalize(doc.getName()) + action;
		}
		
		
		public String getViewName(MetaModuleSection section){
			MetaDocument doc = section.getDocument();
			return view+doc.getName(multiDocument);
		}
		
		public String getViewPath(MetaModuleSection section){
			return "/" + FileEntry.package2path(JspGenerator.getPackage(section.getModule())) + "/";
		}
		
		public String getViewFullName(MetaModuleSection section){
			return getViewPath(section) + getViewName(section);
		}

		public boolean isIgnoreForSection(MetaModuleSection section){
			return !multiDocument && (section.getDialogs().size() == 0 || StorageType.FEDERATION == section.getModule().getStorageType());
		}
		
	}
	
	public static enum SharedAction{
		//SHOW("Show", "Show", OperationType.SINGLE, true, false),
		
		SEARCH("CmsSearch", "SearchResult"){
//			@Override
//			public String getViewName(MetaModuleSection section){
//				return "SearchResultMaf";
//			}
//			@Override
//			public String getViewPath(MetaModuleSection section){
//				return "/" + FileEntry.package2path(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".jsp") + "/";
//			}
//			@Override
//			public String getClassName(MetaModuleSection section){
//				return "SearchMafAction";
//			}
		},
		;
		
		private String action;
		private String view;
		
		private SharedAction(String anAction, String aView) {
			action = anAction;
			view = aView;
		}
		
		public String getClassName() {
				return action + "Action";
		}
		
		public String getMappingName(){
			return action.toLowerCase();
		}
		
		
		public String getViewName(){
			return view;
		}
		
		public String getViewPath(){
			return "/" + FileEntry.package2path(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".jsp") + "/";
		}
		
		public String getViewFullName(){
			return getViewPath() + getViewName();
		}
		
		public static final String getPackageName(){
			return GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".action";
		}

	}
	
	public static enum ContainerAction{
		SHOW("Show"),
		DELETE("Delete"),
		MOVE("Move"),
		ADD("Add"),
		QUICKADD("QuickAdd"),
		;
		
		private String action;
		
		private ContainerAction(String anAction) {
			action = anAction;
		}
		
		public String getClassName(MetaDocument doc, MetaContainerProperty container) {
			return ModuleActionsGenerator.getContainerMultiOpActionName(doc, container);
		}
		
		
		public String getMappingName(MetaDocument doc, MetaContainerProperty container){
			return doc.getParentModule().getName().toLowerCase()+StringUtils.capitalize(doc.getName())+StringUtils.capitalize(container.getName())+ action;
		}
		
		public String getViewName(MetaDocument doc, MetaContainerProperty container){
			return JspGenerator.getContainerPageName(doc, container);
		}
		
	}

	
	/**
	 * <p>generate.</p>
	 *
	 * @param views a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<FileEntry> generate(List<MetaView> views) {
		List<FileEntry> ret = new ArrayList<FileEntry>(); 
		try{
			ret.add(new FileEntry(generateCMSMapping(views)));
		}catch(Exception e){
			System.out.println("CMSMappingsConfiguratorGenerator error: " + e.getMessage());
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * <p>getClassSimpleName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getClassSimpleName(){
		return "CMSMappingsConfigurator";
	}
	
	/**
	 * <p>getClassName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static String getClassName(){
		return getPackageName() + "." + getClassSimpleName();
	}
	
	private GeneratedClass generateCMSMapping(List<MetaView> views){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
		clazz.setPackageName(getPackageName());
		
		clazz.addImport(Map.class);
		clazz.addImport(HashMap.class);
		clazz.addImport(net.anotheria.maf.action.CommandForward.class);
		clazz.addImport(net.anotheria.maf.action.ActionMappings.class);
		clazz.addImport(net.anotheria.maf.action.ActionMappingsConfigurator.class);
		clazz.addImport(IndexPageActionGenerator.getIndexPageFullName());


		clazz.addInterface("ActionMappingsConfigurator");
		clazz.setName(getClassSimpleName());

		startClassBody();
		
		emptyline();
		
		appendString("private static final Map<String, String> showActionsRegistry;");
		appendString("static{");
		increaseIdent();
		appendStatement("showActionsRegistry = new HashMap<String,String>()");
		
		for(MetaView view: views){
			for (MetaSection section: view.getSections()){
				if (!(section instanceof MetaModuleSection))
					continue;
				MetaModuleSection s = (MetaModuleSection)section;
				generateActionsRegistry(s);
			}
		}
		closeBlock("close static block");
		emptyline();
		
		openFun("public static String getActionPath(String parentName, String documentName)");
		appendStatement("return showActionsRegistry.get(parentName + \".\" + documentName)");
		closeBlock("getActionPath");
		emptyline();
		
		appendString("@Override");
		openFun("public void configureActionMappings(ActionMappings mappings)");

		appendStatement("mappings.addMapping(\"index\", " + IndexPageActionGenerator.getIndexPageActionName() + ".class, new CommandForward(\"success\", "+quote(IndexPageJspGenerator.getIndexJspFullName())+"))");
		appendStatement("mappings.addMapping(\"fileShow\", "+quote(ShowFile.class.getName())+", new CommandForward(\"success\", \"/net/anotheria/webutils/jsp/UploadFile.jsp\"))");
		appendStatement("mappings.addMapping(\"fileUpload\", "+quote(FileAjaxUpload.class.getName())+")");

		appendStatement("mappings.addMapping(\"showTmpFile\", "+quote(ShowTmpFile.class.getName())+")");
		appendStatement("mappings.addMapping(\"getFile\", "+quote(GetFile.class.getName())+")");
		
		appendStatement("mappings.addMapping(\"login\", net.anotheria.anosite.cms.action.LoginAction.class, new CommandForward(\"success\", \"/net/anotheria/anosite/cms/jsp/Login.jsp\"))");
		appendStatement("mappings.addMapping(\"logout\", net.anotheria.anosite.cms.action.LogoutAction.class, new CommandForward(\"success\", \"/net/anotheria/anosite/cms/jsp/Login.jsp\"))");
        appendStatement("mappings.addMapping(\"changePass\", net.anotheria.anosite.cms.action.ChangePassAction.class, new CommandForward(\"success\", \"/net/anotheria/anosite/cms/jsp/ChangePass.jsp\"))");

        appendStatement("mappings.addMapping(\"showUsages\", net.anotheria.anosite.bredcrambs.action.ShowUsagesOfDocumentAction.class)");

        generateSharedMappings(clazz);

        for (MetaView view: views) {
        	for (MetaSection section: view.getSections()) {
        		if (!(section instanceof  MetaModuleSection))
        			continue;

        		MetaModuleSection s = (MetaModuleSection) section;
        		appendStatement("configureActionMappings" + s.getDocument().getName()  + "(mappings)");
			}
		}

		closeBlock("configureActionMappings");
        emptyline();


        for(MetaView view: views){
			for (MetaSection section: view.getSections()){
				if (!(section instanceof MetaModuleSection))
					continue;
				MetaModuleSection s = (MetaModuleSection)section;


//				if(s.getDialogs().size() == 0)
//					continue;
				openFun("private void configureActionMappings" + s.getDocument().getName() + "(ActionMappings mappings)");

				generateSectionMappings(clazz, s);
				emptyline();
				MetaDocument doc = s.getDocument();
				for (int p=0; p<doc.getProperties().size(); p++){
					MetaProperty pp = doc.getProperties().get(p);
					if (pp instanceof MetaContainerProperty){
						generateContainerMappings(clazz, s, (MetaContainerProperty)pp);
					}
				}
				closeBlock("configure " + s.getDocument().getName());
				emptyline();
			}
		}
		
		return clazz;
	}

	
	
	private static String getPackageName(){
		return GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED) + ".filter";
	}
	
	private void generateSectionMappings(GeneratedClass clazz, MetaModuleSection section){
		MetaModule module = section.getModule();
		String actionsPackage = ModuleActionsGenerator.getPackage(module);
		boolean validatedUpdateAction = section.isValidatedOnSave();
		for(SectionAction action: SectionAction.values()){
			if(action.isIgnoreForSection(section))
				continue;
//			String actionName = action.getClassName(section);
//			clazz.addImport(actionsPackage + "." + actionName);
			String actionName = actionsPackage + "." + action.getClassName(section);
			appendStatement("mappings.addMapping("+ quote(action.getMappingName(section)) +", "+  actionName +".class, new CommandForward(\"success\"," + quote(action.getViewFullName(section)+".jsp") + "))");
			if (validatedUpdateAction && action.equals(SectionAction.UPDATE)) {
				appendStatement("mappings.addMapping("+ quote(action.getMappingName(section)) +", "+  actionName +".class, new CommandForward(\"validationError\"," + quote(SectionAction.NEW.getViewFullName(section)+".jsp") + "))");
			}
		}

	}
	
	private void generateActionsRegistry(MetaModuleSection section){
			if(SectionAction.SHOW.isIgnoreForSection(section))
				return;
			appendStatement("showActionsRegistry.put(" + quote(section.getModule().getName() + "." + section.getDocument().getName()) + ", " + quote(SectionAction.SHOW.getMappingName(section)) + ")");
	}
	
	
	private void generateSharedMappings(GeneratedClass clazz){
		String actionsPackage = GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".action";
		for(SharedAction action: SharedAction.values()){
//			String actionName = action.getClassName();
//			clazz.addImport(actionsPackage + "." + actionName);
			String actionName = actionsPackage + "." + action.getClassName();
			appendStatement("mappings.addMapping("+ quote(action.getMappingName()) +", "+  actionName +".class, new CommandForward(\"success\"," + quote(action.getViewFullName()+".jsp") + "))");
		}

	}
	
	private void generateContainerMappings(GeneratedClass clazz, MetaModuleSection section, MetaContainerProperty container){
		if(section.getDialogs().size() == 0)
			return;
		MetaDocument doc = section.getDocument();
		String actionsPackage = ModuleActionsGenerator.getPackage(doc);
		String jspPath = FileEntry.package2fullPath(JspGenerator.getPackage(doc)).substring(FileEntry.package2fullPath(JspGenerator.getPackage(doc)).indexOf('/'))+"/";
		
		
		for(ContainerAction action: ContainerAction.values()){
//			String actionName = action.getClassName(doc, container);
//			clazz.addImport(actionsPackage + "." + actionName);
			String actionName = actionsPackage + "." + action.getClassName(doc, container);
			appendStatement("mappings.addMapping("+ quote(action.getMappingName(doc, container)) +", "+  actionName +".class, new CommandForward(\"success\"," + quote(jspPath + action.getViewName(doc, container)+".jsp") + "))");
		}

	}
	
	
	//TODO: Investigate this methods copied from StrutsCOnfigGenerator
	/**
	 * Show action for data.
	 */
	public static final String ACTION_SHOW   = "show";
	/**
	 * new dialog.
	 */
	public static final String ACTION_NEW    = "new";
	/**
	 * edit existing element.
	 */
	public static final String ACTION_EDIT   = "edit";
	/**
	 * creates a new element.
	 */
	public static final String ACTION_CREATE = "create";
	/**
	 * Updates an existing element.
	 */
	public static final String ACTION_UPDATE = "update";
	/**
	 * Deletes an element.
	 */
	public static final String ACTION_DELETE = "delete";
	/** Constant <code>ACTION_VERSIONINFO="versioninfo"</code> */
	public static final String ACTION_VERSIONINFO = "versioninfo";
	/** Constant <code>ACTION_DUPLICATE="duplicate"</code> */
	public static final String ACTION_DUPLICATE = "duplicate";
	/** Constant <code>ACTION_DEEPCOPY="deepcopy"</code> */
	public static final String ACTION_DEEPCOPY = "deepcopy";
	/** Constant <code>ACTION_ADD="add"</code> */
	public static final String ACTION_ADD 	 = "add";
	/** Constant <code>ACTION_QUICK_ADD="quickAdd"</code> */
	public static final String ACTION_QUICK_ADD  = "quickAdd";
	/** Constant <code>ACTION_EXPORT="export"</code> */
	public static final String ACTION_EXPORT = "export";
	/** Constant <code>ACTION_SHOW_QUERIES="showQueries"</code> */
	public static final String ACTION_SHOW_QUERIES = "showQueries";
	/** Constant <code>ACTION_EXECUTE_QUERY="execQuery"</code> */
	public static final String ACTION_EXECUTE_QUERY = "execQuery";
	/** Constant <code>ACTION_LINKS_TO_ME="LinksToMe"</code> */
	public static final String ACTION_LINKS_TO_ME = "LinksToMe";
	/** Constant <code>ACTION_MOVE="move"</code> */
	public static final String ACTION_MOVE = "move";
	/** Constant <code>ACTION_SEARCH="search"</code> */
	public static final String ACTION_SEARCH = "search";
	/** Constant <code>ACTION_COPY_LANG="copyLang"</code> */
	public static final String ACTION_COPY_LANG ="copyLang";
	/** Constant <code>ACTION_SWITCH_MULTILANGUAGE_INSTANCE="switchMultilang"</code> */
	public static final String ACTION_SWITCH_MULTILANGUAGE_INSTANCE = "switchMultilang";
    /**
     * show usages of an element.
     */
    public static final String ACTION_SHOW_USAGES = "showUsages";
	/**
	 * Locks current document.
	 */
    public static final String ACTION_LOCK = "lock";
	/**
	 * Unlocks current document.
	 */
    public static final String ACTION_UNLOCK = "unLock";
    /** Constant <code>ACTION_CLOSE="close"</code> */
    public static final String ACTION_CLOSE = "close";
	/**
	 * Transfers current document to prod.
	 */
	public static final String ACTION_TRANSFER = "transfer";
	
	/**
	 * <p>getPath.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @param action a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getPath(MetaDocument doc, String action){
		return doc.getParentModule().getName().toLowerCase()+StringUtils.capitalize(doc.getName())+StringUtils.capitalize(action);
	}
	/**
	 * <p>getShowQueriesPath.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getShowQueriesPath(MetaDocument doc){
		return getPath(doc, ACTION_SHOW_QUERIES);
	}
	/**
	 * <p>getShowCMSPath.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getShowCMSPath(MetaDocument doc){
		return getPath(doc, ACTION_SHOW);
	}
	/**
	 * <p>getDialogFormName.</p>
	 *
	 * @param dialog a {@link net.anotheria.asg.generator.view.meta.MetaDialog} object.
	 * @param document a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getDialogFormName(MetaDialog dialog, MetaDocument document) {
		return dialog.getName() + document.getParentModule().getName() + document.getName() + "Form";
	}
	/**
	 * <p>getContainerPath.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @param container a {@link net.anotheria.asg.generator.meta.MetaContainerProperty} object.
	 * @param action a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getContainerPath(MetaDocument doc, MetaContainerProperty container, String action){
		return doc.getParentModule().getName().toLowerCase()+StringUtils.capitalize(doc.getName())+StringUtils.capitalize(container.getName())+StringUtils.capitalize(action);
	}
	/**
	 * <p>getFormName.</p>
	 *
	 * @param form a {@link net.anotheria.asg.generator.forms.meta.MetaForm} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getFormName(MetaForm form){
	    return StringUtils.capitalize(form.getId())+"AutoForm";
	}
	/**
	 * <p>getFormPath.</p>
	 *
	 * @param form a {@link net.anotheria.asg.generator.forms.meta.MetaForm} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getFormPath(MetaForm form){
	    return form.getId()+StringUtils.capitalize(form.getAction());
	}
	/**
	 * <p>getExecuteQueryPath.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getExecuteQueryPath(MetaDocument doc){
		return getPath(doc, ACTION_EXECUTE_QUERY);
	}
	/**
	 * <p>getContainerEntryFormName.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @param property a {@link net.anotheria.asg.generator.meta.MetaContainerProperty} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getContainerEntryFormName(MetaDocument doc, MetaContainerProperty property){
		String nameAddy = "XXX";
		if (property instanceof MetaTableProperty)
			nameAddy = "Row";
		if (property instanceof MetaListProperty)
			nameAddy = "Element";
	    return doc.getParentModule().getName().toLowerCase()+StringUtils.capitalize(doc.getName())+StringUtils.capitalize(property.getName())+nameAddy+"Form";
	}
	/**
	 * <p>getContainerQuickAddFormName.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @param property a {@link net.anotheria.asg.generator.meta.MetaContainerProperty} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getContainerQuickAddFormName(MetaDocument doc, MetaContainerProperty property){
		String nameAddy = "XXX";
		if (property instanceof MetaListProperty)
			nameAddy = "QuickAdd";
	    return doc.getParentModule().getName().toLowerCase()+StringUtils.capitalize(doc.getName())+StringUtils.capitalize(property.getName())+nameAddy+"Form";
	}
	//TODO: end

}
