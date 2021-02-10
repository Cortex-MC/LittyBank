package me.kodysimpson.littybank.models;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ATM {

    private static List<Location> atmLocations = new ArrayList<>();

    public static List<Location> getAtmLocations() {
        return atmLocations;
    }

    public static void setAtmLocations(List<Location> atmLocations) {
        ATM.atmLocations = atmLocations;
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



    public static Player getOwner(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String uuid = container.get(new NamespacedKey(LittyBank.getPlugin(), "ATMOwner"), PersistentDataType.STRING);
        return Bukkit.getPlayer(uuid);
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
