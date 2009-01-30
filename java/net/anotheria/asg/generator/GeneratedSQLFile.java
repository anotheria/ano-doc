package net.anotheria.asg.generator;

public class GeneratedSQLFile extends GeneratedArtefact{

	
	private StringBuilder body;
	private String path = "sql";
	
	
	public GeneratedSQLFile() {
		body = new StringBuilder();
	}
	
	public GeneratedSQLFile(String aName){
		this();
		setName(aName);
	}
	
	@Override
	public String createFileContent() {
		StringBuilder ret = new StringBuilder(body.length());
		ret.append(getBody());
		return ret.toString();
	}

	@Override
	public String getFileType() {
		return ".sql";
	}

	@Override
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public StringBuilder getBody() {
		return body;
	}

	
}
