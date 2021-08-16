package me.kodysimpson.littybank.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@DatabaseTable(tableName = "checkingaccounts")
@Data
@NoArgsConstructor
public class CheckingAccount {

    @DatabaseField(id = true)
    private UUID owner;
    @DatabaseField
    private double balance;

    public String getPrettyBalance(){
        return String.format("%,.2f", this.balance);
    }

//    public String formatId() {
//        String stringID = String.valueOf(id);
//        while (stringID.length()<8) {
//            stringID = "0" + stringID;
//        }
//        return "#" + stringID;
//    }

}
