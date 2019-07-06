package com.sateekot.rules.generator;

/**
 * @author skota
 * RulesEngine is to start the rule engine.
 */
public class RuleEngineInitializer {
    public static void main(String[] args) {
        RuleEngine engine = EngineRegistry.getEngine();
        engine.createInlaneRulesArtifact();
    }
}
