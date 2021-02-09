package me.kodysimpson.littybank.utils;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.models.AccountTier;

public class AccountUtils {

    public static double getAccountPrice(AccountTier accountTier){

        switch (accountTier){
            case SILVER:
                return LittyBank.getPlugin().getConfig().getDouble("savings-accounts.SILVER.price");
            case GOLD:
                return LittyBank.getPlugin().getConfig().getDouble("savings-accounts.GOLD.price");
            case PLATINUM:
                return LittyBank.getPlugin().getConfig().getDouble("savings-accounts.PLATINUM.price");
        }

        return 0;
    }

    public static double getAccountInterest(AccountTier accountTier){

        switch (accountTier){
            case SILVER:
                return LittyBank.getPlugin().getConfig().getDouble("savings-accounts.SILVER.interest");
            case GOLD:
                return LittyBank.getPlugin().getConfig().getDouble("savings-accounts.GOLD.interest");
            case PLATINUM:
                return LittyBank.getPlugin().getConfig().getDouble("savings-accounts.PLATINUM.interest");
        }

        return 0;
    }

    public static String getAccountTierName(AccountTier accountTier){

        switch (accountTier){
            case SILVER:
                return LittyBank.getPlugin().getConfig().getString("savings-accounts.SILVER.tierName");
            case GOLD:
                return LittyBank.getPlugin().getConfig().getString("savings-accounts.GOLD.tierName");
            case PLATINUM:
                return LittyBank.getPlugin().getConfig().getString("savings-accounts.PLATINUM.tierName");
        }

        return null;
    }

}
