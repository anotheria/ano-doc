package net.anotheria.anodoc.service;

import net.anotheria.anodoc.data.Module;

public class ModuleHolder {

    private Module module;
    private long addingTime;

    public ModuleHolder(Module module, long addingTime) {
        this.module = module;
        this.addingTime = addingTime;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public long getAddingTime() {
        return addingTime;
    }

    public void setAddingTime(long addingTime) {
        this.addingTime = addingTime;
    }
}
