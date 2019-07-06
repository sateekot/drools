package com.sateekot.rules.controller;


import com.sateekot.rules.model.SalesData;
import com.sateekot.rules.model.UserReward;
import com.sateekot.rules.service.KieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class KieRuleEngineController {

    @Autowired
    private KieServiceImpl kieServiceImpl;

    @RequestMapping(value = "/getOffers", method = RequestMethod.POST)
    public Map<String, UserReward> getAllProgramOffers(@RequestBody SalesData salesData) {
        long startTime = System.currentTimeMillis();
        Map<String, UserReward> result = kieServiceImpl.getOffers(salesData);
        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken for request = " + (endTime - startTime));
        return result;
    }


    @RequestMapping(value = "/reloadRules", method = RequestMethod.GET)
    public String reloadRules() {
        try {
            String result = kieServiceImpl.buildKieContainer();
            return result;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
