package me.kodysimpson.littybank.models;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankNote {

    private long value;

    public BankNote() {
        this.value = 0;
    }

    public BankNote(long value) {
        this.value = value;
    }

    public BankNote(ItemStack item) {
        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        this.value = compound.getLong("BankNoteValue");
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public ItemStack createBankNote(int amount) {
        ItemStack note = new ItemStack(Material.PAPER, amount);
        ItemMeta meta = note.getItemMeta();
        meta.setDisplayName(String.format("$%d Bank Note", value));
        note.setItemMeta(meta);

        net.minecraft.server.v1_16_R3.ItemStack nmsNote = CraftItemStack.asNMSCopy(note);
        NBTTagCompound noteCompound = nmsNote.hasTag() ? nmsNote.getTag() : new NBTTagCompound();

        noteCompound.setLong("BankNoteValue", value);
        nmsNote.setTag(noteCompound);

        return CraftItemStack.asBukkitCopy(nmsNote);
    }

    public static boolean isBankNote(ItemStack item) {
        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (compound == null) return false;

        return compound.hasKey("BankNoteValue");
    }

}
