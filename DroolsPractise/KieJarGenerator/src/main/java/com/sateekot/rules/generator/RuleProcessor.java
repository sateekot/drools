package com.sateekot.rules.generator;

import com.sateekot.rules.model.RuleContext;

/**
 *
 */
public interface RuleProcessor {
    boolean execute(RuleContext ruleContext);
    /*default  KieFileSystem createKieFileSystemWithKProject(KieServices ks, boolean isdefault) {
        return createKieFileSystemWithKProject(ks, isdefault, "KBase1", "KSession1");
    }
    default KieFileSystem createKieFileSystemWithKProject(KieServices ks, boolean isdefault, String kbaseName, String ksessionName) {
        KieModuleModel kproj = ks.newKieModuleModel();

        KieBaseModel kieBaseModel1 = kproj.newKieBaseModel(kbaseName).setDefault(isdefault);

        KieSessionModel ksession1 = kieBaseModel1.newKieSessionModel(ksessionName).setDefault(isdefault)
                .setType(KieSessionModel.KieSessionType.STATEFUL)
                //.setClockType(ClockTypeOption.get("realtime"))
                ;

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.writeKModuleXML(kproj.toXML());
        return kfs;
    }*/
}
