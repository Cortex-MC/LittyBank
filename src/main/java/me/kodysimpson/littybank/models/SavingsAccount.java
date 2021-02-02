package me.kodysimpson.littybank.models;

import java.util.UUID;

/**
 * Used to model a player "Savings account" to store money with interest
 */
public class SavingsAccount {

    //database id, can serve also as bank account id shown in game
    private int id;

    private UUID accountOwner;
    private AccountTier tier;
    private double balance;

    public SavingsAccount(int id, UUID accountOwner, AccountTier tier, double balance) {
        this.id = id;
        this.accountOwner = accountOwner;
        this.tier = tier;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(UUID accountOwner) {
        this.accountOwner = accountOwner;
    }

    public AccountTier getTier() {
        return tier;
    }

    public void setTier(AccountTier tier) {
        this.tier = tier;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
