package net.anotheria.asg.generator.restapi;

import net.anotheria.asg.generator.*;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.StorageType;
import net.anotheria.asg.generator.model.ModuleGenerator;
import net.anotheria.asg.generator.model.ServiceGenerator;
import net.anotheria.asg.generator.model.db.SQLGenerator;
import net.anotheria.asg.generator.model.docs.ModuleFactoryGenerator;
import net.anotheria.asg.generator.view.meta.MetaView;
import net.anotheria.util.ExecutionTimer;

import java.util.ArrayList;
import java.util.List;

import static net.anotheria.asg.generator.GenerationJobManager.startNewJob;

public class RestAPIGenerator extends AbstractAnoDocGenerator {

    /**
     * <p>generate.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @param modules a {@link java.util.List} object.
     */
    public void generate(String path, List<MetaModule> modules, List<MetaView> views){
        Context context = GeneratorDataRegistry.getInstance().getContext();
        List<FileEntry> todo = new ArrayList<FileEntry>();
        ExecutionTimer timer = new ExecutionTimer("DataGenerator");
        timer.startExecution("structure");
        todo.add((new StructureGenerator().generateStructure(path, modules, views)));
        timer.stopExecution("structure");
        writeFiles(todo);
    }


}
