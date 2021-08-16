package me.kodysimpson.littybank.tasks;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.database.Database;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.littybank.utils.AccountUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.List;

public class InterestTask extends BukkitRunnable {
    @Override
    public void run() {

        List<SavingsAccount> accounts = null;
        try {
            accounts = Database.getSavingsDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (accounts != null) {
            for (SavingsAccount account : accounts){

                double interestRate = AccountUtils.getAccountInterest(account.getTier());
                double currentAmount = account.getBalance();
                double gained = currentAmount * interestRate;

                account.setBalance(currentAmount + gained);

                //update the account with the new interest
                try {
                    Database.getSavingsDao().update(account);

                    //Message the player if they are online
                    if (LittyBank.getPlugin().getAccountConfig().isAlertInterestGained()){
                        OfflinePlayer player = Bukkit.getOfflinePlayer(account.getOwner());
                        if (player.isOnline()){
                            player.getPlayer().sendMessage("Your " + account.getTier() + " savings account has just earned $" + gained + ".");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
