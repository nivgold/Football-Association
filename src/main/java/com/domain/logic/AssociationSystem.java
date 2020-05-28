package com.domain.logic;

import com.domain.logic.data_types.Address;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.managers.ManageLeagues;
import com.domain.logic.managers.ManageMembers;
import com.domain.logic.managers.ManageSeasons;
import com.domain.logic.managers.ManageTeams;
import com.domain.logic.roles.AssociationAgent;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;
import com.externalsystems.AssociationAccountingSystem;
import com.externalsystems.CountryTaxLawSystem;
import com.logger.EventLogger;
import com.stubs.DBStub;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class AssociationSystem {
    private static AssociationSystem system;

    private static HashSet<Member> connectedUsers = new HashSet<>();

    private ManageLeagues manageLeagues;
    private ManageMembers manageMembers;
    private ManageSeasons manageSeasons;
    private ManageTeams manageTeams;
    private DBStub dbStub;

    private AssociationAccountingSystem associationAccountingSystem;
    private CountryTaxLawSystem countryTaxLawSystem;

    private AssociationSystem() {
        this.manageLeagues = ManageLeagues.getInstance();
        this.manageMembers = ManageMembers.getInstance();
        this.manageSeasons = ManageSeasons.getInstance();
        this.manageTeams = ManageTeams.getInstance();
        this.dbStub = DBStub.getInstance();
    }

    public static AssociationSystem getInstance()
    {
        if (system == null)
            system = new AssociationSystem();

        return system;
    }

    public void logOutUser(Member member){
        connectedUsers.remove(member);
    }

    public void connectUser(Member member){
        connectedUsers.add(member);
        EventLogger.getInstance().saveLog("adding member: \""+member.getUserName()+"\" to connected member");
    }

    public Member findConnectedUser(String username) throws Exception {
        EventLogger.getInstance().saveLog("finding connected member with username: '"+username+"' ...");
        for (Member member : connectedUsers){
            if (member.getUserName().equals(username)) {
                EventLogger.getInstance().saveLog("'"+username+"' has found in connected users!");
                return member;
            }
        }
        throw new Exception("user not found in connected users");
    }

    public boolean checkIfConnected(Member member){
        return connectedUsers.contains(member);
    }

    public void clearSystem(){
        //TODO remove all data from DB
    }

    public boolean resetSystem() throws Exception {
        clearSystem();

        this.countryTaxLawSystem = new CountryTaxLawSystem();
        this.associationAccountingSystem = new AssociationAccountingSystem();

        JSONParser parser = new JSONParser();
        String baseFilePath = "src\\main\\resources\\base_file.json";
        try {
            FileReader fileReader = new FileReader(new File(baseFilePath));
            Object obj = parser.parse(fileReader);
            JSONArray array = (JSONArray) obj;
            JSONObject jsonObjectSystemManager = (JSONObject) array.get(0);
            JSONObject sysMemClose = (JSONObject) jsonObjectSystemManager.get("systemManagerMember");
            String sysMemUserName = (String) sysMemClose.get("userName");
            String sysMemUserPass = (String) sysMemClose.get("password");
            SystemManagerMember systemManagerMember = new SystemManagerMember(sysMemUserName, sysMemUserPass, "email",new Address("", "", "", ""), "name");
            dbStub.addMember(systemManagerMember);

            Field[] fields = new Field[2];
            JSONArray jsonArrayFields = (JSONArray) array.get(3);
            for(int i=0; i<jsonArrayFields.size(); i++){
                JSONObject fieldClose = (JSONObject) ((JSONObject) jsonArrayFields.get(i)).get("field");
                String fieldCountry =(String) fieldClose.get("Country");
                String fieldState =(String) fieldClose.get("State");
                String fieldCity =(String) fieldClose.get("City");
                String fieldPostalCode =(String) fieldClose.get("PostalCode");
                fields[i] = new Field(fieldCountry, fieldState, fieldCity, fieldPostalCode);
            }
            int counter = 0;

            JSONArray jsonArrayMembers = (JSONArray) array.get(1);
            for(int i=0; i<jsonArrayMembers.size(); i++){
                JSONObject memClose = (JSONObject) ((JSONObject) jsonArrayMembers.get(i)).get("member");
                String memUserName =(String) memClose.get("userName");
                String memName =(String) memClose.get("name");
                String memPassword=(String) memClose.get("password");
                String hashPassword = SHA1Function.hash(memPassword);
                Member member = new Member(memUserName, hashPassword, "email" +i, new Address("" + i, "" +i, "" + i, "" + i), memName);
                dbStub.addMember(member);
                String memIRole=(String) memClose.get("role");
                if(memIRole.equals("referee")){
                    Referee referee = new Referee(member);
                }
                else if(memIRole.equals("associationAgent")){
                    AssociationAgent associationAgent = new AssociationAgent(member);
                }
                else{//add role of team owner
                    JSONArray jsonArrayTeams = (JSONArray) array.get(2);
                    JSONObject teamClose = (JSONObject) ((JSONObject) jsonArrayTeams.get(counter)).get("team");
                    String teamName = (String) teamClose.get("teamName") ;
                    String teamStatus = (String) teamClose.get("TeamStatus") ;
                    TeamOwner teamOwner = new TeamOwner(member);
                    Team team = new Team(teamName, TeamStatus.valueOf(teamStatus), teamOwner, fields[counter]);
                    dbStub.addTeam(team);
                    counter++;
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ManageLeagues getManageLeagues() {
        return manageLeagues;
    }

    public ManageMembers getManageMembers() {
        return manageMembers;
    }

    public ManageSeasons getManageSeasons() {
        return manageSeasons;
    }

    public ManageTeams getManageTeams() {
        return manageTeams;
    }
}
