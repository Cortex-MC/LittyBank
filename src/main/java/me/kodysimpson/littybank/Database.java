package me.kodysimpson.littybank;

import me.kodysimpson.littybank.models.SavingsAccount;
import java.sql.*;

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
            statement.execute("CREATE TABLE IF NOT EXISTS SavingsAccounts(AccountID int NOT NULL IDENTITY(1, 1), AccountTier varchar(255), OwnerUUID varchar(255), Balance double);");

            System.out.println("Database loaded");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Database initialization error.");
        }


    }

    //Create a new collector in the database
    public static int createAccount(SavingsAccount savingsAccount) {

        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("INSERT INTO Collectors(AccountTier, OwnerUUID, Balance) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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

}

