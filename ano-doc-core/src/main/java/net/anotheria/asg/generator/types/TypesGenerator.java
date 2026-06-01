package net.anotheria.asg.generator.types;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.AbstractAnoDocGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.types.meta.DataType;
import net.anotheria.asg.generator.types.meta.EnumerationType;


/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public class TypesGenerator extends AbstractAnoDocGenerator{
	/**
	 * <p>generate.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param types a {@link java.util.List} object.
	 */
	public void generate(String path, List<DataType> types){
		List<FileEntry> files = new ArrayList<FileEntry>();
		Context context = GeneratorDataRegistry.getInstance().getContext();
		
		for (int i=0; i<types.size(); i++){
			DataType type = types.get(i);
			//System.out.println("Generating type: "+type);
			if (type instanceof EnumerationType)
				files.addAll(new EnumerationGenerator().generate(type));
		}
		
		writeFiles(files);
	}

}
