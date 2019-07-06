package com.sateekot.rules.utils;

import org.drools.template.ObjectDataCompiler;

import java.io.InputStream;
import java.util.List;

public class KieHelper {
    public static String prepareDRLString(String templatePath, List<?> programMetaData) {

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(programMetaData, getTemplate(templatePath));
        return drl;
    }

    private static InputStream getTemplate(String templatePath) {
        return KieHelper.class.getResourceAsStream( templatePath );
    }
}
