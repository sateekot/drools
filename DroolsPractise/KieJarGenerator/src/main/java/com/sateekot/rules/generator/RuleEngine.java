package com.sateekot.rules.generator;

import com.sateekot.rules.model.RuleContext;
import com.sateekot.rules.utils.KieJarConstants;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.core.util.FileManager;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.scanner.KieMavenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RuleEngine {

    private static Logger LOGGER = Logger.getLogger(RuleEngine.class.getName());

    private final List<RuleProcessor> processorList;

    @Autowired
    public RuleEngine(List<RuleProcessor> processors) {
        System.out.println(processors.size());
        this.processorList = processors;
    }

    public boolean createInlaneRulesArtifact() {
        try {
            // TODO
            // 1. Call to get All Programs metadata.
            //String finalDRL = prepareCompleteDRL();
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kfs = createKieFileSystemWithKProject(kieServices, true);
            StringBuilder stringBuilder = new StringBuilder();
            RuleContext ruleContext = new RuleContext();
            ruleContext.setStringBuilder(stringBuilder);
            ruleContext.setKieFileSystem(kfs);
            for(RuleProcessor processor : processorList) {
                processor.execute(ruleContext);
            }
            ReleaseId releaseId = kieServices.newReleaseId(KieJarConstants.GROUP_ID, KieJarConstants.ARTIFACT_ID, KieJarConstants.VERSION);
            InternalKieModule kieJar = createKieJarWithClass(kieServices, releaseId, ruleContext);
            KieMavenRepository mavenRepository = KieMavenRepository.getKieMavenRepository();
            mavenRepository.installArtifact(releaseId,kieJar, createKPom(releaseId));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        return false;
    }

/*    private String prepareCompleteDRL() {
        StringBuilder stringBuilder = new StringBuilder();
        RuleContext ruleContext = new RuleContext();
        ruleContext.setStringBuilder(stringBuilder);
        ruleContext.setContinuityProgramMaster(new ContinuityProgramMaster());
        for(RuleProcessor processor : processorList) {
            processor.execute(ruleContext);
        }
        return stringBuilder.toString();
    }*/

    private File createKPom(ReleaseId releaseId) throws IOException {
        FileManager fileManager = new FileManager();
        File pomFile = fileManager.newFile("pom.xml");
        fileManager.write(pomFile, getPom(releaseId));
        return pomFile;
    }

    private String getPom(ReleaseId releaseId, ReleaseId... dependencies) {
        String pom
                = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + " xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n"
                + " <modelVersion>4.0.0</modelVersion>\n"
                + "\n"
                + " <groupId>" + releaseId.getGroupId() + "</groupId>\n"
                + " <artifactId>" + releaseId.getArtifactId() + "</artifactId>\n"
                + " <version>" + releaseId.getVersion() + "</version>\n"
                + "\n";
        if (dependencies != null && dependencies.length > 0) {
            pom += "<dependencies>\n";
            for (ReleaseId dep : dependencies) {
                pom += "<dependency>\n";
                pom += " <groupId>" + dep.getGroupId() + "</groupId>\n";
                pom += " <artifactId>" + dep.getArtifactId() + "</artifactId>\n";
                pom += " <version>" + dep.getVersion() + "</version>\n";
                pom += "</dependency>\n";
            }
            pom += "</dependencies>\n";
        }
        pom += "</project>";
        return pom;
    }

    private InternalKieModule createKieJarWithClass(KieServices ks, ReleaseId releaseId, RuleContext ruleContext) throws IOException {

        KieFileSystem kfs = ruleContext.getKieFileSystem();
        kfs.writePomXML(getPom(releaseId));
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        Results results = kieBuilder.buildAll().getResults();
        if(!results.getMessages().isEmpty()) {
            for(Message message : results.getMessages()) {
                LOGGER.log(Level.INFO, message.getText());
            }
        }
        return (InternalKieModule) kieBuilder.getKieModule();
    }

    protected KieFileSystem createKieFileSystemWithKProject(KieServices ks, boolean isdefault) {
        return createKieFileSystemWithKProject(ks, isdefault, "defaultKieBase", "defaultKieSession");
    }

    protected static KieFileSystem createKieFileSystemWithKProject(KieServices ks, boolean isdefault, String kbaseName, String ksessionName) {
        KieModuleModel kieModuleModel = ks.newKieModuleModel();
        KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel(kbaseName).setDefault(isdefault);
        KieSessionModel ksession1 = kieBaseModel.newKieSessionModel(ksessionName).setDefault(isdefault)
                .setType(KieSessionModel.KieSessionType.STATEFUL);
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.writeKModuleXML(kieModuleModel.toXML());
        return kfs;
    }
}
