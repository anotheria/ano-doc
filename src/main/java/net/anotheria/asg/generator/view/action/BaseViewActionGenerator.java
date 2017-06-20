package net.anotheria.asg.generator.view.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator;
import net.anotheria.asg.generator.view.meta.MetaCustomSection;
import net.anotheria.asg.generator.view.meta.MetaModuleSection;
import net.anotheria.asg.generator.view.meta.MetaSection;
import net.anotheria.asg.generator.view.meta.MetaView;
import net.anotheria.util.StringUtils;

/**
 * This generator generates the base action for a view.
 *
 * @author another
 * @version $Id: $Id
 */
public class BaseViewActionGenerator extends AbstractGenerator {

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.IGenerator#generate(net.anotheria.anodoc.generator.IGenerateable, net.anotheria.anodoc.generator.Context)
	 */
	/**
	 * <p>generate.</p>
	 *
	 * @param g a {@link net.anotheria.asg.generator.IGenerateable} object.
	 * @return a {@link net.anotheria.asg.generator.FileEntry} object.
	 */
	public FileEntry generate(IGenerateable g) {
		
		MetaView view = (MetaView)g;
		return new FileEntry(generateViewAction(view));
	}
	
	/**
	 * <p>getViewActionName.</p>
	 *
	 * @param view a {@link net.anotheria.asg.generator.view.meta.MetaView} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getViewActionName(MetaView view){
		return "Base"+StringUtils.capitalize(view.getName())+"Action";
	}
	
	/**
	 * <p>generateViewAction.</p>
	 *
	 * @param view a {@link net.anotheria.asg.generator.view.meta.MetaView} object.
	 * @return a {@link net.anotheria.asg.generator.GeneratedClass} object.
	 */
	public GeneratedClass generateViewAction(MetaView view){

		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".action");
		
		List<MetaSection> sections = view.getSections();
		List<MetaModule> modules = new ArrayList<MetaModule>();
		
		for (int i=0; i<sections.size(); i++){
			MetaSection section = sections.get(i);
			if (section instanceof MetaModuleSection){
				if (modules.indexOf(((MetaModuleSection)section).getModule())==-1){
					modules.add(((MetaModuleSection)section).getModule());
				}
			}
		}		

		clazz.addImport("java.util.List");
		clazz.addImport("java.util.ArrayList");
		clazz.addImport("net.anotheria.maf.action.ActionMapping");
		clazz.addImport("net.anotheria.maf.bean.FormBean");
		clazz.addImport("net.anotheria.webutils.bean.NavigationItemBean");

		clazz.addImport("javax.servlet.http.HttpServletRequest");
		clazz.addImport("javax.servlet.http.HttpServletResponse");

		clazz.setAbstractClass(true);
		clazz.setParent(BaseActionGenerator.getBaseActionName());
		clazz.setGeneric("T extends FormBean");
		clazz.setName(getViewActionName(view));
		
		startClassBody();

		
		appendString("protected abstract String getTitle();");
		emptyline();
		
		appendString("@Override");
		appendString("protected String getActiveMainNavi() {");
		increaseIdent();
		appendStatement("return \""+StringUtils.capitalize(view.getTitle())+"\"");
		append(closeBlock());
		emptyline();

		appendString("protected boolean isPermitted(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {");
		increaseIdent();
		appendStatement("return "+!view.getName().toLowerCase().isEmpty());
		append(closeBlock());

		emptyline();		
			appendStatement("protected List<NavigationItemBean> getSubNavigation(){");
			appendStatement("List<NavigationItemBean> subNavi = new ArrayList<NavigationItemBean>()");
			increaseIdent();		
				for (int i=0; i<sections.size(); i++){
						MetaSection section = (MetaSection)sections.get(i);
						if (section instanceof MetaModuleSection)
							appendStatement("subNavi.add(makeMenuItemBean("+quote(section.getTitle())+", "+quote(CMSMappingsConfiguratorGenerator.getPath(((MetaModuleSection)section).getDocument(), CMSMappingsConfiguratorGenerator.ACTION_SHOW))+"))");
						if (section instanceof MetaCustomSection)
							appendStatement("subNavi.add(makeMenuItemBean("+quote(section.getTitle())+", "+quote(((MetaCustomSection)section).getPath())+"))");
								
					}
					appendStatement("return subNavi");
			append(closeBlock());
			
		emptyline();
		
		appendString("private NavigationItemBean makeMenuItemBean(String title, String link){");
		increaseIdent();
		appendString("NavigationItemBean bean = new NavigationItemBean();");
		appendString("bean.setCaption(title);");
		appendString("bean.setLink(link);");
		appendString("bean.setActive(title.equals(getTitle()));");
		appendString("return bean;");
		append(closeBlock());		
		emptyline();
		
		//security...
		appendString("protected boolean isAuthorizationRequired(){");
		increaseIdent();
		appendStatement("return true");
		append(closeBlock());
		emptyline();

		return clazz;
	}
}
