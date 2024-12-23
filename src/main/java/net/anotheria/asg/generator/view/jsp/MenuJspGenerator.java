package net.anotheria.asg.generator.view.jsp;

import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedJSPFile;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator.SharedAction;
import net.anotheria.asg.generator.view.meta.MetaView;

import java.util.List;

/**
 * <p>MenuJspGenerator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class MenuJspGenerator extends AbstractJSPGenerator {

	/**
	 * <p>generate.</p>
	 *
	 * @param views a {@link java.util.List} object.
	 * @param context a {@link net.anotheria.asg.generator.Context} object.
	 * @return a {@link net.anotheria.asg.generator.FileEntry} object.
	 */
	public FileEntry generate(List<MetaView> views , Context context) {
		
		FileEntry menu = new FileEntry(generateMenu(views, context));
		return menu;
		
	}
	
	/**
	 * <p>getMenuName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getMenuName(){
		return "MenuMaf";
	}
	
	/**
	 * <p>getMenuPageName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
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
				appendString("<img class=\"logo\" src=\"../cms_static/img/${currentSystem eq 'PROD' ? 'logo2.gif' : 'logo.gif'}\" alt=\"CMS Logo\"/>");
				appendString("<ano:notEmpty name=\"currentApplication\">");
					increaseIdent();
					appendString("<div class=\"currentApplication\">App: ${currentApplication}</div>");
					decreaseIdent();
				appendString(" </ano:notEmpty>");
				appendString("<div class=\"currentSystem\">Environment: ${currentSystem eq 'PROD' ? '<font color=\"red\">PROD</font>' : currentSystem}</div>");
				appendString("<ano:notEqual name=\"disabledSearchFlag\" value=\"true\">");
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
							appendString("<li><input type=\"radio\" id=\"r1\" value=\"view\" <ano:equal name=\"searchScope\" value=\"view\">CHECKED</ano:equal> name=\"searchArea\"/><label for=\"r1\">Current page</label></li>");
							appendString("<li><input type=\"radio\" id=\"r2\" value=\"section\" <ano:equal name=\"searchScope\" value=\"section\">CHECKED</ano:equal> name=\"searchArea\"/><label for=\"r2\">Current section</label></li>");
						decreaseIdent();
						appendString("</ul>");
						appendString("<input type=\"hidden\" value=\"<ano:write name=\"moduleName\" />\" name=\"module\"/>");
						appendString("<input type=\"hidden\" value=\"<ano:write name=\"documentName\" />\" name=\"document\"/>");
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
			appendString("</ano:notEqual>");
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
								appendString("<ano:iterate name=\"mainNavigation\" type=\"net.anotheria.asg.util.bean.NavigationItemBean\" id=\"NaviItem\">");
									increaseIdent();
									appendString("<ano:equal name=\"NaviItem\" property=\"active\" value=\"true\">");
										increaseIdent();
										appendString("<li class=\"opened\"><a><ano:write name=\"NaviItem\" property=\"caption\"/></a>");
											increaseIdent();
											appendString("<ul>");
												increaseIdent();
												appendString("<ano:iterate id=\"subNaviItem\" name=\"NaviItem\" property=\"subNavi\">");
												appendString("<li><a <ano:equal name=\"subNaviItem\" property=\"active\" value=\"true\"> class=\"active\" href=\"<ano:write name=\"subNaviItem\" property=\"link\"/>\"</ano:equal><ano:notEqual name=\"subNaviItem\" property=\"active\" value=\"true\">href=\"<ano:write name=\"subNaviItem\" property=\"link\"/>\"</ano:notEqual>><ano:write name=\"subNaviItem\" property=\"caption\"/></a></li>");
												appendString("</ano:iterate>");
											decreaseIdent();		
											appendString("</ul>");
										decreaseIdent();	
										appendString("</li>");
									decreaseIdent();
									appendString("</ano:equal>");
									appendString("<ano:notEqual name=\"NaviItem\" property=\"active\" value=\"true\">");
									appendString("<li><a href=\"<ano:tslink><ano:write name=\"NaviItem\" property=\"link\"/></ano:tslink>\"><ano:write name=\"NaviItem\" property=\"caption\"/></a></li>");
									appendString("</ano:notEqual>");
								decreaseIdent();
								appendString("</ano:iterate>");
							decreaseIdent();
							appendString("</ul>");

                            appendString("<a href=\"<ano:tslink>changePass</ano:tslink>\" class=\"change_pass\">Change password</a>");
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
