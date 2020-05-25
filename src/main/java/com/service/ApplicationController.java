package com.service;

import com.domain.domaincontroller.DomainController;
import com.domain.logic.data_types.Address;
import com.domain.logic.users.Member;
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
    @PostMapping("/login") //TODO no need for response annotation?
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Member member = domainController.login(loginRequest.getUserName(),loginRequest.getPassword(), loginRequest.getFirstName(), loginRequest.getLastName());
        if (member==null)
            return new LoginResponse(null,"false");
        return new LoginResponse(member,"true");
    }



    // ------------------------2.Create Team---------------------
    @PostMapping("/addTeam")
    public StatusResponse createTeam(@RequestBody TeamRequest teamRequest) {
         if(domainController.createTeam(teamRequest.getOwnerUserName(),teamRequest.getTeamName(), teamRequest.getFieldCountry(), teamRequest.getFieldState(),teamRequest.getFieldCity(),teamRequest.getFieldPostalCode())) {
             return StatusResponse.getTrueStatusObj();
         }
         return StatusResponse.getFalseStatusObj();
    }



    //-------------------------***3.Manage Game:***---------------------

    // ------------------------3.1.Define GameSetting Policy-------
    @PostMapping("/setGameSettingPolicy")
    public StatusResponse defineGameSettingPolicy(@RequestBody GameSettingPolicyRequest gameSettingPolicyRequest) {
        if (domainController.defineGameSettingPolicy(gameSettingPolicyRequest.getAssociationAgentUsername(),gameSettingPolicyRequest.getLeagueName(),gameSettingPolicyRequest.getSeasonYear(),gameSettingPolicyRequest.getGameSettingPolicy())) {
            return StatusResponse.getTrueStatusObj();
        }
         return StatusResponse.getFalseStatusObj();
    }

    // ------------------------3.2.Define Game Ranking Policy-------
    @PostMapping("/setRankingPolicy")
    public StatusResponse defineGameRankingPolicy(@RequestBody RankingPolicyRequest rankingPolicyRequest) {
        domainController.defineGameRankingPolicy(rankingPolicyRequest.getAssociationAgentUsername(),rankingPolicyRequest.getLeagueName(),rankingPolicyRequest.getSeasonYear(),rankingPolicyRequest.getWin(),rankingPolicyRequest.getGoal(),rankingPolicyRequest.getDraw(),rankingPolicyRequest.getYellowCards(),rankingPolicyRequest.getRedCards());
        return null;
    }



    // ------------------------4.1.Referee Adds Events To Game------










}
