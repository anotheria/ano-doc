package net.anotheria.asg.util.filestorage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to temporary hold the file data between an upload
 * an save.
 */
public class TemporaryFileHolder implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Map<String,String> extensionMapping;
	
	static{
		extensionMapping = new HashMap<String, String>();
		extensionMapping.put("pdf","application/pdf");
		extensionMapping.put("jpe","image/jpeg");
		extensionMapping.put("jpeg","image/jpeg");
		extensionMapping.put("jpg","image/jpeg");
		extensionMapping.put("png","image/png");
		extensionMapping.put("js","text/javascript");
		extensionMapping.put("exe","application/octet-stream");
		extensionMapping.put("gif","image/gif");
		extensionMapping.put("gz","application/x-gzip");
		extensionMapping.put("zip","application/zip");
		
		extensionMapping.put("doc","application/msword");
		extensionMapping.put("dot","application/msword");

		extensionMapping.put("pot","application/vnd.ms-powerpoint");
		extensionMapping.put("ppt","application/vnd.ms-powerpoint");
		extensionMapping.put("pps","application/vnd.ms-powerpoint");
	}

	/**
	 * The data.
	 */
	private byte[] data;
	/**
	 * The file mimetype.
	 */
	private String mimeType;
	/**
	 * The name of the file.
	 */
	private String fileName;
	
	/**
	 * last modified if available
	 */
	private long lastModified;
	
	/**
	 * Returns the data.
	 * @return byte[]
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Returns the size of this file in bytes.
	 * @return
	 */
	public int getSize(){
		return data.length;
	}

	/**
	 * Returns the fileName.
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns the mimeType.
	 * @return String
	 */
	public String getMimeType() {
		if (mimeType==null)
			mimeType = guessMimeType();
		return mimeType;
	}

	/**
	 * Sets the data.
	 * @param data The data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Sets the fileName.
	 * @param fileName The fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the mimeType.
	 * @param mimeType The mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	private String guessMimeType(){
		if (fileName==null)
			return null;
		try{
			String extension = fileName.substring(fileName.lastIndexOf('.')+1);
			//System.out.println(extension); 	
			if (extension!=null)
				return extensionMapping.get(extension);
		}catch(Exception e){
			//ignored;
		}
		
		return null;
	}
	
	public static void main(String a[]){
		TemporaryFileHolder f = new TemporaryFileHolder();
		f.setFileName("bla.jpg");
		System.out.println(f.getMimeType());
		System.out.println(f.getMimeType());
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	

}
