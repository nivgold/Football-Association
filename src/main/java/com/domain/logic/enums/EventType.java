package com.domain.logic.enums;

public enum EventType {
    Goal,
    Offside,
    Foul,
    YellowCard,
    RedCard,
    Injury,
    Substitution;

    public static EventType strToEventType(String type) {
        switch(type) {
            case "Goal":
                return  Goal;
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
