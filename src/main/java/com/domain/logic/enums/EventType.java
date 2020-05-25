package com.domain.logic.enums;

import org.apache.catalina.valves.rewrite.Substitution;

public enum EventType {
    HostGoal,
    GuestGoal,
    Offside,
    Foul,
    YellowCard,
    RedCard,
    Injury,
    Substitution;

    public static EventType strToEventType(String type) {
        switch(type) {
            case "HostGoal":
                return  HostGoal;
            case "GuestGoal":
                return GuestGoal;
            case "Offside":
                return Offside;
            case  "Foul" :
                return Foul;
            case "YellowCard":
                return YellowCard;
            case "RedCard":
                return RedCard;
            case "Injury":
                return Injury;
            case "Substitution":
                return Substitution;
            default:
                return null;
        }
    }
}
