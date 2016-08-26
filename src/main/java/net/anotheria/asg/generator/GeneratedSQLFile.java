package net.anotheria.asg.generator;

/**
 * Represents a generated SQL script.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class GeneratedSQLFile extends GeneratedArtefact{

	/**
	 * Body of the file.
	 */
	private StringBuilder body;
	/**
	 * Path to the generated artefact storage.
	 */
	private String path = "sql";
	
	
	/**
	 * <p>Constructor for GeneratedSQLFile.</p>
	 */
	public GeneratedSQLFile() {
		body = new StringBuilder();
	}
	
	/**
	 * <p>Constructor for GeneratedSQLFile.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	public GeneratedSQLFile(String aName){
		this();
		setName(aName);
	}
	
	/** {@inheritDoc} */
	@Override
	public String createFileContent() {
		StringBuilder ret = new StringBuilder(body.length());
		ret.append(getBody());
		return ret.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String getFileType() {
		return ".sql";
	}

	/** {@inheritDoc} */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * <p>Setter for the field <code>path</code>.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * <p>Getter for the field <code>body</code>.</p>
	 *
	 * @return a {@link java.lang.StringBuilder} object.
	 */
	public StringBuilder getBody() {
		return body;
	}

	
}
