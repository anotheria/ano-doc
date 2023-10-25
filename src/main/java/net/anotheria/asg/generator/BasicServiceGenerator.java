package net.anotheria.asg.generator;

import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.model.ServiceGenerator;
import net.anotheria.asg.service.AbstractASGService;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO Please remain lrosenberg to comment AbstractServiceGenerator.java
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class BasicServiceGenerator extends AbstractGenerator{
	
	
	/**
	 * <p>generate.</p>
	 *
	 * @param modules a {@link java.util.List} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<FileEntry> generate(List<MetaModule>  modules){
		List<FileEntry> ret = new ArrayList<FileEntry>(); 
		
		ret.add(new FileEntry(generateBasicService(modules)));
		ret.add(new FileEntry(generateBasicCMSService(modules)));
		
		return ret;
	}
	
	private GeneratedClass generateBasicService(List<MetaModule> modules){
		
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
		clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".service");
		
		clazz.addImport("org.slf4j.Logger");
		clazz.addImport("org.slf4j.LoggerFactory");
		clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactory");
		clazz.addImport("net.anotheria.anoprise.metafactory.Extension");
		clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactoryException");
		clazz.addImport("org.slf4j.Logger");
		clazz.addImport("org.slf4j.LoggerFactory");
		clazz.addImport("org.slf4j.MarkerFactory");
		clazz.addImport("org.codehaus.jettison.json.JSONObject");
		clazz.addImport("net.anotheria.anosite.gen.shared.util.ModuleName");
		clazz.addImport("net.anotheria.anosite.gen.shared.util.DocumentName");
		clazz.addImport("net.anotheria.asg.exception.ASGRuntimeException");
		for(MetaModule m: modules) {
			clazz.addImport(ServiceGenerator.getInterfaceImport(m));
		}

		clazz.addImport(AbstractASGService.class);

		clazz.setAbstractClass(true);
		clazz.setName("BasicService");
		clazz.setParent(AbstractASGService.class);
		
		startClassBody();

		appendStatement("protected Logger log");
		emptyline();
		appendStatement("private static Object serviceInstantiationLock = new Object()");
		emptyline();
		appendCommentLine("CMS instances of different services.");
		for (MetaModule m:modules) {
			appendStatement("private static volatile " + ServiceGenerator.getInterfaceName(m) + " " + getServiceInstance(m));
		}
		emptyline();

        //generate constructor
        appendString("protected BasicService(){");
        increaseIdent();
        appendStatement("log = LoggerFactory.getLogger(\"AnoSiteLog\")");
        closeBlockNEW();
        emptyline();

		for (MetaModule m: modules) {
			appendString("protected " + ServiceGenerator.getInterfaceName(m) + " " + getServiceGetterCall(m) + "{");
			increaseIdent();
			appendString("if (" +getServiceInstance(m) + " == null){");
			increaseIdent();
			appendString("synchronized(serviceInstantiationLock){");
			increaseIdent();
			appendString("if (" + getServiceInstance(m) + " == null){");
			increaseIdent();
			appendString("try{");
			appendIncreasedStatement(getServiceInstance(m) + " = MetaFactory.get(" + ServiceGenerator.getInterfaceName(m) + ".class, Extension.EDITORINTERFACE)");
			appendString("}catch(MetaFactoryException e){");
			appendIncreasedStatement("log.error(MarkerFactory.getMarker(\"FATAL\"), " + quote("Can't load editor instance of module service " + m.getName()) + ", e)");
			appendString("}");
			closeBlockNEW();
			closeBlockNEW();
			closeBlockNEW();

			appendStatement("return " + getServiceInstance(m));
			closeBlockNEW();
			emptyline();
		}

		return clazz;
	}

	private GeneratedClass generateBasicCMSService(List<MetaModule> modules){
		
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
		clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".service");

		clazz.addImport("net.anotheria.anodoc.data.Module");
		clazz.addImport("net.anotheria.anodoc.service.IModuleService");
		clazz.addImport("net.anotheria.anodoc.service.ModuleServiceFactory");
		clazz.addImport("net.anotheria.asg.util.listener.IModuleListener");

		clazz.setName("BasicCMSService");
		clazz.setParent("BasicService");
		clazz.setAbstractClass(true);

		startClassBody();
		
		appendStatement("public static final String MY_OWNER_ID = "+quote(GeneratorDataRegistry.getInstance().getContext().getOwner()));
		appendStatement("protected IModuleService service");
		emptyline();

		appendString("static{");
		increaseIdent();
		appendString("AnoDocConfigurator.configure();");
        closeBlockNEW();
        emptyline();
        
        //generate constructor
        appendString("protected BasicCMSService(){");
        increaseIdent();
        appendStatement("service = ModuleServiceFactory.createModuleService()");
        closeBlockNEW();
        emptyline();
        
        //generate update method.
        appendString("protected void updateModule(Module mod){");
        increaseIdent();
        appendString("try{");
        appendString("service.storeModule(mod);");
        appendString("}catch(Exception e){");
        increaseIdent();
        appendString("log.error(\"updateModule\", e);");
        closeBlockNEW();
        closeBlockNEW();
		emptyline();

        //generate method for adding module listener.
        appendString("protected void addModuleListener(String moduleId, IModuleListener moduleListener){");
        increaseIdent();
        appendString("service.addModuleListener(moduleId, MY_OWNER_ID, moduleListener);");
        closeBlockNEW();
    	emptyline();

        appendString("protected Module getModule(String moduleId){");
        increaseIdent();
        appendString("try{");
        appendString("return service.getModule(MY_OWNER_ID, moduleId, true);");
        appendString("}catch(Exception e){");
        increaseIdent();
        appendString("log.error(\"getModule\", e);");
        closeBlockNEW();
        appendStatement("return null");
        closeBlockNEW();
        emptyline();
        
		return clazz;
	}

	private String getServiceGetterCall(MetaModule module) {
		return "get" + module.getName() + "Service()";
	}

	private String getServiceInstance(MetaModule module) {
		return module.getName().toLowerCase() + "Service";
	}
}
