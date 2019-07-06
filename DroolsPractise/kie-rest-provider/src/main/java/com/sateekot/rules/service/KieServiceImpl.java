package com.sateekot.rules.service;

import com.sateekot.rules.model.SalesData;
import com.sateekot.rules.model.UserReward;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Service
public class KieServiceImpl {
    private KieContainer kieContainer;

    @PostConstruct
    public void init() {
        buildKieContainer();
    }

    public Map<String, UserReward> getOffers(SalesData salesData) {

        Map<String, UserReward> globalResult = new HashMap<>();
        try {
            KieSession kieSession = kieContainer.newKieSession("defaultKieSession");
            kieSession.setGlobal("globalResult", globalResult);
            kieSession.insert(salesData);
            kieSession.fireAllRules();
            kieSession.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return globalResult;

    }

    public String buildKieContainer() {
        KieServices ks = KieServices.Factory.get();
        String groupId = "com.sateekot.rules";
        String artifactId = "RewardRules";
        String version = "[1.0.0-SNAPSHOT,)";
        ReleaseId releaseId = ks.newReleaseId(groupId, artifactId, version);
        kieContainer = ks.newKieContainer(releaseId);
        KieScanner kscanner = ks.newKieScanner(kieContainer);
        kscanner.scanNow();
        Results results = kieContainer.verify();
        results.getMessages().stream().forEach((message) -> {
            System.out.println(">> Message ( " + message.getLevel() + " ): " + message.getText());
        });
        assertThat(false, is(results.hasMessages(Message.Level.ERROR)));
        kieContainer.getKieBaseNames().stream().map((kieBase) -> {
            System.out.println(">> Loading KieBase: " + kieBase);
            return kieBase;
        }).forEach((kieBase) -> {
            kieContainer.getKieSessionNamesInKieBase(kieBase).stream().forEach((kieSession) -> {
                System.out.println("\t >> Containing KieSession: " + kieSession);

            });
        });

        for (KiePackage kp : kieContainer.getKieBase("defaultKieBase").getKiePackages()) {
            for (Rule rule : kp.getRules()) {
                System.out.println("kp " + kp + " rule " + rule.getName());
            }
        }
        kieContainer.newKieSession("defaultKieSession");
        return "KieContainer built successfully.";
    }

/*    private void updateKieContainer() {
        KieServices ks = KieServices.Factory.get();
        KieScanner scanner = ks.newKieScanner(kieContainer);
        scanner.scanNow();
    }*/
}
