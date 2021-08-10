package me.kodysimpson.littybank.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.kodysimpson.littybank.LittyBank;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Date;
import java.util.UUID;

/**
 * Used to model a player "Savings account" to store money with interest
 */
@DatabaseTable(tableName = "savingsaccounts")
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

    @DatabaseField
    private Date lastUpdated;
    @DatabaseField
    private Date lastChecked;

    public SavingsAccount() {}

    public SavingsAccount(int id, UUID owner, AccountTier tier, double balance, Date lastUpdated, Date lastChecked) {
        this.id = id;
        this.owner = owner;
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

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
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
}
