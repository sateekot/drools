package com.sateekot.rules.generator;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sateekot.rules.model.RuleContext;
import com.sateekot.rules.model.RuleTemplateModel;
import com.sateekot.rules.utils.KieHelper;
import org.kie.api.builder.KieFileSystem;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiscountRewardRuleProcessor implements RuleProcessor {
    @Override
    public boolean execute(RuleContext ruleContext) {
        //2. Prepare data as per template. Now It is reading from file.
        Reader readerUsecase1 = new InputStreamReader(getClass().getResourceAsStream("/testdata/discount-reward.json"));
        Type listType = new TypeToken<ArrayList<RuleTemplateModel>>(){}.getType();
        List<RuleTemplateModel> sweepstakesBuyXFromNUpc = new Gson().fromJson(readerUsecase1,listType);
        String drl = KieHelper.prepareDRLString("/templates/discount_reward.drt",sweepstakesBuyXFromNUpc);
        ruleContext.getStringBuilder().append(drl);

        KieFileSystem kfs = ruleContext.getKieFileSystem();
        String file = "rules/discount_reward_rules.drl";
        kfs.write("src/main/resources/defaultKieBase/" + file, drl);
/*        ArrayList<SweepstakesProgram> rules = new ArrayList<>();
        rules.add( createRule( 1, Arrays.asList("UPC1","UPC2","UPC3"), "1",0.0) );
        rules.add( createRule( 2, Arrays.asList("UPC4","UPC5","UPC6"), "2", 0.0) );
        rules.add( createRule( 3, Arrays.asList("UPC7","UPC8","UPC9"), "3", 0.0) );
        rules.add( createRule( 4, Arrays.asList("UPC11","UPC12","UPC13"), "4", 0.0) );*/
        return true;
    }
}
