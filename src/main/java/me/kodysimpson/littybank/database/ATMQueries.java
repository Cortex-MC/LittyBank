package me.kodysimpson.littybank.database;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.littybank.utils.Serializer;
import org.bukkit.Location;

import java.sql.SQLException;

public class ATMQueries {

    public static ATM getATMFromLocation(Location location) throws SQLException {

        QueryBuilder<ATM, Integer> queryBuilder = Database.getAtmDao().queryBuilder();
        queryBuilder.where().eq("location", Serializer.serializeLocation(location));

        return Database.getAtmDao().queryForFirst(queryBuilder.prepare());
    }

    public static void updateBalance(SavingsAccount account){



    }

}
