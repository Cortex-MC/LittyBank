package me.kodysimpson.littybank.utils;

import me.kodysimpson.littybank.models.SavingsAccount;

import java.util.Comparator;

public class SavingsAccountsComparator implements Comparator<SavingsAccount> {

    @Override
    public int compare(SavingsAccount a, SavingsAccount b) {
        if (a.getTier() == b.getTier()) {
            return a.getBalance() > b.getBalance() ? 1 : -1;
        }
        return a.getTier().getAsString().compareTo(b.getTier().getAsString());
    }
}
