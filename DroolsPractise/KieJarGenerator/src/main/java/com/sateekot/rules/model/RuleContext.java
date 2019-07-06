package com.sateekot.rules.model;

import org.kie.api.builder.KieFileSystem;

public class RuleContext {
    private StringBuilder stringBuilder;
    private KieFileSystem kieFileSystem;

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }


    public KieFileSystem getKieFileSystem() {
        return kieFileSystem;
    }

    public void setKieFileSystem(KieFileSystem kieFileSystem) {
        this.kieFileSystem = kieFileSystem;
    }
}
