package me.kodysimpson.littybank.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Serializer {

    public static String serializeLocation(Location location) {

        //Serialize the location object into a string
        try{
            //Serialize the item(turn it into byte stream)
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(location);
            os.flush();

            byte[] serializedObject = io.toByteArray();

            //Encode the serialized object into to the base64 format
            return new String(Base64.getEncoder().encode(serializedObject));

        }catch (IOException ex){
            ex.printStackTrace();
        }

        return null;
    }

    public static Location deserializeLocation(String location) {

        try{

            byte[] serializedObject = Base64.getDecoder().decode(location);

            //Input stream to read the byte array
            ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);
            //object input stream to serialize bytes into objects
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);

            //Use the object input stream to deserialize an object from the raw bytes
            return (Location) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
