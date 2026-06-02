package net.anotheria.asg.generator.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.anotheria.util.IOUtils;
import net.anotheria.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility for writing code to files.
 *
 * @author lrosenberg.
 * @version $Id: $Id
 */
public class FileWriter {

	private FileWriter() {
	}

	private static String BASE_DIR;
	
	/** Constant <code>DEF_BASE_DIR="."</code> */
	public static final String DEF_BASE_DIR = ".";
	
	static{
		BASE_DIR = DEF_BASE_DIR;
	}
	
	/**
	 * <p>writeFile.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param fileName a {@link java.lang.String} object.
	 * @param content a {@link java.lang.String} object.
	 */
	public static final void writeFile(String path, String fileName, String content){
		writeFile(path, fileName, content, false);
	}

	/**
	 * <p>writeFile.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param fileName a {@link java.lang.String} object.
	 * @param content a {@link java.lang.String} object.
	 * @param override a boolean.
	 */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	public static final void writeFile(String path, String fileName, String content, boolean override){
		if (content==null || content.length()==0){
			//System.out.println("IGNORE emptyfile "+fileName );
			return;
		}
		if (path==null)
			path = "";
		if (path.length()>0 && !path.endsWith("/"))
			path += "/";
			
		File fDir = new File(BASE_DIR+"/"+path);
		fDir.mkdirs();
		File f = new File(BASE_DIR+"/"+path+fileName);
		if (f.exists() && !override){
			FileInputStream fIn = null;
			try{
				fIn = new FileInputStream(f);
				byte[] d = new byte[fIn.available()];
				fIn.read(d);
				if (content.equals(new String(d))){
					//System.out.println("Skipping "+f);
					return;					
				}
					
			}catch(IOException e){
			}finally{
				IOUtils.closeIgnoringException(fIn);
			}
		}
		System.out.println("writing "+f);
		FileOutputStream fOut = null;
		try{
		
			fOut = new FileOutputStream(f);
			fOut.write(content.getBytes());
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			IOUtils.closeIgnoringException(fOut);
		}
	}
	
	/**
	 * <p>writeJavaFile.</p>
	 *
	 * @param packageName a {@link java.lang.String} object.
	 * @param className a {@link java.lang.String} object.
	 * @param content a {@link java.lang.String} object.
	 */
	public static final void writeJavaFile(String packageName, String className, String content){
		String[] tokens = StringUtils.tokenize(packageName, '.');
		String path = "";
		for (int i=0; i<tokens.length; i++){
			path += tokens[i];
			if (i< tokens.length-1)
				path += "/";
		}
		writeFile(path, className+".java", content, false);
	}
	
	/**
	 * <p>setBaseDir.</p>
	 *
	 * @param aBaseDir a {@link java.lang.String} object.
	 */
	public static void setBaseDir(String aBaseDir){
		BASE_DIR = aBaseDir;
	}
	
	/**
	 * <p>main.</p>
	 *
	 * @param a an array of {@link java.lang.String} objects.
	 */
	public static void main(String []a){
		
	}
}
