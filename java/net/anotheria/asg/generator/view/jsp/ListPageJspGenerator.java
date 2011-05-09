package net.anotheria.asg.generator.view.jsp;

import net.anotheria.asg.generator.GeneratedJSPFile;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaEnumerationProperty;
import net.anotheria.asg.generator.meta.MetaLink;
import net.anotheria.asg.generator.meta.MetaListProperty;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator.ContainerAction;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator.SectionAction;
import net.anotheria.asg.generator.view.action.ModuleBeanGenerator;
import net.anotheria.asg.generator.view.meta.MetaModuleSection;
import net.anotheria.util.StringUtils;

/**
 * Generates the jsps for the edit view.
 * 
 * @author another
 */
public class ListPageJspGenerator extends AbstractJSPGenerator {

	public GeneratedJSPFile generate(MetaModuleSection section, MetaDocument doc, MetaListProperty list) {
		
		GeneratedJSPFile jsp = new GeneratedJSPFile();
		startNewJob(jsp);

		jsp.setName(getContainerPageName(doc, list));
		jsp.setPackage(getContext().getJspPackageName(section.getModule()));

		resetIdent();
		appendGenerationPoint("generate");

		MetaProperty p = list.getContainedProperty();

		append(getBaseJSPHeader());

		appendString("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		increaseIdent();
		appendString("<head>");
		increaseIdent();
		appendString("<title>Edit " + doc.getName() + StringUtils.capitalize(list.getName()) + "</title>");
		generatePragmas();
		appendString("<link href=\"" + getCurrentCSSPath("newadmin.css") + "\" rel=\"stylesheet\" type=\"text/css\">");
		appendString("<link href=\"" + getCurrentCSSPath("fileuploader.css") + "\" rel=\"stylesheet\" type=\"text/css\"/>");

		appendString("<script type=\"text/javascript\" src=\"" + getCurrentJSPath("jquery-1.4.min.js") + "\"></script>");
		appendString("<script type=\"text/javascript\" src=\"" + getCurrentJSPath("anofunctions.js") + "\"></script>");
		appendString("<script type=\"text/javascript\" src=\"" + getCurrentJSPath("fileuploader.js") + "\"></script>");
		
		decreaseIdent();
		appendString("</head>");
		appendString("<body>");
		appendString("<jsp:include page=\"" + getTopMenuPage() + "\" flush=\"true\"/>");

		appendString("<div class=\"right\">");
		increaseIdent();
		appendString("<div class=\"r_w\">");
		increaseIdent();
		appendString("<div class=\"top_nav\">");
		increaseIdent();
		appendString("<div class=\"r_b_l\"><!-- --></div>");
		appendString("<div class=\"r_b_r\"><!-- --></div>");
		appendString("<div class=\"left_p\">");
		increaseIdent();
		appendString("<div class=\"clear\"><!-- --></div>");
		decreaseIdent();

		String backButtonHref = SectionAction.EDIT.getMappingName(section);
		backButtonHref += "?pId=<bean:write name=\"ownerId\"/>";
		appendString("<a href=" + backButtonHref + " class=\"button\"><span>Back to " + section.getDocument().getName() + "</span></a>");

		// SAVE AND CLOSE BUTTONS SHOULD BE HERE
		appendString("</div>");
		decreaseIdent();
		appendString("</div>");

		appendString("<div class=\"main_area\">");
		appendString("<div class=\"c_l\"><!-- --></div>");
		appendString("<div class=\"c_r\"><!-- --></div>");
		appendString("<div class=\"c_b_l\"><!-- --></div>");
		appendString("<div class=\"c_b_r\"><!-- --></div>");
		appendString("<h2>Add new item</h2>");
		appendString("<div class=\"clear\"><!-- --></div>");

		appendString("<table class=\"cmsList pages_table\" width=" + quote("100%") + " cellspacing=" + quote("1") + " cellpadding=" + quote("1") + " border="
				+ quote("0") + ">");
		appendString("<thead>");
		appendString("<tr class=" + quote("lineCaptions") + ">");
		appendIncreasedString("<td style=\"width:50px;\">Pos</td>");
		appendIncreasedString("<td style=\"width:50px;\">" + StringUtils.capitalize(list.getName()) + "</td>");
		appendIncreasedString("<td>" + "Description" + "</td>");
		appendIncreasedString("<td width=\"100\">&nbsp</td>");
		appendString("</tr>");

		appendString("</thead>");
		appendString("<tbody>");
		appendString("<logic:iterate name=" + quote("elements") + " id=" + quote("element") + " type="
				+ quote(ModuleBeanGenerator.getContainerEntryFormImport(doc, list)) + " indexId=" + quote("ind") + ">");
		increaseIdent();
		appendString("<tr class=\"cmsElement<%=ind.intValue()%2==0 ? \" lineLight\" : \" lineDark\"%> highlightable\">");
		increaseIdent();
		appendString("<td><bean:write name=" + quote("element") + " property=" + quote("position") + " filter=\"true\"/></td>");

		if (p.isLinked()) {

			MetaLink link2p = (MetaLink) p;
			MetaModule targetModule = link2p.getLinkTarget().indexOf('.') == -1 ? doc.getParentModule() : GeneratorDataRegistry.getInstance().getModule(
					link2p.getTargetModuleName());

			MetaDocument linkTarget = targetModule.getDocumentByName(link2p.getTargetDocumentName());
			String targetLinkAction = SectionAction.EDIT.getMappingName(linkTarget);

			appendString("<td><a href=<ano:tslink>" + quote(targetLinkAction + "?pId=<bean:write name=" + quote("element") + " property=" + quote(list.getContainedProperty().getName()) + "/></ano:tslink>")
					+ "><bean:write name=\"element\" property=" + quote(list.getContainedProperty().getName()) + " filter=\"true\"/></a></td>");
			appendString("<td><a href=<ano:tslink>" + quote(targetLinkAction + "?pId=<bean:write name=\"element\" property=" + quote(list.getContainedProperty().getName()) + "/></ano:tslink>")
					+ "><bean:write name=\"element\" property=\"description\" filter=\"true\"/></a></td>");
		} else if (p.getType() == MetaProperty.Type.IMAGE) {
			String imageName = "<bean:write name=\"element\" property=" + quote(list.getContainedProperty().getName()) + " filter=\"true\"/>";
			String imagePath = "getFile?pName=" + imageName;
			appendString("<td><a href=" + quote(imagePath) + " target=\"_blank\"><img src=" + quote(imagePath) + "alt=" + quote(imageName) + " class=\"thumbnail\">" + "</a></td>");
			appendString("<td>" + imageName + "<bean:write name=" + quote("element") + " property=" + quote("description") + " filter=\"true\"/></td>");
		} else {
			appendString("<td><bean:write name=" + quote("element") + " property=" + quote(list.getContainedProperty().getName()) + " filter=\"true\"/></td>");
			appendString("<td><bean:write name=" + quote("element") + " property=" + quote("description") + " filter=\"true\"/></td>");
		}

		String parameter = "ownerId=<bean:write name=" + quote("element") + " property=" + quote("ownerId") + "/>";
		parameter += "&pPosition=<bean:write name=" + quote("element") + " property=" + quote("position") + "/>";
		appendString("<td>");
		appendIncreasedString("<a href=" + quote(ContainerAction.MOVE.getMappingName(doc, list) + "?dir=top&" + parameter) + ">" + getTopImage("move to top") + "</a>");
		appendIncreasedString("<a href=" + quote(ContainerAction.MOVE.getMappingName(doc, list) + "?dir=up&" + parameter) + ">" + getUpImage("move up") + "</a>");
		appendIncreasedString("<a href=" + quote(ContainerAction.MOVE.getMappingName(doc, list) + "?dir=down&" + parameter) + ">" + getDownImage("move down") + "</a>");
		appendIncreasedString("<a href=" + quote(ContainerAction.MOVE.getMappingName(doc, list) + "?dir=bottom&" + parameter) + ">" + getBottomImage("move to bottom") + "</a>");
		appendIncreasedString("<a href=" + quote(ContainerAction.DELETE.getMappingName(doc, list) + "?" + parameter) + ">" + getDeleteImage("delete row") + "</a>");
		appendString("</td>");
		decreaseIdent();
		appendString("</tr>");
		decreaseIdent();
		appendString("</logic:iterate>");
		decreaseIdent();
		appendString("</tbody>");
		appendString("</table>");

		String name = p.getName();
		if (name == null || name.length() == 0)
			name = "&nbsp;";

		appendString("<table width=" + quote("100%") + " cellspacing=" + quote("0") + " cellpadding=" + quote("0") + " border=" + quote("0") + ">");
		appendString("<tbody>");
		increaseIdent();

		appendString("<tr>");
		appendIncreasedString("<td align=\"right\">Add&nbsp;" + name + ": </td>");
		appendString("<td align=\"left\">");
		generateAddEditor(doc, list);
		appendString("</td>");
		decreaseIdent();
		appendString("</tr>");

		generateQuickAddEditor(doc, list);

		appendString("</tbody>");
		appendString("</table>");
		decreaseIdent();
		appendString("<div class=\"clear\"><!-- --></div>");
		appendString("</div>");
		appendString("</div>");
		appendString("</div>");

		appendString("</body>");
		decreaseIdent();
		appendString("</html>");

		generateEditorJS(doc, list, name.toLowerCase() + "ValuesCollection");

		append(getBaseJSPFooter());
		return jsp;

	}
	
	private void generateAddEditor(MetaDocument doc, MetaListProperty list){
		MetaProperty p = list.getContainedProperty();
		String name = p.getName();
		String addFormAction = ContainerAction.ADD.getMappingName(doc, list);
		String addFormName = addFormAction + "ElementForm";
		
		appendString("<form name=" + quote(addFormName) + " action=" + quote(addFormAction) + " method=\"post\">");
		appendString("<input type=" + quote("hidden") + " name=" + quote("ownerId") + " value=\"<bean:write name=" + quote("ownerId") + "/>\">");
		
		if (p.isLinked() || (p instanceof MetaEnumerationProperty)) {
			String propertyName = list.getContainedProperty().getName();
			appendString("<em id=" + quote(propertyName) + " name=" + quote(propertyName) + " class=\"selectBox fll mr_10\"></em><div id=\"" + propertyName + "Selector\"></div>");
		} else if(p.getType() == MetaProperty.Type.IMAGE){ 
			appendString(getImageEditor(p));
		}else {
			appendIncreasedString("<input class=\"add_id fll\" type=\"text\" style=\"width:25%\" name=" + quote(name) + " value=\"\"/>");
		}
		
		appendString("<a href=" + quote("#") + " class=\"button\" onClick=" + quote("document." + addFormName + ".submit()") + "><span>Add</span></a>");
		appendString("</form>");
	}
	

	private String getImageEditor(MetaProperty p){
		String ret ="";
		ret += "<div id=\"file-uploader-" + p.getName() + "\" class=\"image_uploader\"><!-- --></div>\r";
		ret += "<script>\r";
		ret += "$(document).ready(function() {\r";
		ret += "	var uploader = new qq.FileUploader({\r";
		ret += "	    element: document.getElementById('file-uploader-" + p.getName() +"'),\r";
		ret += "	    action: '${pageContext.request.contextPath}/cms/fileUpload',\r";
		ret += "	    params: {\r";
		ret += "	    	property: '" + p.getName() + "'\r";
	    ret += "	    }\r";

		ret += "	});\r";
		ret += "});\r";
		ret += "</script>\r";
		return ret;
	}
	
	
	private void generateQuickAddEditor(MetaDocument doc, MetaListProperty list){
		MetaProperty p = list.getContainedProperty();
		if (!p.isLinked())
			return;

		String name = p.getName();
		String quickAddFormAction = ContainerAction.QUICKADD.getMappingName(doc, list);
		String quickAddFormName = quickAddFormAction + "Form";
		 {
			increaseIdent();
			appendString("<tr>");
			appendString("<td align=\"right\">");
			appendString("Quick&nbsp;add:");
			appendString("</td>");
			appendString("<td align=\"left\">");
			appendString("<form name=" + quote(quickAddFormName) + " action=" + quote(quickAddFormAction) + " method=\"post\">");
			increaseIdent();
			appendString("<input type=" + quote("hidden") + " name=" + quote("ownerId") + " value=\"<bean:write name=" + quote("ownerId") + "/>\">");

			p = list.getContainedProperty();

			name = p.getName();
			if (name == null || name.length() == 0)
				name = "&nbsp;";
			String field = "";
			field += "<input class=\"add_id fll\" type=\"text\" style=\"width:25%;\" name=" + quote("quickAddIds");
			field += " value=\"\"/><span class=\"fll mr_10 mt_4 mt_5\">id's comma separated list.</span>";
			appendString(field);
			decreaseIdent();
			decreaseIdent();
			appendString("<a href=" + quote("#") + " class=\"button\" onClick=" + quote("document." + quickAddFormName + ".submit()")
					+ "><span>QuickAdd</span></a>");
			appendString("</form>");
			appendString("</td>");
			appendString("</tr>");
		}
	}

	private void generateEditorJS(MetaDocument doc, MetaListProperty list, String elName) {
		MetaProperty p = list.getContainedProperty();
		if (!(p.isLinked() || p instanceof MetaEnumerationProperty))
			return;

		appendString("<script type=\"text/javascript\" src=" + quote(getCurrentYUIPath("core/build/yahoo-dom-event/yahoo-dom-event.js")) + "></script>");
		appendString("<script type=\"text/javascript\" src=" + quote(getCurrentYUIPath("core/build/container/container-min.js")) + "></script>");
		appendString("<script type=\"text/javascript\" src=" + quote(getCurrentYUIPath("core/build/element/element-min.js")) + "></script>");
		appendString("<script type=\"text/javascript\" src=" + quote(getCurrentYUIPath("core/build/datasource/datasource-min.js")) + "></script>");
		appendString("<script type=\"text/javascript\" src=" + quote(getCurrentYUIPath("core/build/autocomplete/autocomplete-min.js")) + "></script>");
		appendString("<script type=\"text/javascript\" src=" + quote(getCurrentYUIPath("anoweb/widget/ComboBox.js")) + "></script>");
		appendString("<script type=\"text/javascript\">");
		increaseIdent();

		appendString("//Initializing items for " + elName);
		appendString("var " + elName + "Json = {items:[");
		appendString("<logic:iterate id=\"item\" name=\"" + elName + "\" type=\"net.anotheria.webutils.bean.LabelValueBean\">");
		increaseIdent();
		appendString("{id:\"<bean:write name=\"item\" property=\"value\" filter=\"true\"/>\",name:\"<bean:write name=\"item\" property=\"label\" filter=\"true\"/>\"},");
		// appendString("{id:\"${item.value}\",name:\"${item.label}\"},");
		decreaseIdent();
		appendString("</logic:iterate>");
		appendString("]};");
		String propertyName = list.getContainedProperty().getName();
		appendString("new YAHOO.anoweb.widget.ComboBox(" + quote(propertyName) + ",\"" + propertyName + "Selector\"," + elName + "Json, {id:'',name:'none'});");

		decreaseIdent();
		appendString("</script>");

	}

}
