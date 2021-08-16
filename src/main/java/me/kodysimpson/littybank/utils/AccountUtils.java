package me.kodysimpson.littybank.utils;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.models.AccountTier;

public class AccountUtils {

    public static double getAccountPrice(AccountTier accountTier){
        return LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(accountTier).getCost();
    }

    public static double getAccountInterest(AccountTier accountTier){
        return LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(accountTier).getInterestRate();
    }

    public static String getAccountTierName(AccountTier accountTier){
        return LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(accountTier).getDisplayName();
    }

    public static AccountTier getNextTier(AccountTier accountTier){
        if (accountTier == AccountTier.SILVER){
            return AccountTier.GOLD;
        }else if (accountTier == AccountTier.GOLD){
            return AccountTier.PLATINUM;
        }else{
            return null;
        }
    }

}
