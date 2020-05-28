package com.stubs;

import com.domain.logic.AssociationSystem;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.SeasonInLeague;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.roles.AssociationAgent;
import com.domain.logic.roles.IRole;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.Member;
import com.logger.EventLogger;

import java.util.ArrayList;

public class AssociationAgentStub extends AssociationAgent {
    private Member member;
    private DBStub dbStub;

    public AssociationAgentStub(Member member) throws Exception {
        super(member);
        dbStub = DBStub.getInstance();
    }

    /**
     * creates new league.
     * @param name
     */
    public void createLeague(String name) throws Exception {
        if (dbStub.findLeague(name) == null){
            League league = new League(name);
            dbStub.addLeague(league);
            EventLogger.getInstance().saveLog("League: "+name+" was added to the system");
        }
        else{
            EventLogger.getInstance().saveLog("the League:"+name+" already exists");
        }

    }

    /**
     * creates new season and add it to the relevant league.
     * @param l
     * @param year
     */
    public void createSeasonInLeague(League l, int year) throws Exception {
        if(dbStub.findLeague(l.getLeagueName()) == null){
            EventLogger.getInstance().saveLog("The league " + l.getLeagueName() + " doesn't exists");
            return;
        }
        if(dbStub.findSeason(year) == null){
            EventLogger.getInstance().saveLog("The league " + l.getLeagueName() + " doesn't exists");
            return;
        }
        //check if the league already exists
        SeasonInLeague seasonInLeague = dbStub.findSeasonInLeague(year, l.getLeagueName());
        if(seasonInLeague != null){
            EventLogger.getInstance().saveLog("The season already belongs to the league");
        }
        else {
            boolean succeed = dbStub.addSeasonInLeague(year, l.getLeagueName());
            if (succeed)
                EventLogger.getInstance().saveLog("The season has been added successfully to the league");
        }
    }


    /**
     * adds new referee to the system
     * @param member
     */
    public void appointReferee(Member member) throws Exception {
        // check if member already a referee in the system
        for (IRole role : member.getRoles()){
            if (role instanceof Referee) {
                EventLogger.getInstance().saveLog("member already a Referee");
                return;
            }
        }
        dbStub.appointReferee(member.getUserName());
        EventLogger.getInstance().saveLog("memeber -> "+member+" is Referee");
    }

    /**
     * removes existing referee from the system
     * @param r
     */
    public void removeReferee(Referee r) throws Exception {
        r.removeYourself();
        dbStub.removeReferee(r.getMember().getUserName());
        EventLogger.getInstance().saveLog("The referee has been removed successfully");
    }

    /**
     * set referee to a given league in a given season
     * @param r
     * @param l
     * @param s
     */
    public void setRefereeInLeague(Referee r, League l, Season s) throws Exception {
        //check if the league is valid
        if (dbStub.findLeague(l.getLeagueName()) != null) {
            //check if the season is valid
            if (l.getSeasons().contains(s)) {
                //check if the referee is valid
                if(l.getLeagueRefereeMap().get(s)!=null) {
                    if (l.getLeagueRefereeMap().get(s).contains(r))
                        System.out.println("The referee already exists in this league and season");
                }
                else {
                    if (l.getLeagueRefereeMap().get(s) == null)
                        l.getLeagueRefereeMap().put(s, new ArrayList<>());
                    l.getLeagueRefereeMap().get(s).add(r);
                    r.getLeagues().add(l);
                    System.out.println("The referee has been added to the league's season successfully");
                }
            }
            System.out.println("The season does not exists in the league");
        }
        System.out.println("The league does not exists");
    }


    /**
     * set ranking policy to a given league and season
     * @param l
     * @param s
     * @param rp
     */
    public void setRankingPolicy(League l, Season s, RankingPolicy rp) throws Exception {
        RankingPolicy current = l.getSeasonLeaguePolicy().get(s).getRankingPolicy();
        if (current != rp) {
            l.setRankingPolicy(s, rp);
            System.out.println("The ranking policy changed successfully");
        }
        else {
            throw new Exception("cannot perform the operation");
        }
    }

    /**
     * set game setting policy to a given league and season
     * @param l
     * @param s
     * @param gsp
     */
    public void setGameSettingPolicy(League l, Season s, GameSettingPolicy gsp) throws Exception {

        GameSettingPolicy current = l.getSeasonLeaguePolicy().get(s).getGameSettingPolicy();
        if (current== null || current.getSettingStrategy().getClass() != gsp.getSettingStrategy().getClass()) {
            l.setGameSettingPolicy(s, gsp);
        }
        else {
            throw new Exception("cannot perform the operation");
        }
    }

    /**
     * remove the object from all occurrences
     * @return
     */
    @Override
    public boolean removeYourself() throws Exception {
        member.removeAssociationAgent(this);
        return true;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }


}
