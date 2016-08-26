package net.anotheria.asg.generator;

import java.util.List;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public interface IGenerator {
	/**
	 * <p>generate.</p>
	 *
	 * @param g a {@link net.anotheria.asg.generator.IGenerateable} object.
	 * @return a {@link java.util.List} object.
	 */
	List<FileEntry> generate(IGenerateable g);	
}
