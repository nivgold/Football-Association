package com.service;

import com.domain.domaincontroller.DomainController;
import com.domain.logic.football.League;
import com.domain.logic.policies.GameSettingPolicy;
import com.service.request_data_holders.GameSettingPolicyRequest;
import com.service.request_data_holders.LeagueRequest;
import com.service.request_data_holders.TeamRequest;
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
     * just a test - pass json
     */
    @PostMapping("/addLeague")
    public void addLeagueByObj(@RequestBody LeagueRequest leagueReq){
        System.out.println(leagueReq.getLeagueName());
    }



    // ------------------------2.Create Team---------------------
    @PostMapping("/addTeam")
    public void createTeam(@RequestBody TeamRequest teamRequest) {
        domainController.createTeam(teamRequest.getOwnerUserName(),teamRequest.getTeamName(), teamRequest.getFieldCountry(), teamRequest.getFieldState(),teamRequest.getFieldCity(),teamRequest.getFieldPostalCode());
    }



    //-------------------------3.Manage Game---------------------

    // ------------------------3.1.Define GameSetting Policy-------
    @PostMapping("/setGameSettingPolicy")
    public void defineGameSettingPolicy(@RequestBody GameSettingPolicyRequest gameSettingPolicyRequest) {
        domainController.defineGameSettingPolicy();
    }








}
