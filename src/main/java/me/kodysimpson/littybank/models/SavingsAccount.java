package me.kodysimpson.littybank.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * Used to model a player "Savings account" to store money with interest
 */
@DatabaseTable(tableName = "savingsaccounts")
@Data
public class SavingsAccount {

    //database id, can serve also as bank account id shown in game
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private UUID owner;
    @DatabaseField
    private AccountTier tier;
    @DatabaseField
    private double balance;

    public SavingsAccount() {}

    public SavingsAccount(int id, UUID owner, AccountTier tier, double balance) {
        this.id = id;
        this.owner = owner;
        this.tier = tier;
        this.balance = balance;
    }

    public String getPrettyBalance(){
        return String.format("%,.2f", this.balance);
    }

    public static String formatId(int id) {
        String stringID = String.valueOf(id);
        while (stringID.length()<8) {
            stringID = "0" + stringID;
        }
        return "#" + stringID;
    }
}
