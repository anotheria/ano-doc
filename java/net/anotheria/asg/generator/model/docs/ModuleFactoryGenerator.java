package net.anotheria.asg.generator.model.docs;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.CommentGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public class ModuleFactoryGenerator extends AbstractGenerator implements IGenerator{
	
	
	public List<FileEntry> generate(IGenerateable gmodule, Context context){
		
		MetaModule mod = (MetaModule)gmodule;
		
		List<FileEntry> ret = new ArrayList<FileEntry>();
		
		ret.add(generateFactory(mod, context));
		
		return ret;
	}
	
	public static String getModuleFactoryName(MetaModule module){
		return module.getFactoryClassName();
	}
	
	private FileEntry generateFactory(MetaModule module, Context context){
		
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
		clazz.setName(module.getFactoryClassName());
		clazz.setParent("AbstractModuleFactory");
		clazz.setPackageName(context.getPackageName(module)+".data");
	
		clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getModuleFactoryName(module), "The Factory for the "+module.getName()+" objects."));

		clazz.addImport("net.anotheria.anodoc.data.Module");
		clazz.addImport("net.anotheria.anodoc.data.Document");
		clazz.addImport("net.anotheria.anodoc.data.DataHolder");
		clazz.addImport("net.anotheria.anodoc.service.AbstractModuleFactory");

		startClassBody();
		generateModuleCreator(module);
		emptyline();
		generateSecondLevelDocumentCreator(module);
		emptyline();
		
		return new FileEntry(clazz);
	}
	
	private void generateModuleCreator(MetaModule module){
		appendString("public Module recreateModule(String ownerId, String copyId) {");
		increaseIdent();
		appendStatement("return new Module"+module.getName()+"()");
		append(closeBlock());
		
	}
	
	private void generateSecondLevelDocumentCreator(MetaModule module){
		appendString("public Document createDocument(String id, DataHolder context) {");
		increaseIdent();
		List<MetaDocument> docs = module.getDocuments();
		for (int i=0; i<docs.size(); i++){
			MetaDocument doc = docs.get(i);
			appendString("if (context.getId().equals("+module.getModuleClassName()+"."+doc.getListName()+"))");
			increaseIdent();
			appendStatement("return new "+DocumentGenerator.getDocumentName(doc)+"(id)");
			decreaseIdent();
		}
		
		appendStatement("throw new RuntimeException(\"Unexpected document in list:\"+context.getId())");
		append(closeBlock());
		
	}
}
