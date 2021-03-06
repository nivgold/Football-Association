package com.service;

import com.domain.domaincontroller.DomainController;
import com.domain.logic.data_types.GameIdentifier;
import com.domain.logic.football.Event;
import com.domain.logic.users.Member;
import com.service.messagingstompwebsocket.Notification;
import com.service.request_data_holders.*;
import com.service.responses.GameIdentifierResponse;
import com.service.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("api/v1")
@RestController @CrossOrigin
public class ApplicationController {

    @Autowired
    private SimpMessagingTemplate simpleMessagingTemplate;

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
        return new LoginResponse(member, "true");
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
        if (domainController.defineGameRankingPolicy(rankingPolicyRequest.getAssociationAgentUsername(),rankingPolicyRequest.getLeagueName(),rankingPolicyRequest.getSeasonYear(),rankingPolicyRequest.getWin(),rankingPolicyRequest.getGoal(),rankingPolicyRequest.getDraw(),rankingPolicyRequest.getYellowCards(),rankingPolicyRequest.getRedCards())) {
            return StatusResponse.getTrueStatusObj();
        }
        return StatusResponse.getFalseStatusObj();
    }


    // ------------------------***4:***------------------------------

    // ------------------------4.1.Referee Adds Events To Game------
    @PostMapping("/addEventToGame")
    public StatusResponse addEventToGame(@RequestBody AddEventToGameRequest addEventToGameRequest) {
        Event event = domainController.addGameEvent(addEventToGameRequest.getRefereeUsername(),addEventToGameRequest.getGameID(),addEventToGameRequest.getGameMinute(),addEventToGameRequest.getDescription(),addEventToGameRequest.getType(),addEventToGameRequest.getPlayerUsername());
        if (event != null) {
            Notification.sendGameEventNotification(simpleMessagingTemplate, event.getGameID(), event.toString());
            return StatusResponse.getTrueStatusObj();
        }
        return StatusResponse.getFalseStatusObj();
    }

    // ------------------------4.2.Referee Create Game Report-------
    @PostMapping("/createGameReport")
    public StatusResponse createGameReport(@RequestBody CreateReportRequest createReportRequest) {
        if (domainController.createGameReport(createReportRequest.getRefereeUsername(),createReportRequest.getGameID(),createReportRequest.getReport())) {
            return StatusResponse.getTrueStatusObj();
        }
        return StatusResponse.getFalseStatusObj();
    }


    // ------------------------***others***-------
    // ------------------------test getter-------
    @GetMapping("/testGetter")
    public StatusResponse getTrueStatus() {
        return StatusResponse.getTrueStatusObj();
    }

    // ------------------------get all leagues-------
    @GetMapping("/getLeagueNames")
    public ArrayList<String> getLeagueNames() {
        return domainController.getAllLeaguesNames();
    }

    //------------------------getRefereeActiveGame-----
    @GetMapping("/getRefereeActiveGame")
    public GameIdentifierResponse getRefereeActiveGame(@RequestParam("refereeUsername") String refereeUsername){
        GameIdentifier gameIdentifier = domainController.getRefereeActiveGame(refereeUsername);
        if (gameIdentifier==null) {
            return null;
        }
        return new GameIdentifierResponse(gameIdentifier.getGameID(),gameIdentifier.getHostTeamName(),gameIdentifier.getGuestTeamName());
    }

    //------------------------getRefereeReportActiveGame-----
    @GetMapping("/getRefereeReportActiveGame")
    public GameIdentifierResponse getRefereeReportActiveGame(@RequestParam("refereeUsername") String refereeUsername){
        GameIdentifier gameIdentifier = domainController.getRefereeReportActiveGame(refereeUsername);
        if (gameIdentifier==null) {
            return null;
        }
        return new GameIdentifierResponse(gameIdentifier.getGameID(),gameIdentifier.getHostTeamName(),gameIdentifier.getGuestTeamName());
    }

    //------------------------getAllTeamPlayers-----
    @GetMapping("/getTeamPlayerNames")
    public ArrayList<String> getAllTeamPlayers(@RequestParam("teamName") String teamName){
        return domainController.getAllTeamPlayers(teamName);
    }

    // ------------------resetSystem (Dangerous!):----
    @PostMapping("/resetSystem")
    public void resetSystem(@RequestBody ResetSystemRequest resetSystemRequest) {
        try { domainController.performResetSystem(resetSystemRequest.getSysManagerUserName()); }
        catch (Exception e) {}
    }

    //------------------------getAllLeagueSeasons-----
    @GetMapping("/getAllLeagueSeasons")
    public ArrayList<Integer> getAllLeagueSeasons(@RequestParam("leagueName") String teamName){
        return domainController.getAllLeagueSeasons(teamName);
    }

    //------------------------Logout-----
    @PostMapping("/logout")
    public StatusResponse logout(@RequestBody LogoutRequest logoutRequest){
        if (domainController.logout(logoutRequest.getUsername())){
            return StatusResponse.getTrueStatusObj();
        }
        return StatusResponse.getFalseStatusObj();
    }












}
