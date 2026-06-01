package net.anotheria.asg.generator.restapi;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedJSONFile;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.meta.MetaModuleSection;
import net.anotheria.asg.generator.view.meta.MetaSection;
import net.anotheria.asg.generator.view.meta.MetaView;
import net.anotheria.asg.generator.view.meta.MetaViewElement;

import java.util.List;


public class StructureGenerator extends AbstractGenerator {
    public FileEntry generateStructure(String path, List<MetaModule> modules, List<MetaView> views){
        System.out.println("==== GENERATE STRUCTURE ");


        GeneratedJSONFile artefact = new GeneratedJSONFile("structure");
        startNewJob(artefact);

        appendString("{");
        increaseIdent();

        appendString(quote("views"), ": [");
        increaseIdent();
        boolean firstView = true;
        for (MetaView view: views){
            if (!firstView){
                appendString(",");
            }
            firstView = false;
            appendString("{");
            increaseIdent();
            appendString(quote("name"), ": " + quote(view.getName()),",");

            appendString(quote("sections"),": [");
            increaseIdent();
            for (int i = 0; i < view.getSections().size(); i++){
                MetaSection sec = view.getSections().get(i);
                if (!(sec instanceof MetaModuleSection)){
                    continue;
                }
                MetaModuleSection section = (MetaModuleSection) sec;
                appendString("{");
                increaseIdent();
                appendString(quote("name"), ": " + quote(section.getTitle()),",");
                appendString(quote("module"), ": " + quote(section.getModule().getName()),",");
                appendString(quote("document"), ": " + quote(section.getDocument().getName()),",");
                appendString(quote("elements"), ": [");
                increaseIdent();

                for (int j = 0; j < section.getElements().size(); j++){
                    MetaViewElement elem = section.getElements().get(j);
                    appendString("{");
                    increaseIdent();
                    appendString(quote("name"), ": " + quote(elem.getName()),",");
                    appendString(quote("type"), ": " + quote(elem.getClass().getSimpleName()),",");
                    appendString(quote("readonly"),": "+elem.isReadonly(),",");
                    appendString(quote("autocompleteOff"),": "+elem.isAutocompleteOff());
                    decreaseIdent();
                    appendString("}");
                    if (j < section.getElements().size() - 1){
                        appendString(",");
                    }
                }
                decreaseIdent(); // elements
                appendString("]");

                decreaseIdent();
                appendString("}");
                if (i < view.getSections().size() - 1){
                    appendString(",");
                }
            }
            decreaseIdent();
            appendString("]");//sections

            decreaseIdent();
            appendString("}");
        }

        decreaseIdent();
        appendString("]");
        closeBlockNEW();


        FileEntry structureFile = new FileEntry(artefact);
        structureFile.setType(".json");
        return structureFile;
    }

}
