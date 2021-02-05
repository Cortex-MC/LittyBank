package me.kodysimpson.littybank.models;

import me.kodysimpson.littybank.LittyBank;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BankNote {

    private PersistentDataContainer container;
    private double value;

    private static List<String> playersInConversation = new ArrayList<>();

    public BankNote() {
        this.value = 0;
    }

    public BankNote(double value) {
        this.value = value;
    }

    public BankNote(ItemStack item) {
        this.container = item.getItemMeta().getPersistentDataContainer();
        this.value = item.getAmount() * container.get(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.DOUBLE);
    }

    public double getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public static List<String> getPlayersInConversation() {
        return playersInConversation;
    }

    public static void setPlayersInConversation(List<String> playersInConversation) {
        BankNote.playersInConversation = playersInConversation;
    }

//    public static ItemStack createBankNote(float value, int amount) {
//        ItemStack note = new ItemStack(Material.PAPER, amount);
//        ItemMeta meta = note.getItemMeta();
//        meta.setDisplayName(String.format("$%f Bank Note", 1.0*Math.round(value*100)/100));
//
//        PersistentDataContainer container = note.getItemMeta().getPersistentDataContainer();
//
//        container.set(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.FLOAT, value);
//
//        note.setItemMeta(meta);
//
//        return note;
//    }

    public static ItemStack createBankNote(double value) {
        ItemStack note = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = note.getItemMeta();
        String valueString = String.valueOf(Math.round(value*100.0));
        valueString = valueString.substring(0, valueString.length()-2) + "." + valueString.substring(valueString.length()-2);
        meta.setDisplayName(String.format("$%s Bank Note", valueString));

        ArrayList<String> lore = new ArrayList<>();
        lore.add("Daddy got a brand new car!!!");

        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.DOUBLE, value);

        meta.setLore(lore);
        note.setItemMeta(meta);

        return note;
    }

    public static boolean isBankNote(ItemStack item) {

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        return container.has(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.DOUBLE);

    }

    public static void addPlayerInConversation(Player player) {
        String uuid = player.getUniqueId().toString();
        playersInConversation.add(uuid);
    }

    public static void removePlayerInConversation(Player player) {
        String uuid = player.getUniqueId().toString();
        playersInConversation.remove(uuid);
    }

    public static boolean isPlayerInConversation(Player player) {
        String uuid = player.getUniqueId().toString();
        return playersInConversation.contains(uuid);
    }

}
