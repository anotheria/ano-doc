package net.anotheria.asg.generator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.anotheria.asg.generator.util.FileWriter;


/**
 * Base class for generators.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class AbstractAnoDocGenerator {
	/**
	 * <p>runGenerator.</p>
	 *
	 * @param generator a {@link net.anotheria.asg.generator.IGenerator} object.
	 * @param target a {@link net.anotheria.asg.generator.IGenerateable} object.
	 * @param context a {@link net.anotheria.asg.generator.Context} object.
	 * @param results a {@link java.util.List} object.
	 */
	protected void runGenerator(IGenerator generator, IGenerateable target, Context context, List<FileEntry> results){
		List<FileEntry> tmp = generator.generate(target);
		for (Iterator<FileEntry> it = tmp.iterator(); it.hasNext(); )
			results.add(it.next());
		
	}
	
	/**
	 * <p>writeFiles.</p>
	 *
	 * @param entries a {@link java.util.List} object.
	 */
	protected void writeFiles(List<FileEntry> entries){
		Map<String, String> writtenFiles = new HashMap<String, String>();
		for (int i=0; i<entries.size(); i++){
			FileEntry e = (FileEntry)entries.get(i);
			warnOnNameClash(writtenFiles, e);
			FileWriter.writeFile(e.getPath(), e.getName()+e.getType(), e.getContent());
		}
		
	}
	
	/**
	 * Complains if two different generated artefacts want to be written into the same file. The second one
	 * wins and the first one is silently lost, which at runtime shows up as an action forwarding to a page
	 * which doesn't belong to it. Such a name clash is a bug in the definitions (or in the generator) and is
	 * hard to find later on, therefore we make some noise here. Artefacts which are generated more than once
	 * with identical content (shared pages for example) are fine and remain unmentioned.
	 *
	 * @param writtenFiles content of the files written so far, keyed by their full name.
	 * @param entry the entry which is about to be written.
	 */
	private void warnOnNameClash(Map<String, String> writtenFiles, FileEntry entry){
		//empty entries are skipped by the FileWriter as well, they are the result of a generator which had nothing to do.
		if (entry.getContent()==null || entry.getContent().length()==0)
			return;
		String fullName = entry.getPath()+"/"+entry.getName()+entry.getType();
		String previousContent = writtenFiles.put(fullName, entry.getContent());
		if (previousContent==null || previousContent.equals(entry.getContent()))
			return;
		System.err.println("**************************************************************************");
		System.err.println("*** NAME CLASH: "+fullName);
		System.err.println("*** is generated twice with different content, the first version is lost.");
		System.err.println("*** Rename one of the documents, containers or properties involved.");
		System.err.println("**************************************************************************");
	}
}
