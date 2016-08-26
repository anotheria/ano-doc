package net.anotheria.asg.generator;

import net.anotheria.util.StringUtils;

/**
 * Generation result.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class FileEntry {
	/**
	 * The path to store the file into.
	 */
	private String path;
	/**
	 * The name of the file.
	 */
	private String name;
	/**
	 * The content of the file.
	 */
	private String content;
	/**
	 * The type of the file. The type of the file is the extension.
	 */
	private String type;
	
	/**
	 * <p>Constructor for FileEntry.</p>
	 */
	public FileEntry(){
		type = ".java";
	}
	
	/**
	 * <p>Constructor for FileEntry.</p>
	 *
	 * @param aPath a {@link java.lang.String} object.
	 * @param aName a {@link java.lang.String} object.
	 * @param aContent a {@link java.lang.String} object.
	 * @param aType a {@link java.lang.String} object.
	 */
	public FileEntry(String aPath, String aName, String aContent, String aType){
		this();
		this.path = aPath;
		this.name = aName;
		this.content = aContent;
		type = aType;
	}
	
	/**
	 * <p>Constructor for FileEntry.</p>
	 *
	 * @param aPath a {@link java.lang.String} object.
	 * @param aName a {@link java.lang.String} object.
	 * @param aContent a {@link java.lang.String} object.
	 */
	public FileEntry(String aPath, String aName, String aContent){
		this();
		this.path = aPath;
		this.name = aName;
		this.content = aContent;
	}
	
	/**
	 * <p>Constructor for FileEntry.</p>
	 *
	 * @param artefact a {@link net.anotheria.asg.generator.GeneratedArtefact} object.
	 */
	public FileEntry(GeneratedArtefact artefact){
		this();
		if (artefact!=null){
			path = artefact.getPath();
			name = artefact.getName();
			type = artefact.getFileType();
			content = artefact.createFileContent();
		}
	}
	
	/**
	 * <p>Getter for the field <code>content</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Getter for the field <code>path</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * <p>Setter for the field <code>content</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setContent(String string) {
		content = string;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * <p>Setter for the field <code>path</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setPath(String string) {
		path = string;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return path+"/"+name;
	}
	
	/**
	 * Resolves a package name to a full path including sources dir.
	 *
	 * @param packageName a {@link java.lang.String} object.
	 * @return full path of the package
	 */
	public static String package2fullPath(String packageName){
		return "java/" + package2path(packageName); 
	}
	
	/**
	 * Resolves a package name to a path.
	 *
	 * @param packageName a {@link java.lang.String} object.
	 * @return path of package
	 */
	public static String package2path(String packageName){
		return StringUtils.replace(packageName, '.', '/'); 
	}

	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return type of the file
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets type of the file.
	 *
	 * @param string type of file to set
	 */
	public void setType(String string) {
		type = string;
	}

}
