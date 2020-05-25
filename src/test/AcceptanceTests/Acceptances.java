public class Acceptances {
    public static void main(String[] args) {
        String manager = args[0];
        String UC_NAME = args[1];

        switch (manager){
            case "system":
            {
                SystemAcceptanceTest.activate(UC_NAME);
                break;
            }
            case "system_manager":
            {
                SystemManagerAcceptanceTest.activate(UC_NAME);
                break;
            }
            case "member":
            {
                MemberAcceptanceTest.activate(UC_NAME);
                break;
            }
            case "guest":
            {
                GuestAcceptanceTest.activate(UC_NAME);
                break;
            }
            case "team_owner":
            {
                TeamOwnerAcceptanceTest.activate(UC_NAME);
                break;
            }
            case "association_agent":
            {
                AssociationAgentAcceptanceTest.activate(UC_NAME);
                break;
            }
            case "referee":
            {
                RefereeAcceptanceTest.activate(UC_NAME);
                break;
            }
            case "coverage":
            {
                CoverageAcceptance.activate(UC_NAME);
            }
        }
    }
}
