package com.domain.logic;

import com.domain.domaincontroller.managers.ManageLeagues;
import com.domain.domaincontroller.managers.ManageMembers;
import com.domain.domaincontroller.managers.ManageSeasons;
import com.domain.domaincontroller.managers.ManageTeams;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.roles.AssociationAgent;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;
import com.externalsystems.AssociationAccountingSystem;
import com.externalsystems.CountryTaxLawSystem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AssociationSystem {
    private static AssociationSystem system;

    private ManageMembers manageMembers;
    private ManageLeagues manageLeagues;
    private ManageTeams manageTeams;
    private ManageSeasons manageSeasons;
    private AssociationAccountingSystem associationAccountingSystem;
    private CountryTaxLawSystem countryTaxLawSystem;

    private AssociationSystem() {
        this.manageMembers = ManageMembers.getInstance();
        this.manageLeagues = ManageLeagues.getInstance();
        this.manageTeams = ManageTeams.getInstance();
        this.manageSeasons = ManageSeasons.getInstance();
    }

    public static AssociationSystem getInstance()
    {
        if (system == null)
            system = new AssociationSystem();

        return system;
    }

    public ManageMembers getManageMembers() {
        return manageMembers;
    }

    public ManageTeams getManageTeams() {
        return manageTeams;
    }

    public ManageLeagues getManageLeagues() {
        return manageLeagues;
    }

    public ManageSeasons getManageSeasons() {
        return manageSeasons;
    }

    public void clearSystem(){
        manageMembers.removeAllMembers();
        manageLeagues.removeAllLeagues();
        manageSeasons.removeAllSeasons();
        manageTeams.removeAllTeams();

    }

    public boolean resetSystem(){
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
                String memIRole=(String) memClose.get("role");
                if(memIRole.equals("referee")){
                    Referee referee = new Referee(member);
                }
                else if(memIRole.equals("associationAgent")){
                    AssociationAgent associationAgent = new AssociationAgent(member);
                }
                else{//add role of team owner
                    Team[] teams = new Team[2];
                    JSONArray jsonArrayTeams = (JSONArray) array.get(2);
                    JSONObject teamClose = (JSONObject) ((JSONObject) jsonArrayTeams.get(counter)).get("team");
                    String teamName = (String) teamClose.get("teamName") ;
                    String teamStatus = (String) teamClose.get("TeamStatus") ;
                    TeamOwner teamOwner = new TeamOwner(member, teamName, TeamStatus.valueOf(teamStatus), fields[counter]);

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
}
