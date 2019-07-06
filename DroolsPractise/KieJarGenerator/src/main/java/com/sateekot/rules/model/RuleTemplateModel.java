package com.sateekot.rules.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RuleTemplateModel {

    private String ruleId;
    private Integer quantity;
    private List<String> productList;
    private Double discount;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public static void main(String[] args) {
        List<RuleTemplateModel> list = new ArrayList<>();
        RuleTemplateModel model1 = new RuleTemplateModel();
        model1.setRuleId("discount-rule-1");
        model1.setQuantity(1);
        model1.setDiscount(2.0);
        model1.setProductList(Arrays.asList("ProductA","ProductB","ProductC"));

        RuleTemplateModel model2 = new RuleTemplateModel();
        model2.setRuleId("discount-rule-2");
        model2.setQuantity(2);
        model2.setDiscount(3.0);
        model2.setProductList(Arrays.asList("ProductA","ProductB","ProductC"));

        RuleTemplateModel model3 = new RuleTemplateModel();
        model3.setRuleId("discount-rule-3");
        model3.setQuantity(3);
        model3.setDiscount(4.0);
        model3.setProductList(Arrays.asList("ProductA","ProductB","ProductC","ProductD","ProductE"));

        list.add(model1);
        list.add(model2);
        list.add(model3);

        System.out.println(new Gson().toJson(list));


    }
}
