package me.kodysimpson.littybank.tasks;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.littybank.utils.AccountUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class InterestTask extends BukkitRunnable {
    @Override
    public void run() {

        List<SavingsAccount> accounts = Database.getAllAccounts();
        for (SavingsAccount account : accounts){

            double interestRate = AccountUtils.getAccountInterest(account.getTier());
            double currentAmount = account.getBalance();
            double gained = currentAmount * interestRate;

            account.setBalance(currentAmount + gained);

            //update the account with the new interest
            Database.updateSavingsAccount(account);
            //System.out.println("pickle lord 866");
        }

    }
}
