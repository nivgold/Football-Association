package com.domain.logic.roles;


import com.domain.logic.AssociationSystem;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.users.Member;
import com.logger.Logger;

import java.util.ArrayList;

public class AssociationAgent implements IRole {

    private Member member;

    public AssociationAgent(Member member) throws Exception {
        this.member = member;
        member.addAssociationAgent(this);
    }

    /**
     * creates new league.
     * @param name
     */
    public void createLeague(String name) {
        AssociationSystem system = AssociationSystem.getInstance();
        //TODO call DAO to find the league
        if (system.getManageLeagues().findLeague(name) == null){
            League league = new League(name);
            Logger.getInstance().saveLog("League: "+name+" was added to the system");
        }
        else{
            Logger.getInstance().saveLog("the League:"+name+" already exists");
        }

    }

    /**
     * creates new season and add it to the relevant league.
     * @param l
     * @param year
     */
    public void createSeasonInLeague(League l, int year) {
        AssociationSystem system = AssociationSystem.getInstance();
        //check if the league already exists
        //TODO call DAO to find the league
        if (system.getManageLeagues().findLeague(l.getLeagueName()) != null) {
            boolean seasonExists = false;

            for (Season s: l.getSeasons()) {
                if (s.getYear() == year) {
                    seasonExists = true;
                    System.out.println("The season already belongs to the league");
                    break;
                }
            }
            if (!seasonExists) {
                //TODO call DAO to find season
                Season s = AssociationSystem.getInstance().getManageSeasons().findSeason(year);
                if (s==null) {
                    s = new Season(year);
                }
                l.addSeason(s);
                Logger.getInstance().saveLog("The season has been added successfully to the league");
            }
        }
        else {
            Logger.getInstance().saveLog("The league does not exists.");
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
                Logger.getInstance().saveLog("member already a Referee");
                return;
            }
        }
        Referee referee = new Referee(member);
        Logger.getInstance().saveLog("memeber -> "+member+" is Referee");
    }

    /**
     * removes existing referee from the system
     * @param r
     */
    public void removeReferee(Referee r) throws Exception {
        r.removeYourself();
        Logger.getInstance().saveLog("The referee has been removed successfully");
    }

    /**
     * set referee to a given league in a given season
     * @param r
     * @param l
     * @param s
     */
    public void setRefereeInLeague(Referee r, League l, Season s) {
        AssociationSystem system = AssociationSystem.getInstance();
        //check if the league is valid
        //TODO call DAO to find league
        if (system.getManageLeagues().findLeague(l.getLeagueName()) != null) {
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
            // TODO - update dao
        }
        else {
            throw new Exception("The current ranking policy is the same as the ranking policy you have entered");
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
            System.out.println("The ranking policy changed successfully");
            // TODO - update dao
        }
        else {
            throw new Exception("cannot update the game setting policy");
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
