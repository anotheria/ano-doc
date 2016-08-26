package net.anotheria.asg.generator;

/**
 * Represents a generated jsp file.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class GeneratedJSPFile extends GeneratedArtefact{

	/**
	 * Body of the artefact.
	 */
	private StringBuilder body;
	/**
	 * Path of the artefact.
	 */
	private String path;
	
	
	/**
	 * <p>Constructor for GeneratedJSPFile.</p>
	 */
	public GeneratedJSPFile(){
		body = new StringBuilder();
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
		return ".jsp";
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

	/** {@inheritDoc} */
	@Override public StringBuilder getBody() {
		return body;
	}
	
	/**
	 * <p>setPackage.</p>
	 *
	 * @param aPackage a {@link java.lang.String} object.
	 */
	public void setPackage(String aPackage){
		path = FileEntry.package2fullPath(aPackage);
	}
}
