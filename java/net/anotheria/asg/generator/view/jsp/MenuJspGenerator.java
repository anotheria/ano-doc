package net.anotheria.asg.generator.view.jsp;

import java.util.Collection;
import java.util.List;

import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedJSPFile;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator.SectionAction;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator.SharedAction;
import net.anotheria.asg.generator.view.meta.MetaModuleSection;
import net.anotheria.asg.generator.view.meta.MetaView;

public class MenuJspGenerator extends AbstractJSPGenerator {

	public FileEntry generate(List<MetaView> views , Context context) {
		
		FileEntry menu = new FileEntry(generateMenu(views, context));
		return menu;
		
	}
	
	public static final String getMenuName(){
		return "MenuMaf";
	}
	
	public static final String getMenuPageName(){
		return getMenuName()+".jsp";
	}
	
	private GeneratedJSPFile generateMenu(List<MetaView> views, Context context){
		
		GeneratedJSPFile jsp = new GeneratedJSPFile();
		startNewJob(jsp);
		jsp.setName(getMenuName());
		jsp.setPackage(context.getPackageName(MetaModule.SHARED)+".jsp");
		append(getBaseJSPHeader());
		appendString("<!--  generated by JspMafMenuGenerator.generateMenu -->");			
		appendString("<!--  dynamic maf menu start -->");
		appendString("<div class=\"left\">");
			increaseIdent();
			appendString("<div class= \"scroll_left\">");
				increaseIdent();
				appendString("<img class=\"logo\" src=\"../cms_static/img/logo.gif\" alt=\"CMS Logo\"/>");
				appendString("<logic:notEqual name=\"disabledSearchFlag\" value=\"true\">");
				appendString("<form name=\"Search\" action="+quote(SharedAction.SEARCH.getMappingName())+">");
					increaseIdent();
					appendString("<input class=\"search\" name=\"criteria\" type=\"text\" value=\"Search...\" />");
					appendString("<a href=\"#\" class=\"adv_search \">Advanced search</a>");
					appendString("<div class=\"clear\"><!-- --></div>");
					appendString("<div class=\"open adv_s_open\">");
					increaseIdent();
						appendString("<div class=\"top\">");
							increaseIdent();
							appendString("<div><!-- --></div>");
						decreaseIdent();
						appendString("</div>");
					appendString("<div class=\"in\">");
						increaseIdent();
						appendString("<span class=\"f_14\">Search</span>");
						appendString("<ul>");
							increaseIdent();	
							appendString("<li><input type=\"radio\" id=\"r1\" value=\"view\" <logic:equal name=\"searchScope\" value=\"view\">CHECKED</logic:equal> name=\"searchArea\"/><label for=\"r1\">Current page</label></li>");
							appendString("<li><input type=\"radio\" id=\"r2\" value=\"section\" <logic:equal name=\"searchScope\" value=\"section\">CHECKED</logic:equal> name=\"searchArea\"/><label for=\"r2\">Current section</label></li>");
						decreaseIdent();
						appendString("<ul>");
						appendString("<input type=\"hidden\" value=\"<bean:write name=\"moduleName\" />\" name=\"module\"/>");
						appendString("<input type=\"hidden\" value=\"<bean:write name=\"documentName\" />\" name=\"document\"/>");
						appendString("<a href=\"#\" class=\"button\" onClick=\"document.Search.submit();return false\"><span>Search</span></a>");
					decreaseIdent();
					appendString("</div>");
					appendString("<div class=\"bot\">");
					appendString("<div><!-- --></div>");
					appendString("</div>");
				decreaseIdent();
				appendString("</div>");
			decreaseIdent();
			appendString("</form>");
			appendString("<a href=\"#\" class=\"lang_open\">Languages</a>");
			appendString("</logic:notEqual>");
				appendString("<div class=\"clear\"><!-- --></div>");
				appendString("<div class=\"open lang_s_open\">");
							increaseIdent();
							appendString("<div class=\"top\">");
								increaseIdent();
								appendString("<div><!-- --></div>");
							decreaseIdent();
							appendString("</div>");
							appendString("<div class=\"in\">");
							appendString("<input type=\"checkbox\" id=\"all_check\" class=\"all_check\"/><label for=\"c1\">Select All</label>");
							appendString("<div class=\"clear\"><!-- --></div>");
							appendString("<ul>");
									increaseIdent();
									for (String sl : GeneratorDataRegistry.getInstance().getContext().getLanguages()){
										appendString("<li>");
										appendString("<input type=\"checkbox\" id=\"lang_"+sl+"\"/><label for=\"lang_"+sl+"\">"+sl+"</label>");
										appendString("</li>");
									}
								decreaseIdent();
								appendString("</ul>");
								appendString("<a href=\"#\" class=\"button\"><span>Apply</span></a>");
								appendString("<div class=\"clear\"><!-- --></div>");
								appendString("</div>");
								appendString("<div class=\"bot\">");
									appendIncreasedString("<div><!-- --></div>");
								appendString("</div>");
						decreaseIdent();
						appendString("</div>");
								
							appendString("<ul class=\"main_navigation\">");
								increaseIdent();
								appendString("<logic:iterate name=\"mainNavigation\" type=\"net.anotheria.webutils.bean.NavigationItemBean\" id=\"NaviItem\">");
									increaseIdent();
									appendString("<logic:equal name=\"NaviItem\" property=\"active\" value=\"true\">");
										increaseIdent();
										appendString("<li class=\"opened\"><a><bean:write name=\"NaviItem\" property=\"caption\"/></a>");
											increaseIdent();
											appendString("<ul>");
												increaseIdent();
												appendString("<logic:iterate id=\"subNaviItem\" name=\"NaviItem\" property=\"subNavi\">");
												appendString("<li><a <logic:equal name=\"subNaviItem\" property=\"active\" value=\"true\"> class=\"active\" href=\"<bean:write name=\"subNaviItem\" property=\"link\"/>\"</logic:equal><logic:notEqual name=\"subNaviItem\" property=\"active\" value=\"true\">href=\"<bean:write name=\"subNaviItem\" property=\"link\"/>\"</logic:notEqual>><bean:write name=\"subNaviItem\" property=\"caption\"/></a></li>");
												appendString("</logic:iterate>");
											decreaseIdent();		
											appendString("</ul>");
										decreaseIdent();	
										appendString("</li>");
									decreaseIdent();
									appendString("</logic:equal>");
									appendString("<logic:notEqual name=\"NaviItem\" property=\"active\" value=\"true\">");
									appendString("<li><a href=\"<ano:tslink><bean:write name=\"NaviItem\" property=\"link\"/></ano:tslink>\"><bean:write name=\"NaviItem\" property=\"caption\"/></a></li>");
									appendString("</logic:notEqual>");
								decreaseIdent();
								appendString("</logic:iterate>");
							decreaseIdent();
							appendString("</ul>");
							
							appendString("<a href=\"<ano:tslink>logout</ano:tslink>\" class=\"logout\">Logout</a>");
							appendString("<div class=\"clear\"><!-- --></div>");
							appendString("<a href=\"http://www.anotheria.net\" class=\"powered\"><img src=\"../cms_static/img/powered_conf.gif\" alt=\"\"/></a>");
						
						decreaseIdent();		
						appendString("</div>");
						
					decreaseIdent();
					appendString("</div>");
		appendString("<!-- / dynamic maf menu end-->");
		appendString("<!-- / generated by JspMafMenuGenerator.generateMenu -->");	

		return jsp;
	}
}