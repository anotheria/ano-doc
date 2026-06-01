package net.anotheria.asg.generator;

/**
 * Represents a generated XML file.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class GeneratedXMLFile extends GeneratedArtefact{

	/**
	 * Body of the artefact.
	 */
	private StringBuilder body;
	/**
	 * Path to the generated file.
	 */
	private String path;
	/**
	 * Encoding of the generated file.
	 */
	private String encoding;
	
	/**
	 * Creates a new artefact.
	 */
	public GeneratedXMLFile(){
		body = new StringBuilder();
	}
	
	/**
	 * Creates a new artefact.
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param anEncoding a {@link java.lang.String} object.
	 */
	public GeneratedXMLFile(String aName, String anEncoding){
		this();
		setName(aName);
		encoding = anEncoding;
	}
	
	/** {@inheritDoc} */
	@Override
	public String createFileContent() {
		StringBuilder ret = new StringBuilder(body.length());
		ret.append("<?xml version=\"1.0\" encoding=\""+getEncoding()+"\" ?>"+CRLF);
		ret.append(getBody());
		return ret.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String getFileType() {
		return ".xml";
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

	/**
	 * <p>Getter for the field <code>encoding</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * <p>Setter for the field <code>encoding</code>.</p>
	 *
	 * @param encoding a {@link java.lang.String} object.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}
