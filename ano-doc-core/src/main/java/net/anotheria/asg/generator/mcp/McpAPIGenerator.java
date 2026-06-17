package net.anotheria.asg.generator.mcp;

import net.anotheria.asg.generator.AbstractAnoDocGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.util.ExecutionTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates the MCP tools layer for all modules.
 * For each module produces one {Module}McpTools.java containing List/Get/Update
 * inner classes per document. Register only the tools relevant to your use case.
 */
public class McpAPIGenerator extends AbstractAnoDocGenerator {

    public void generate(String path, List<MetaModule> modules) {
        List<FileEntry> todo = new ArrayList<>();
        ExecutionTimer timer = new ExecutionTimer("McpAPIGenerator");

        McpToolsGenerator toolsGen = new McpToolsGenerator();

        for (MetaModule module : modules) {
            timer.startExecution(module.getName() + "-mcp");
            todo.addAll(toolsGen.generate(module));
            timer.stopExecution(module.getName() + "-mcp");
        }

        writeFiles(todo);
    }
}
