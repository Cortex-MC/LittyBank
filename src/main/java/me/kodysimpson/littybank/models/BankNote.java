package me.kodysimpson.littybank.models;

import me.kodysimpson.littybank.LittyBank;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BankNote {

    private PersistentDataContainer container;
    private float value;

    public BankNote() {
        this.value = 0;
    }

    public BankNote(float value) {
        this.value = value;
    }

    public BankNote(ItemStack item) {
        this.container = item.getItemMeta().getPersistentDataContainer();
        this.value = container.get(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.LONG);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public static ItemStack createBankNote(long value, int amount) {
        ItemStack note = new ItemStack(Material.PAPER, amount);
        ItemMeta meta = note.getItemMeta();
        meta.setDisplayName(String.format("$%d Bank Note", value));

        PersistentDataContainer container = note.getItemMeta().getPersistentDataContainer();

        container.set(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.LONG, value);

        note.setItemMeta(meta);

        return note;
    }

    public static ItemStack createBankNote(long value) {
        ItemStack note = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = note.getItemMeta();
        meta.setDisplayName(String.format("$%d Bank Note", value));

        PersistentDataContainer container = note.getItemMeta().getPersistentDataContainer();

        container.set(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.LONG, value);

        note.setItemMeta(meta);

        return note;
    }

    public static boolean isBankNote(ItemStack item) {

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        return container.has(new NamespacedKey(LittyBank.getPlugin(), "BankNoteValue"), PersistentDataType.LONG);

    }

}
