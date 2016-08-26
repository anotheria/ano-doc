package net.anotheria.asg.ant;

import java.io.File;

import net.anotheria.asg.generator.Generator;
import net.anotheria.asg.generator.util.FileWriter;

import org.apache.tools.ant.Task;
/**
 * Tasks for generation of java files out of xml data.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class GenerateTask extends Task {
	/**
	 * The output directory for generated files.
	 */
	private String outputDir;
	/**
	 * The base dir.
	 */
	private String baseDir;
	
	/**
	 * <p>execute.</p>
	 */
	public void execute(){
		
		try{
			log("Starting generation with "+Generator.getProductString()+" into: "+outputDir+" from: "+baseDir);
			FileWriter.setBaseDir(outputDir);
			Generator.setBaseDir(getBaseDir()+File.separatorChar);
			Generator.generate();
			log("Generation complete");
		}catch(Exception e){
			log("Error: " + e.getMessage());
			//TODO: could not understand how to use native logging
			System.out.println("Generation failed: " );
			e.printStackTrace();
		}
	}

	/**
	 * <p>Getter for the field <code>outputDir</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * <p>Setter for the field <code>outputDir</code>.</p>
	 *
	 * @param outputDir a {@link java.lang.String} object.
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * <p>Getter for the field <code>baseDir</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getBaseDir() {
		return baseDir;
	}

	/**
	 * <p>Setter for the field <code>baseDir</code>.</p>
	 *
	 * @param baseDir a {@link java.lang.String} object.
	 */
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
}
