package net.anotheria.asg.generator;

/**
 * Mostly currently executed generation job.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class GenerationJob {
	/**
	 * The string builder with file content.
	 */
	private StringBuilder builder;
	/**
	 * Currently generated artefact.
	 */
	private GeneratedArtefact artefact;
	/**
	 * Creates and starts a new job.
	 */
	public GenerationJob(){
		reset();
	}
	
	/**
	 * <p>Constructor for GenerationJob.</p>
	 *
	 * @param aBuilder a {@link java.lang.StringBuilder} object.
	 */
	public GenerationJob(StringBuilder aBuilder){
		builder = aBuilder;
		artefact = null;
	}
	/**
	 * Sets the generation of the given artefact as the current job.
	 *
	 * @param anArtefact a {@link net.anotheria.asg.generator.GeneratedArtefact} object.
	 */
	public GenerationJob(GeneratedArtefact anArtefact){
		artefact = anArtefact;
		builder = artefact.getBody();
	}

	/**
	 * <p>getStringBuilder.</p>
	 *
	 * @return a {@link java.lang.StringBuilder} object.
	 */
	public StringBuilder getStringBuilder(){
		return builder;
	}
	
	/**
	 * <p>Getter for the field <code>artefact</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.GeneratedArtefact} object.
	 */
	public GeneratedArtefact getArtefact(){
		return artefact;
	}
	
	/**
	 * <p>reset.</p>
	 */
	public void reset(){
		builder = new StringBuilder(5000);
		artefact = null;
	}
	
	/**
	 * <p>Setter for the field <code>builder</code>.</p>
	 *
	 * @param target a {@link java.lang.StringBuilder} object.
	 */
	public void setBuilder(StringBuilder target){
		builder = target;
	}
}
