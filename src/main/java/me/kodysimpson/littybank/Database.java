package me.kodysimpson.littybank;

import me.kodysimpson.littybank.models.AccountTier;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.littybank.utils.SavingsAccountsComparator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {

    //Create and establish connection with SQL Database
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");

            try {
                connection = DriverManager.getConnection(LittyBank.getConnectionUrl());

            } catch (SQLException e) {
                System.out.println("Unable to establish a connection with the database");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Unable to find the h2 DB sql driver");
        }
        return connection;
    }

    //Initialize database tables
    public static void initializeDatabase() {

        try {

            //Create the desired tables for our database if they don't exist
            Statement statement = getConnection().createStatement();
            //Table for storing all of the locks
            statement.execute("CREATE TABLE IF NOT EXISTS SavingsAccounts(AccountID int NOT NULL IDENTITY(1, 1), AccountTier varchar(255), OwnerUUID varchar(255), Balance DECIMAL(30,3));");

            System.out.println("Database loaded");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Database initialization error.");
            e.printStackTrace();
        }


    }

    //Create a new collector in the database
    public static int createAccount(SavingsAccount savingsAccount) {

        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("INSERT INTO SavingsAccounts(AccountTier, OwnerUUID, Balance) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, savingsAccount.getTier().getAsString());
            statement.setString(2, savingsAccount.getAccountOwner().toString());
            statement.setDouble(3, savingsAccount.getBalance());


            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

    public static List<SavingsAccount> getAccounts(Player player) {
        String uuid = player.getUniqueId().toString();
        List<SavingsAccount> accounts = new ArrayList<>();

        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM SavingsAccounts WHERE OwnerUUID = ?");
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int AccountID = result.getInt(1);
                AccountTier tier = AccountTier.matchTier(result.getString(2));
                double balance = result.getDouble(4);
                accounts.add(new SavingsAccount(AccountID, player.getUniqueId(), tier, balance));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Collections.sort(accounts, new SavingsAccountsComparator());
        return accounts;
    }

    public static void deleteAccount(int id) {

        try {

            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM SavingsAccounts WHERE AccountID = ?");
            statement.setInt(1, id);

            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static double getBalance(int id) {

        try {

            PreparedStatement statement = getConnection().prepareStatement("SELECT Balance FROM SavingsAccounts WHERE AccountID = ?");
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            result.next();
            return result.getDouble(1);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

}

