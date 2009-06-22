package net.anotheria.asg.generator.apputil;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedXMLFile;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.util.StringUtils;

public class Log4JConfigurationGenerator extends AbstractGenerator implements IGenerator {

	private static final String categories[] = new String[]{
		"debug","info","warn","error", "fatal"
	};
	
	public static final String GENERATED_PACKAGE = "net.anotheria.anosite.gen";
	
	public List<FileEntry> generate(IGenerateable g, Context context) {
		List<FileEntry> files = new ArrayList<FileEntry>();
		
		GeneratedXMLFile log4Config = generateContent(context);
		log4Config.setPath("/etc/appdata");
		
		FileEntry entry = new FileEntry(log4Config);
		entry.setType(".xml");
		files.add(entry);
	
		return files;
	}
	
	private GeneratedXMLFile generateContent(Context context){
	
		GeneratedXMLFile artefact = new GeneratedXMLFile("log4j", "UTF-8");
		startNewJob(artefact);
		
		appendString("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">");
		
		appendString("<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/'>");
		increaseIdent();
		emptyline();
		
		appendString("<!-- File Appenders for generated code -->");
		emptyline();
		
		for (String cat : categories){
		  	appendString("<!-- File Appender for ", cat, " and higher -->");
		  	appendString("<appender name=", quote(StringUtils.capitalize(cat) + "GenAppender"), " class="+quote("org.apache.log4j.RollingFileAppender"),">");
		  	increaseIdent();
		  	appendString("<param name=", quote("File"), " value=", quote("logs/"+context.getApplicationName()+"-gen-"+cat+".log"), " />");
		  	appendString("<param name=", quote("Threshold"), " value=", quote(cat.toUpperCase()), " />");
		  	appendString("<param name=", quote("MaxFileSize"), " value=",quote("100MB"), " />  ");     
		  	appendString("<param name=", quote("MaxBackupIndex"), " value=", quote(5), " />");
		  	appendString("<layout class=", quote("org.apache.log4j.PatternLayout"), ">");
		  	increaseIdent();
		  	appendString("<param name=", quote("ConversionPattern"), " value=", quote("%r %d{ISO8601} %-5p %c - %m%n"), "/>");
		  	decreaseIdent();
		  	appendString("</layout>");
			decreaseIdent();
		  	appendString("</appender>");
		}
		emptyline();
		
		appendString("<!-- File Appenders for written code -->");
		emptyline();

		for (String cat : categories){
		  	appendString("<!-- File Appender for ", cat, " and higher -->");
		  	appendString("<appender name=", quote(StringUtils.capitalize(cat) + "Appender"), " class="+quote("org.apache.log4j.RollingFileAppender"),">");
		  	increaseIdent();
		  	appendString("<param name=", quote("File"), " value=", quote("logs/"+context.getApplicationName()+"-"+cat+".log"), " />");
		  	appendString("<param name=", quote("Threshold"), " value=", quote(cat.toUpperCase()), " />");
		  	appendString("<param name=", quote("MaxFileSize"), " value=",quote("100MB"), " />  ");     
		  	appendString("<param name=", quote("MaxBackupIndex"), " value=", quote(5), " />");
		  	appendString("<layout class=", quote("org.apache.log4j.PatternLayout"), ">");
		  	increaseIdent();
		  	appendString("<param name=", quote("ConversionPattern"), " value=", quote("%r %d{ISO8601} %-5p %c - %m%n"), "/>");
		  	decreaseIdent();
		  	appendString("</layout>");
			decreaseIdent();
		  	appendString("</appender>");
		}
		emptyline();
		
		appendString("<!-- Console appender -->");
		appendString("<appender name="+quote("ConsoleAppender"), " class=", quote("org.apache.log4j.ConsoleAppender"),">");
		increaseIdent();
		appendString("<param name="+quote("Threshold"), " value="+quote("WARN"), " />");
		appendString("<layout class="+quote("org.apache.log4j.PatternLayout"), ">");
		increaseIdent();
		appendString("<param name="+quote("ConversionPattern")," value=", quote("%r %d{ISO8601} %-5p %c - %m%n"), "/>");
		decreaseIdent();
		appendString("</layout>");
		decreaseIdent();
		appendString("</appender>");
		emptyline();

		appendString("<logger name=", quote(context.getTopPackageName()), " additivity=", quote("false"), ">");
		increaseIdent(); 
		appendString("<level value=", quote("INFO"), "/>");
		for (String cat : categories)
			appendString("<appender-ref ref=",quote(StringUtils.capitalize(cat)+"GenAppender"), "/>");
		decreaseIdent();
		appendString("</logger>");
		emptyline();
		
		appendString("<logger name=", quote("net.anotheria."+context.getApplicationName().toLowerCase()), " additivity=", quote("false"), ">");
		increaseIdent(); 
		appendString("<level value=", quote("INFO"), "/>");
		for (String cat : categories)
			appendString("<appender-ref ref=",quote(StringUtils.capitalize(cat)+"Appender"), "/>");
		decreaseIdent();
		appendString("</logger>");
		emptyline();

		decreaseIdent();
		appendString("</log4j:configuration>");
		return artefact;
	}
}