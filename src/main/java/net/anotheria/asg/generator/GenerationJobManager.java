package net.anotheria.asg.generator;

/**
 * <p>GenerationJobManager class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class GenerationJobManager {

	private GenerationJobManager() {
	}

	/**
	 * Currently executed job.
	 */
	private static ThreadLocal<GenerationJob> currentJob = new ThreadLocal<GenerationJob>(){
		@Override
		protected synchronized GenerationJob initialValue(){
			return new GenerationJob();
		}
	};
	
	/**
	 * <p>Getter for the field <code>currentJob</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.GenerationJob} object.
	 */
	public static GenerationJob getCurrentJob(){
		return currentJob.get();
	}
	
	/**
	 * <p>startNewJob.</p>
	 */
	public static void startNewJob(){
		currentJob.set(new GenerationJob());
	}

	/**
	 * <p>startNewJob.</p>
	 *
	 * @param artefact a {@link net.anotheria.asg.generator.GeneratedArtefact} object.
	 */
	public static void startNewJob(GeneratedArtefact artefact){
		currentJob.set(new GenerationJob(artefact));
	}
}
