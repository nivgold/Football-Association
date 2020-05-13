package com.service;

import com.domain.domaincontroller.DomainController;
import com.domain.logic.football.League;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1")
@RestController
public class ApplicationController {


    private final DomainController domainController;

    @Autowired
    public ApplicationController(DomainController domainController) {
        this.domainController = domainController;
    }


    /**
     * just a test - as stav wants
     */
    @PostMapping("/addLeague")
    public void addLeagueByName (@RequestBody String leagueName){
        domainController.addLeagueByName(leagueName);
    }

    /**
     * just a test - pass json
     */
    @PostMapping
    public void addLeagueByObj(@RequestBody League league){
        domainController.addLeagueByObject(league);
    }







}
