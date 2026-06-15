package net.anotheria.asg.generator.restapi;

import net.anotheria.asg.generator.AbstractAnoDocGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.meta.MetaView;
import net.anotheria.util.ExecutionTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates the REST API layer for all modules.
 * For each module and each document within it, produces:
 * <ul>
 *   <li>A VO class (DocumentNameVO) implementing DataObject with only the public fields</li>
 *   <li>A JAX-RS resource class (DocumentNameResource) with full CRUD methods</li>
 * </ul>
 */
public class RestAPIGenerator extends AbstractAnoDocGenerator {

    /**
     * Generates the REST API for all modules and writes the output files.
     *
     * @param path    output path
     * @param modules list of modules to generate for
     * @param views   list of views (used for the structure file)
     */
    public void generate(String path, List<MetaModule> modules, List<MetaView> views) {
        List<FileEntry> todo = new ArrayList<>();
        ExecutionTimer timer = new ExecutionTimer("RestAPIGenerator");

        timer.startExecution("structure");
        todo.add(new StructureGenerator().generateStructure(path, modules, views));
        timer.stopExecution("structure");

        RestVOGenerator voGen = new RestVOGenerator();
        RestResourceGenerator resourceGen = new RestResourceGenerator();

        for (MetaModule module : modules) {
            timer.startExecution(module.getName() + "-rest");
            for (MetaDocument doc : module.getDocuments()) {
                todo.addAll(voGen.generate(doc));
            }
            todo.addAll(resourceGen.generate(module));
            timer.stopExecution(module.getName() + "-rest");
        }

        writeFiles(todo);
    }
}
