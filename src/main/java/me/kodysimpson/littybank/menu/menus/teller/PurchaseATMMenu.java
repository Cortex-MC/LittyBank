package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PurchaseATMMenu extends Menu {

    public PurchaseATMMenu(AbstractPlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Purchase ATM?";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (e.getCurrentItem().getType() == Material.BARRIER){
            new TellerMenu(playerMenuUtility).open();
        }else if (e.getCurrentItem().getType() == Material.BELL){

            purchaseATM(playerMenuUtility.getOwner());
            playerMenuUtility.getOwner().closeInventory();

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));

        ItemStack info = makeItem(Material.ANVIL, ColorTranslator.translateColorCodes("&6&lATM"),
                ColorTranslator.translateColorCodes("&7An ATM is a portable machine"),
                ColorTranslator.translateColorCodes("&7you can withdraw money from."),
                "\n&7Cost: &a$10000");

        ItemStack purchase = makeItem(Material.BELL, ColorTranslator.translateColorCodes("&#31d428&lPurchase ATM"));

        inventory.setItem(2, back);
        inventory.setItem(5, info);
        inventory.setItem(7, purchase);

    }

    public void purchaseATM(Player player) {
        Economy economy = LittyBank.getEconomy();

        if (economy.getBalance(player) >= 50.0){

            EconomyResponse response = economy.withdrawPlayer(player, 50);

            if (response.transactionSuccess()){

                //chicka chicka chicka! slim shady! hotter than a set of twin babies!!!
                player.getInventory().addItem(ATM.createATM(player));
                player.sendMessage(MessageUtils.message("Purchased ATM; given."));

            }else{

                player.sendMessage(MessageUtils.message("Transaction Error. Try again later."));

            }

        }else{

            player.sendMessage(MessageUtils.message("You cant afford it you poor bitch."));
        }
    }
}
