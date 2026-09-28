package net.anotheria.asg.generator.transfer;

import net.anotheria.asg.generator.AbstractAnoDocGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.util.ExecutionTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates the document transfer layer: one {@code <Module>TransferSupport} per module and the
 * {@code TransferSupportRegistrar} that registers them at startup.
 *
 * <p>Runs after the rest api generator, because the supports hand out the rest VOs and use the rest paths.
 */
public class TransferAPIGenerator extends AbstractAnoDocGenerator {

    /**
     * Generates the transfer layer for all modules and writes the output files.
     *
     * @param path    output path
     * @param modules list of modules to generate for
     */
    public void generate(String path, List<MetaModule> modules) {
        List<FileEntry> todo = new ArrayList<>();
        ExecutionTimer timer = new ExecutionTimer("TransferAPIGenerator");

        TransferSupportGenerator supportGenerator = new TransferSupportGenerator();
        for (MetaModule module : modules) {
            timer.startExecution(module.getName() + "-transfer");
            todo.addAll(supportGenerator.generate(module));
            timer.stopExecution(module.getName() + "-transfer");
        }
        todo.add(supportGenerator.generateRegistrarFile(modules));

        writeFiles(todo);
    }
}
