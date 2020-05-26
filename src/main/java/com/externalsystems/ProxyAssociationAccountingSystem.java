package com.externalsystems;

import com.data.DBCommunicator;
import com.data.Dao;

import java.util.Date;

public class ProxyAssociationAccountingSystem implements IAccountingSystem{

    private IAccountingSystem accountingSystem = new AssociationAccountingSystem();

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        if (date.compareTo(new Date().toString()) < 0){
            // if the given date is in the past
            return false;
        }
        if (checkIfValidTeamName(teamName))
            return accountingSystem.addPayment(teamName, date, amount);
        return false;
    }

    private boolean checkIfValidTeamName(String teamName){
        Dao dao = DBCommunicator.getInstance();
        try {
            for (String currentTeamName : dao.getAllTeamNames()){
                if (currentTeamName.equals(teamName))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
