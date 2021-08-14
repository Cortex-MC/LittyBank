package me.kodysimpson.littybank.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.models.CheckingAccount;
import me.kodysimpson.littybank.models.SavingsAccount;
import org.bukkit.Location;

import java.sql.*;
import java.util.UUID;

public class Database {

//    private static final String url = "jdbc:h2:mem:littybank";
    private static final String url = "jdbc:mysql://localhost:3306/test";

    private static ConnectionSource connectionSource;

    private static Dao<CheckingAccount, UUID> checkingsDao;
    private static Dao<SavingsAccount, Integer> savingsDao;
    private static Dao<ATM, Integer> atmDao;

    public static ConnectionSource getConnectionSource(){

        if (connectionSource == null){

            try{
                connectionSource = new JdbcConnectionSource(url, "root", "");
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return connectionSource;
    }

    //Initialize database tables
    public static void initializeDatabase() {

        try {

            checkingsDao = DaoManager.createDao(getConnectionSource(), CheckingAccount.class);
            savingsDao = DaoManager.createDao(getConnectionSource(), SavingsAccount.class);
            atmDao = DaoManager.createDao(getConnectionSource(), ATM.class);

            TableUtils.createTableIfNotExists(connectionSource, CheckingAccount.class);
            TableUtils.createTableIfNotExists(connectionSource, SavingsAccount.class);
            TableUtils.createTableIfNotExists(connectionSource, ATM.class);

        } catch (SQLException e) {
            System.out.println("Database initialization error.");
            e.printStackTrace();
        }


    }

    public static Dao<SavingsAccount, Integer> getSavingsDao() {
        return savingsDao;
    }

    public static Dao<CheckingAccount, UUID> getCheckingsDao() {
        return checkingsDao;
    }

    public static Dao<ATM, Integer> getAtmDao() {
        return atmDao;
    }

    //Create a new collector in the database
    public static double getBalance(int id) {

//        try {
//
//            PreparedStatement statement = getConnection().prepareStatement("SELECT Balance FROM SavingsAccounts WHERE AccountID = ?");
//            statement.setInt(1, id);
//
//            ResultSet result = statement.executeQuery();
//            result.next();
//            return result.getDouble(1);
//
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        return 0;
    }

    public static void setBalance(int id, double balance) {
//
//        try {
//
//            PreparedStatement statement = getConnection().prepareStatement("UPDATE SavingsAccounts SET Balance = ? WHERE AccountID = ?");
//            statement.setDouble(1, balance);
//            statement.setInt(2, id);
//
//            statement.execute();
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
    }







    // ------------------------------------ < ATM STUFF > -----------------------------------------------


    public static void addATM(ATM atm) {

//        String uuid = atm.getOwner().getUniqueId().toString();
//        Location location = atm.getLocation();
//
//        try {
//            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO ATM(Owner, Location) VALUES (?, ?);");
//            statement.setString(1, uuid);
//            statement.setString(2, Serializer.serializeLocation(location));
//
//            statement.execute();
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

    }

    public static boolean isATMLocation(Location location) {

//        try {
//            PreparedStatement statement = getConnection().prepareStatement("SELECT COUNT(Location) FROM ATM WHERE Location = ?;");
//            statement.setString(1, Serializer.serializeLocation(location));
//
//            ResultSet result = statement.executeQuery();
//            result.next();
//
//            return result.getInt(1) > 0;
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        return false;
    }

    public static void deleteATM(ATM atm) {

//        Location location = atm.getLocation();
//
//        try {
//            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM ATM WHERE Location = ?;");
//            statement.setString(1, Serializer.serializeLocation(location));
//
//            statement.execute();
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
    }

    public static ATM getATM(Location location) {

//        try {
//
//            PreparedStatement statement = getConnection().prepareStatement("SELECT Owner FROM ATM WHERE Location = ?");
//            statement.setString(1, Serializer.serializeLocation(location));
//
//            ResultSet result = statement.executeQuery();
//            result.next();
//
//            Player player = Bukkit.getPlayer(UUID.fromString(result.getString(1)));
//            return new ATM(player, location);
//
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        return null;
    }

}

