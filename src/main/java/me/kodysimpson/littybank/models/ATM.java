package me.kodysimpson.littybank.models;

import me.kodysimpson.littybank.LittyBank;
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

public class ATM {

    private Player owner;
    private Location location;

    public ATM(Player owner, Location location) {
        this.owner = owner;
        this.location = location;
    }

    public ATM(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String uuid = container.get(new NamespacedKey(LittyBank.getPlugin(), "ATMOwner"), PersistentDataType.STRING);
        this.owner = Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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



//    public static boolean isValidATM(Block block) {
//
//        if (!(block.getState() instanceof TileState)) return false;
//
//        TileState state = (TileState) block.getState();
//
//        PersistentDataContainer container = state.getPersistentDataContainer();
//
//        return container.has(new NamespacedKey(LittyBank.getPlugin(), "ATMOwner"), PersistentDataType.STRING);
//    }
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
