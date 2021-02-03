package me.kodysimpson.littybank.models;

import org.bukkit.inventory.ItemStack;

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

    public SavingsAccount() {}

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

    public static String formatId(int id) {
        String stringID = String.valueOf(id);
        while (stringID.length()<8) {
            stringID = "0" + stringID;
        }
        return "#" + stringID;
    }

    public static boolean isValidAccount(ItemStack item) {
        return item.getItemMeta().getDisplayName().contains("Account Number");
    }
}
