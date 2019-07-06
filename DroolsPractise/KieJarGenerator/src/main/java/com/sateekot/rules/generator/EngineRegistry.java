package com.sateekot.rules.generator;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author skota
 * Scans the beans.
 */
public class EngineRegistry {

    public static RuleEngine getEngine() {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        RuleEngine engine = ctx.getBean(RuleEngine.class);
        return engine;
    }

    @Configuration
    @ComponentScan(basePackages="com.sateekot.rules.generator")
    private static class AppConfig {
        public AppConfig() {
        }
    }
}
