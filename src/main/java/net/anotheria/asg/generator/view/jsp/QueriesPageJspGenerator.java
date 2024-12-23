package net.anotheria.asg.generator.view.jsp;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedJSPFile;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaLink;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator;
import net.anotheria.asg.generator.view.meta.MetaModuleSection;
import net.anotheria.asg.generator.view.meta.MetaSection;
import net.anotheria.asg.generator.view.meta.MetaView;
import net.anotheria.util.StringUtils;

/**
 * Generator for the queries page. Currently obsolete.
 *
 * @author another
 * @version $Id: $Id
 */
public class QueriesPageJspGenerator extends AbstractJSPGenerator implements IGenerator{
	

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.IGenerator#generate(net.anotheria.anodoc.generator.IGenerateable, net.anotheria.anodoc.generator.Context)
	 */
	/** {@inheritDoc} */
	public List<FileEntry> generate(IGenerateable g) {
	
		List<FileEntry> files = new ArrayList<FileEntry>();
		MetaView view = (MetaView)g;
	
		FileEntry menu = new FileEntry(generateMenu(view));
		menu.setType(".jsp");
		files.add(menu);

		FileEntry footer = new FileEntry(generateFooter(view, FOOTER_SELECTION_QUERIES, getFooterName(view)));
		footer.setType(".jsp");
		files.add(footer);

		for (int i=0; i<view.getSections().size(); i++){
			MetaSection s = (MetaSection)view.getSections().get(i);
			if (!(s instanceof MetaModuleSection))
				continue;
			MetaModuleSection section = (MetaModuleSection)s;
			MetaDocument doc = section.getDocument();
			if (doc.getLinks().size()>0){
				FileEntry showQueryFile = new FileEntry(generateShowQueriesPage(section, view));
				showQueryFile.setType(".jsp");
				files.add(showQueryFile);
			}
			
		}
		return files;
	}
	
	private GeneratedJSPFile generateShowQueriesPage(MetaModuleSection section, MetaView view){
		ident = 0;
		
		GeneratedJSPFile jsp = new GeneratedJSPFile();
		jsp.setPackage(getContext().getPackageName(section.getDocument())+".jsp");
		startNewJob(jsp);
		jsp.setName(getShowQueriesPageName(section.getDocument()));
		
		append(getBaseJSPHeader());
		
		MetaDocument doc = section.getDocument();
		
		appendString("<!--  generated by JspMafQueriesGenerator.generateShowQueriesPage -->");
		appendString("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		increaseIdent();
		appendString("<head>");
		increaseIdent();
		appendString("<title>"+view.getTitle()+"</title>");
		generatePragmas(view);
		appendString("<link href=\""+getCurrentCSSPath("newadmin.css")+"\" rel=\"stylesheet\" type=\"text/css\">");
		decreaseIdent();
		appendString("</head>");
		appendString("<body>");
		increaseIdent();
		//appendString("<jsp:include page=\""+getMenuName(view)+".jsp\" flush=\"true\"/>");

/*
		List elements = section.getElements();
		
		*/
		int colspan = 1;
		
		appendString("<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
		increaseIdent();
		openTR();
		appendString("<td colspan=\""+colspan+"\"><img src="+quote(getCurrentImagePath("s.gif"))+" width=\"1\" height=\"1\"></td>");
		closeTR();
		
		openTR();
		appendString("<td>&nbsp;</td>");
		closeTR();
		
		openTR("class="+quote("lineCaptions"));
		appendString("<td>&nbsp;Available queries:</td>");
		closeTR();
		
		List<MetaProperty> links = doc.getLinks();
		for (int i=0; i<links.size(); i++){
			MetaLink link = (MetaLink)links.get(i);
			
			if (link.isRelative())
				continue;
			MetaModule mod = GeneratorDataRegistry.getInstance().getModule(link.getTargetModuleName());
			MetaDocument targetDocument = mod.getDocumentByName(link.getTargetDocumentName());
			
			
			openTR("class="+quote(i%2==0 ? "lineLight" : "lineDark"));
			appendString("<form name="+quote(targetDocument.getName())+" action="+quote(CMSMappingsConfiguratorGenerator.getExecuteQueryPath(doc))+" method="+quote("GET")+"><input type="+quote("hidden")+" name="+quote("property")+" value="+quote(link.getName()));
			openTD();
			increaseIdent();
			appendString("&nbsp;Show all "+section.getTitle()+" where "+link.getName()+" is:&nbsp;");
			openTag("select", "name="+quote("criteria"));
			increaseIdent();
			
			appendString("<option value=\"\">none</option>");
			appendString("<ano:iterate name="+quote(targetDocument.getMultiple().toLowerCase())+" type="+quote("net.anotheria.asg.util.bean.LabelValueBean")+" id="+quote("entry")+">");
			appendIncreasedString("<option value="+quote("<ano:write name="+quote("entry")+" property="+quote("value")+"/>")+">"+"<ano:write name="+quote("entry")+" property="+quote("label")+" filter="+quote("false")+"/>"+"</option>");
			appendString("</ano:iterate>");
			//ret += writeClosingTag("select");
			decreaseIdent();
			appendString("</select>&nbsp;<a href="+quote("#")+" onClick="+quote("document.forms."+targetDocument.getName()+".submit(); return false")+">GO</a>");
			decreaseIdent();
			closeTD();
			appendString("</form>");
			closeTR();
		}

		decreaseIdent();
		appendString("</table>");
		decreaseIdent();
		appendString("<jsp:include page=\""+getFooterName(view)+".jsp\" flush=\"true\"/>");
		appendString("</body>");
		decreaseIdent();
		appendString("</html>");
		appendString("<!-- / generated by JspMafQueriesGenerator.generateShowQueriesPage -->");
		append(getBaseJSPFooter()); 
		return jsp;
	}
	

	/** {@inheritDoc} */
	@Override
	protected String getMenuName(MetaView view){
		return "../../shared/jsp/"+StringUtils.capitalize(view.getName())+"QueriesMenu";		
	}

	private GeneratedJSPFile generateMenu(MetaView view){
		
		GeneratedJSPFile jsp = new GeneratedJSPFile();
		jsp.setPackage(getContext().getJspPackageName(MetaModule.SHARED));
		jsp.setName(getMenuName(view));
		
		append(getBaseJSPHeader());
		
		appendString("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		increaseIdent();
		appendString("<tr>");
		increaseIdent();
		appendString("<ano:iterate name=\"queriesMenu\" type=\"net.anotheria.asg.util.bean.MenuItemBean\" id=\"entry\">");
		increaseIdent();
		appendString("<td>");
		increaseIdent();
		appendString("<ano:equal name=\"entry\" property=\"active\" value=\"true\">");
		appendIncreasedString("<td class=\"menuTitleSelected\"><ano:write name=\"entry\" property=\"caption\"/></td>");
		appendString("</ano:equal>");
		appendString("<ano:notEqual name=\"entry\" property=\"active\" value=\"true\">");
		appendIncreasedString("<td class=\"menuTitle\"><a href=\"<ano:tslink><ano:write name=\"entry\" property=\"link\"/></ano:tslink>\"><ano:write name=\"entry\" property=\"caption\"/></a></td>");
		appendString("</ano:notEqual>");
		decreaseIdent();
		appendString("</td>");
		decreaseIdent();
		appendString("</ano:iterate>");
		

		decreaseIdent();
		appendString("</tr>");
		decreaseIdent();
		appendString("</table>");
		
		append(getBaseJSPFooter());
			
		return jsp;
	}
	
	/** {@inheritDoc} */
	@Override
	protected String getFooterName(MetaView view){
		return "../../shared/jsp/"+StringUtils.capitalize(view.getName())+"QueryFooter";		
	}
}
