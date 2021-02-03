package me.kodysimpson.littybank.listeners;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.menus.teller.TellerMenu;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;


public class BankTellerListener implements Listener {

    @EventHandler
    public void onEntityInteract(NPCRightClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        ItemStack itemInHand = e.getClicker().getInventory().getItemInMainHand();

        if (itemInHand.hasItemMeta() && BankNote.isBankNote(itemInHand)) {
            redeemBankNote(e.getClicker(), new BankNote(itemInHand).getValue(), itemInHand);
        }else{
            new TellerMenu(MenuManager.getPlayerMenuUtility(e.getClicker())).open();
        }

    }

    public void redeemBankNote(Player player, float value, ItemStack item) {

        Economy economy = LittyBank.getEconomy();
        EconomyResponse response = economy.depositPlayer(player, value);

        if (response.transactionSuccess()){

            ItemStack newItem = item.getAmount() > 1 ? new BankNote(value).createBankNote(item.getAmount()-1) : new ItemStack(Material.AIR);
            player.sendMessage("You have successfully redeemed $" + value);
            player.getInventory().setItemInMainHand(newItem);

        }else{

            player.sendMessage("Transaction Error. Try again later.");

        }

    }

}
