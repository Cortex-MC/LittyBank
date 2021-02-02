package me.kodysimpson.littybank.models;

public enum AccountTier {

    SILVER, GOLD, PLATINUM;

    public double getInterestRate(){

        switch (this){
            case SILVER:
                return 0.5;
            case GOLD:
                return 1.0;
            case PLATINUM:
                return 1.5;
            default:
                return 0;
        }

    }

    public String getAsString(){

        switch (this){
            case SILVER:
                return "SILVER";
            case GOLD:
                return "GOLD";
            case PLATINUM:
                return "PLATINUM";
            default:
                return null;
        }

    }

}
