package me.kodysimpson.littybank.models;

import org.bukkit.inventory.ItemStack;

import java.util.Date;
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

    private Date lastUpdated;
    private Date lastChecked;

    public SavingsAccount() {}

    public SavingsAccount(int id, UUID accountOwner, AccountTier tier, double balance, Date lastUpdated, Date lastChecked) {
        this.id = id;
        this.accountOwner = accountOwner;
        this.tier = tier;
        this.balance = balance;
        this.lastUpdated = lastUpdated;
        this.lastChecked = lastChecked;
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Date lastChecked) {
        this.lastChecked = lastChecked;
    }

    public static String formatId(int id) {
        String stringID = String.valueOf(id);
        while (stringID.length()<8) {
            stringID = "0" + stringID;
        }
        return "#" + stringID;
    }

    public static boolean isValidAccount(ItemStack item) {
        return item.getItemMeta().getDisplayName().contains("Account");
    }
}
