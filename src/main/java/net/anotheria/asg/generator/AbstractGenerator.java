package net.anotheria.asg.generator;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.view.meta.MetaFieldElement;
import net.anotheria.asg.generator.view.meta.MetaViewElement;
import net.anotheria.asg.generator.view.meta.MultilingualFieldElement;
import net.anotheria.util.StringUtils;


/**
 * TODO please remined another to comment this class.
 *
 * @author another
 * @version $Id: $Id
 */
public class AbstractGenerator{

	/**
	 * Quotes a string with double quotes &quot;.
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String quote(String s){
		return "\""+s+"\"";
	}

	/**
	 * Quotes a string with double quotes &quot;.
	 *
	 * @param s a {@link java.lang.StringBuilder} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String quote(StringBuilder s){
		return "\""+s.toString()+"\"";
	}
	
	/**
	 * Quotes the string representation of the integer parameter with double quotes &quot;.
	 *
	 * @param a a int.
	 * @return a {@link java.lang.String} object.
	 */
	protected String quote(int a){
		return quote(""+a);
	}

	/**
	 * Returns a line with increased ident and the parameter string.
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeIncreasedString(String s){
		increaseIdent();
		String ret = writeString(s);
		decreaseIdent();
		return ret;
	}
	
	/**
	 * Adds all string parameters after each other to the current target StringBuilder with an increased ident.
	 *
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendIncreasedString(String... strings){
		appendIncreasedString(getCurrentJobContent(), strings);
	}
		
	/**
	 * Adds all string parameters after each other to the given target StringBuilder with an increased ident.
	 *
	 * @param target a {@link java.lang.StringBuilder} object.
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendIncreasedString(StringBuilder target, String... strings){
		increaseIdent();
		appendString(target, strings);
		decreaseIdent();
	}

	/**
	 * <p>writeIncreasedStatement.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeIncreasedStatement(String s){
		return writeIncreasedString(s+";");
	}
	
	/**
	 * <p>appendIncreasedStatement.</p>
	 *
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendIncreasedStatement(String... strings){
		appendIncreasedStatement(getCurrentJobContent(), strings);
	}
	
	/**
	 * <p>appendIncreasedStatement.</p>
	 *
	 * @param target a {@link java.lang.StringBuilder} object.
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendIncreasedStatement(StringBuilder target, String... strings){
		increaseIdent();
		appendStatement(target, strings);
		decreaseIdent();
	}

	/**
	 * Constant for line break.
	 */
	public static final String CRLF = "\n";
	
	/**
	 * Current ident.
	 */
	protected int ident = 0;

	/**
	 * Writes a string in a new line with ident and linefeed.
	 *
	 * @param s string to write.
	 * @deprecated use appendString instead
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeString(String s){
		String ret = getIdent();
		ret += s;
		ret += CRLF;
		return ret; 
	}
	
	/**
	 * <p>appendString.</p>
	 *
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendString(String... strings){
		appendString(getCurrentJobContent(), strings);
	}

	/**
	 * <p>appendString.</p>
	 *
	 * @param target a {@link java.lang.StringBuilder} object.
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendString(StringBuilder target, String... strings){
		appendIdent(target);
		for (String s : strings)
			target.append(s);
		target.append(CRLF);
	}

	//later replace with openTry
	/**
	 * <p>openTry.</p>
	 */
	protected void openTry(){
		appendString("try {");
		increaseIdent();
	}

	/**
	 * <p>appendCatch.</p>
	 *
	 * @param exceptionClazz a {@link java.lang.Class} object.
	 */
	protected void appendCatch(Class<? extends Throwable> exceptionClazz){
		((GeneratedClass)getCurrentJob()).addImport(exceptionClazz);
		appendCatch(exceptionClazz.getName());
	}
	
	/**
	 * <p>appendCatch.</p>
	 *
	 * @param exceptionName a {@link java.lang.String} object.
	 */
	protected void appendCatch(String exceptionName){
		decreaseIdent();
		appendString("}catch(", exceptionName, " e){");
		increaseIdent();
	}
	
	/**
	 * <p>openFun.</p>
	 *
	 * @param s a {@link java.lang.String} object.
	 */
	protected void openFun(String s){
		if (!s.endsWith("{"))
			s+=" {";
		appendString(s);
		increaseIdent();
	}
	
	/**
	 * <p>appendNullCheck.</p>
	 *
	 * @param aArgName a {@link java.lang.String} object.
	 * @param aExceptionMessage a {@link java.lang.String} object.
	 */
	protected void appendNullCheck(String aArgName, String aExceptionMessage){
		((GeneratedClass)getCurrentJob()).addImport(IllegalArgumentException.class);
		appendString("if(" + aArgName + " == null)");
		increaseIdent();
		appendString("throw new IllegalArgumentException(\"" + aExceptionMessage + "\");");
		decreaseIdent();		
	}

	
	/**
	 * Writes a statement (';' at the end of the line).
	 *
	 * @param s statement to write.
	 * @deprecated use appendStatement instead
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeStatement(String s){
		String ret = getIdent();
		ret += s;
		ret += ";";
		ret += CRLF;
		return ret; 
	}

	/**
	 * <p>append.</p>
	 *
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void append(String... strings){
		StringBuilder target = getCurrentJobContent();
		for (String s: strings)
			target.append(s);
	}
	
	/**
	 * <p>appendStatement.</p>
	 *
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendStatement(String... strings){
		appendStatement(getCurrentJobContent(), strings);
	}
	
	/**
	 * <p>appendStatement.</p>
	 *
	 * @param target a {@link java.lang.StringBuilder} object.
	 * @param strings a {@link java.lang.String} object.
	 */
	protected void appendStatement(StringBuilder target, String... strings){
		appendIdent(target);
		for (String s : strings)
			target.append(s);
		target.append(';');
		target.append(CRLF);
	}

	
	private void appendIdent(StringBuilder target){
		for (int i=0; i<ident; i++)
			target.append('\t');
	}
	
	/**
	 * Returns current ident as string.
	 * @return a string with "\t"s.
	 */
	private String getIdent(){
		StringBuilder ret = new StringBuilder();
		for (int i=0; i<ident; i++)
			ret.append("\t");
		return ret.toString();
	}
	
	/**
	 * increases current ident.
	 */
	protected void increaseIdent(){
		ident++;
	}
	
	/**
	 * decreases current ident.
	 */
	protected void  decreaseIdent(){
		ident--;
		if (ident<0)
			ident = 0;
	}
	
	/**
	 * <p>resetIdent.</p>
	 */
	protected void resetIdent(){
	    ident = 0;
	}
	
	/**
	 * Returns an empty line.
	 *
	 * @deprecated use emptyline.
	 * @return a {@link java.lang.String} object.
	 */
	public static String writeEmptyline(){
		return CRLF;
	}

	/**
	 * Appends an empty line.
	 */
	public static void emptyline(){
		getCurrentJobContent().append(CRLF);
	}
	
	/**
	 * <p>emptyline.</p>
	 *
	 * @param b a {@link java.lang.StringBuilder} object.
	 */
	protected static void emptyline(StringBuilder b){
		b.append(CRLF);
	}

	/**
	 * <p>appendEmptyline.</p>
	 *
	 * @deprecated use emptyline instead
	 */
	protected static void appendEmptyline(){
		emptyline(getCurrentJobContent());
	}

	/**
	 * <p>writeImport.</p>
	 *
	 * @deprecated use clazz.addImport(imp) instead
	 * @param imp a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeImport(String imp){
		return writeString("import "+imp+";");
	}

	/**
	 * <p>appendImport.</p>
	 *
	 * @deprecated use clazz.addImport(imp) instead
	 * @param imp a {@link java.lang.String} object.
	 */
	protected void appendImport(String imp){
		appendString(getCurrentJobContent(), "import ", imp, ";");
	}

	/**
	 * <p>appendImport.</p>
	 *
	 * @deprecated use clazz.addImport(imp) instead
	 * @param target a {@link java.lang.StringBuilder} object.
	 * @param imp a {@link java.lang.String} object.
	 */
	protected void appendImport(StringBuilder target, String imp){
		appendString(target, "import ", imp, ";");
	}

	/**
	 * <p>writeImport.</p>
	 *
	 * @deprecated use clazz.addImport(imp) instead
	 * @param packagename a {@link java.lang.String} object.
	 * @param classname a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeImport(String packagename, String classname){
		return writeString("import "+packagename+"."+classname+";");
	}

	/**
	 * <p>closeBlock.</p>
	 *
	 * @param b a {@link java.lang.StringBuilder} object.
	 */
	protected void closeBlock(StringBuilder b){
		decreaseIdent();
		b.append(writeString("}"));
	}
	
	/**
	 * <p>closeBlock.</p>
	 *
	 * @deprecated use closeBlock(String) or closeBlockNEW instead
	 * @return a {@link java.lang.String} object.
	 */
	@Deprecated
	protected String closeBlock(){
		decreaseIdent();
		String ret = writeString("}");
		return ret;
	}

	/**
	 * <p>closeBlockNEW.</p>
	 */
	protected void closeBlockNEW(){
		decreaseIdent();
		appendString("}");
	}

	/**
	 * Generates ident decreasing and block closing. Appends message just after block
	 *
	 * @param message comment message to block closing. Usually block/method name.
	 */
	protected void closeBlock(String message){
		decreaseIdent();
		appendString("} //"+message);
	}

	/**
	 * <p>appendMark.</p>
	 *
	 * @param markNumber a int.
	 */
	protected void appendMark(int markNumber){
		
//		String ret = "/* ***** MARK ";
//		ret += markNumber;
//		ret += ", Generator: "+this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
 //   	ret += " ***** */";
//		return emptyline()+writeString(ret)+emptyline();
	}

	/**
	 * <p>writeCommentLine.</p>
	 *
	 * @param commentline a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeCommentLine(String commentline){
		String[] tokens = StringUtils.tokenize(commentline, '\n');
		if (tokens.length!=1)
			return writeComment(commentline);
		String ret = writeString("// "+commentline);
    	return ret;
	}
	
	/**
	 * <p>writeComment.</p>
	 *
	 * @param commentline a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String writeComment(String commentline){
	    String[] tokens = StringUtils.tokenize(commentline, '\n');
	    String ret = "";
	    
	    ret += writeString("/**");
	    for (int i=0; i<tokens.length; i++){
	       ret += writeString(" * "+tokens[i]); 
	    }
	    ret += writeString(" */");
	    return ret;
	}

	/**
	 * <p>appendCommentLine.</p>
	 *
	 * @param commentline a {@link java.lang.String} object.
	 */
	protected void appendCommentLine(String commentline){
		appendCommentLine(getCurrentJobContent(), commentline);
	}
	
	/**
	 * <p>appendGenerationPoint.</p>
	 *
	 * @param point a {@link java.lang.String} object.
	 */
	protected void appendGenerationPoint(String point){
		appendCommentLine("Generated by: " + getClass() + "." + point);
		emptyline();
	}
	
	/**
	 * <p>appendCommentLine.</p>
	 *
	 * @param target a {@link java.lang.StringBuilder} object.
	 * @param commentline a {@link java.lang.String} object.
	 */
	protected void appendCommentLine(StringBuilder target, String commentline){
		String[] tokens = StringUtils.tokenize(commentline, '\n');
		if (tokens.length!=1)
			appendComment(target, commentline);
		else
			appendString(target, "// ",commentline);
	}

	/**
	 * <p>appendComment.</p>
	 *
	 * @param commentline a {@link java.lang.String} object.
	 */
	protected void appendComment(String commentline){
		appendComment(getCurrentJobContent(), commentline);
	}
	
	/**
	 * <p>appendComment.</p>
	 *
	 * @param target a {@link java.lang.StringBuilder} object.
	 * @param commentline a {@link java.lang.String} object.
	 */
	protected void appendComment(StringBuilder target, String commentline){
	    String[] tokens = StringUtils.tokenize(commentline, '\n');
	    
	    
	    appendString(target, "/**");
	    for (int i=0; i<tokens.length; i++){
	    	appendString(target, " * "+tokens[i]); 
	    }
	    appendString(target, " */");
	}

	/**
	 * <p>createMultilingualList.</p>
	 *
	 * @param source a {@link java.util.List} object.
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.util.List} object.
	 */
	protected static List<MetaViewElement> createMultilingualList(List<MetaViewElement> source, MetaDocument doc){
		List<MetaViewElement> ret = new ArrayList<MetaViewElement>();
		for (MetaViewElement e : source){
			if (e instanceof MetaFieldElement){
				MetaProperty p = doc.getField(e.getName());
				if (p==null){
					System.out.println("Can't find property for filed "+e.getName()+", skipped");
					continue;
				}
				if (!p.isMultilingual() || !GeneratorDataRegistry.getInstance().getContext().areLanguagesSupported()){
					ret.add(e);
				}else{
					for (String l : GeneratorDataRegistry.getInstance().getContext().getLanguages())
						ret.add(new MultilingualFieldElement(l,(MetaFieldElement)e));
				}
			}else{
				ret.add(e);
			}
		}

		return ret;
		
		
	}
	
	/**
	 * Returns the language of the selected multilingual element or null if the element is not multilingual.
	 *
	 * @param element a {@link net.anotheria.asg.generator.view.meta.MetaViewElement} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getElementLanguage(MetaViewElement element){
		return element instanceof MultilingualFieldElement ? ((MultilingualFieldElement)element).getLanguage() : null;
	}
	
	///////// NEW GENERATION INTERFACE ///////////
	/**
	 * Starts new job. Sets the parameter artefact as generated artefact.
	 *
	 * @param clazz a {@link net.anotheria.asg.generator.GeneratedArtefact} object.
	 */
	public final void startNewJob(GeneratedArtefact clazz){
		GenerationJobManager.startNewJob(clazz);
	}

	/**
	 * <p>getCurrentJobContent.</p>
	 *
	 * @return the content of the currently active job
	 */
	public static final StringBuilder getCurrentJobContent(){
		return GenerationJobManager.getCurrentJob().getStringBuilder();
	}
	
	/**
	 * <p>getCurrentJob.</p>
	 *
	 * @return the artefact currently being generated
	 */
	public static final GeneratedArtefact getCurrentJob(){
		return GenerationJobManager.getCurrentJob().getArtefact();
	}

	/**
	 * Starts the body of a class. Resets the ident.
	 */
	protected void startClassBody(){
		ident = 1;
	}
}
