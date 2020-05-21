package com.service;

import com.domain.domaincontroller.DomainController;
import com.service.request_data_holders.*;
import com.service.responses.LoginResponse;
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



    // ------------------------1.Login-------------------z

    /**
     * api/v1/login/
     * @return
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        if (domainController.login(loginRequest.getUserName(),loginRequest.getPassword(), loginRequest.getFirstName(), loginRequest.getLastName()))
            return new LoginResponse(loginRequest.getUserName(),"true");
        return new LoginResponse(loginRequest.getUserName(), "false");
    }



    // ------------------------2.Create Team---------------------
    @PostMapping("/addTeam")
    public void createTeam(@RequestBody TeamRequest teamRequest) {
        domainController.createTeam(teamRequest.getOwnerUserName(),teamRequest.getTeamName(), teamRequest.getFieldCountry(), teamRequest.getFieldState(),teamRequest.getFieldCity(),teamRequest.getFieldPostalCode());
    }



    //-------------------------***3.Manage Game:***---------------------

    // ------------------------3.1.Define GameSetting Policy-------
    @PostMapping("/setGameSettingPolicy")
    public void defineGameSettingPolicy(@RequestBody GameSettingPolicyRequest gameSettingPolicyRequest) {
        domainController.defineGameSettingPolicy(gameSettingPolicyRequest.getAssociationAgentUsername(),gameSettingPolicyRequest.getLeagueName(),gameSettingPolicyRequest.getSeasonYear(),gameSettingPolicyRequest.getGameSettingPolicy());
    }

    // ------------------------3.2.Define Game Ranking Policy-------
    @PostMapping("/setRankingPolicy")
    public void defineGameRankingPolicy(@RequestBody RankingPolicyRequest rankingPolicyRequest) {
        domainController.defineGameRankingPolicy(rankingPolicyRequest.getAssociationAgentUsername(),rankingPolicyRequest.getLeagueName(),rankingPolicyRequest.getSeasonYear(),rankingPolicyRequest.getWin(),rankingPolicyRequest.getGoal(),rankingPolicyRequest.getDraw(),rankingPolicyRequest.getYellowCards(),rankingPolicyRequest.getRedCards());
    }









}
