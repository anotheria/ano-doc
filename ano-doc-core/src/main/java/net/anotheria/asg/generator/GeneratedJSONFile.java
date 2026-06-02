package net.anotheria.asg.generator;

/**
 * Represents a generated XML file.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class GeneratedJSONFile extends GeneratedArtefact{

	/**
	 * Body of the artefact.
	 */
	private StringBuilder body;
	/**
	 * Path to the generated file.
	 */
	private String path;

	/**
	 * Creates a new artefact.
	 */
	public GeneratedJSONFile(){
		body = new StringBuilder();
	}

	/**
	 * Creates a new artefact.
	 *
	 * @param aName a {@link String} object.
	 */
	public GeneratedJSONFile(String aName){
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
		return ".json";
	}

	/** {@inheritDoc} */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * <p>Setter for the field <code>path</code>.</p>
	 *
	 * @param path a {@link String} object.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * <p>Getter for the field <code>body</code>.</p>
	 *
	 * @return a {@link StringBuilder} object.
	 */
	public StringBuilder getBody() {
		return body;
	}


}
