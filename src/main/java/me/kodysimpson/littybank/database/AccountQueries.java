package me.kodysimpson.littybank.database;

import me.kodysimpson.littybank.models.CheckingAccount;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class AccountQueries {

    public static CheckingAccount getAccountForPlayer(Player p){
        CheckingAccount checkingAccount = null;
        try{
            checkingAccount = Database.getCheckingsDao().queryForId(p.getUniqueId());
            if (checkingAccount == null){
                //Create an account for the player
                checkingAccount = new CheckingAccount();
                checkingAccount.setBalance(0.0);
                checkingAccount.setOwner(p.getUniqueId());
                Database.getCheckingsDao().create(checkingAccount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return checkingAccount;
    }

}
