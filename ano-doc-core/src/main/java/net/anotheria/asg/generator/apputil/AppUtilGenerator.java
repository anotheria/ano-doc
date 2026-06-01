package net.anotheria.asg.generator.apputil;

import net.anotheria.asg.generator.AbstractAnoDocGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.meta.MetaModule;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>AppUtilGenerator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class AppUtilGenerator extends AbstractAnoDocGenerator{
	
	private Context context;
	
	/**
	 * <p>Constructor for AppUtilGenerator.</p>
	 *
	 * @param aContext a {@link net.anotheria.asg.generator.Context} object.
	 */
	public AppUtilGenerator(Context aContext){
		context = aContext;
	}
	
	/**
	 * <p>generate.</p>
	 *
	 * @param modules a {@link java.util.List} object.
	 */
	public void generate(List<MetaModule> modules){
		List<FileEntry> files = new ArrayList<FileEntry>();
		files.addAll(new CallContextGenerator().generate(null));
		files.addAll(new XMLExporterGenerator().generate(modules));
		files.addAll(new XMLImporterGenerator().generate(modules, context));
		files.addAll(new LanguageUtilsGenerator().generate(modules, context));
		files.addAll(new BasicServiceUtilGenerator().generate(modules));
		//files.addAll(new Log4JConfigurationGenerator().generate(null));
		
		
		writeFiles(files);
	}
}
