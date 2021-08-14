package me.kodysimpson.littybank.models;

import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.utils.Serializer;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ATM {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private UUID owner;
    @DatabaseField(width = 2048)
    private String location;
    @DatabaseField
    private double balance;
    @DatabaseField
    private double earnings;

    public ATM(){

    }

    public ATM(UUID owner, Location location) {
        this.owner = owner;
        this.location = Serializer.serializeLocation(location);
    }

    public ATM(ItemStack item, Location location) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String uuid = container.get(new NamespacedKey(LittyBank.getPlugin(), "ATMOwner"), PersistentDataType.STRING);
        System.out.println("location string size: " + Serializer.serializeLocation(location).length());
        this.location = Serializer.serializeLocation(location);
        this.owner = UUID.fromString(uuid);
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

    public Location getLocation() {
        return Serializer.deserializeLocation(location);
    }

    public void setLocation(Location location) {
        this.location = Serializer.serializeLocation(location);
    }

    public static ItemStack createATM(Player player) {
        ItemStack atm = new ItemStack(Material.ANVIL, 1);
        ItemMeta meta = atm.getItemMeta();
        meta.setDisplayName(ColorTranslator.translateColorCodes("&#f5aa42&lATM"));

        List<String> lore = new ArrayList<>();
        lore.add(ColorTranslator.translateColorCodes("&7Owner: &a" + player.getName()));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(LittyBank.getPlugin(), "ATMOwner"), PersistentDataType.STRING, player.getUniqueId().toString());

        meta.setLore(lore);
        atm.setItemMeta(meta);

        return atm;
    }


    public static boolean isValidATM(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        return container.has(new NamespacedKey(LittyBank.getPlugin(), "ATMOwner"), PersistentDataType.STRING);
    }

    public static boolean isValidATM(Block block) {

        if (!(block.getState() instanceof TileState state)) return false;

        PersistentDataContainer container = state.getPersistentDataContainer();

        return container.has(new NamespacedKey(LittyBank.getPlugin(), "atmid"), PersistentDataType.INTEGER);
    }

    public static int getIdFromBlock(Block block){
        if (!(block.getState() instanceof TileState state)) return 0;

        PersistentDataContainer container = state.getPersistentDataContainer();

        return container.get(new NamespacedKey(LittyBank.getPlugin(), "atmid"), PersistentDataType.INTEGER);
    }
//
//    public static void setPDC(Block block, Player player) {
//        if (!(block.getState() instanceof TileState)) return;
//
//        TileState state = (TileState) block.getState();
//
//        PersistentDataContainer container = state.getPersistentDataContainer();
//
//        container.set(new NamespacedKey(LittyBank.getPlugin(), "ATMOwner"), PersistentDataType.STRING, player.getUniqueId().toString());
//    }

}
