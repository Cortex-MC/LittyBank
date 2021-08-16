package me.kodysimpson.littybank.utils;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.models.AccountTier;

public class AccountUtils {

    public static double getTierCost(AccountTier accountTier){
        return LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(accountTier).getCost();
    }

    public static double getAccountInterest(AccountTier accountTier){
        return LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(accountTier).getInterestRate();
    }

    public static String getAccountTierName(AccountTier accountTier){
        return LittyBank.getPlugin().getAccountConfig().getSavingsAccountTiers().get(accountTier).getDisplayName();
    }

    public static AccountTier getPreviousTier(AccountTier accountTier){
        if (accountTier == AccountTier.GOLD){
            return AccountTier.SILVER;
        }else if (accountTier == AccountTier.PLATINUM){
            return AccountTier.GOLD;
        }else{
            return null;
        }
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

    public static String getPrettyBalance(double balance){
        return String.format("%,.2f", balance);
    }

}
