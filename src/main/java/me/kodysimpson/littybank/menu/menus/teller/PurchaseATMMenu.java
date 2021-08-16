package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PurchaseATMMenu extends Menu {

    private double atmCost = 0;

    public PurchaseATMMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        atmCost = LittyBank.getPlugin().getConfig().getDouble("atm-cost");
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
    public void handleMenu(InventoryClickEvent e) throws MenuManagerException, MenuManagerNotSetupException {

        if (e.getCurrentItem().getType() == Material.BARRIER){
            MenuManager.openMenu(TellerMenu.class, playerMenuUtility.getOwner());
        }else if (e.getCurrentItem().getType() == Material.BELL){

            purchaseATM(playerMenuUtility.getOwner());
            playerMenuUtility.getOwner().closeInventory();

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));

        ItemStack info = makeItem(Material.ANVIL, ColorTranslator.translateColorCodes("&6&lATM"),
                "&7Get your very own portable",
                "&7ATM to allow users to withdraw",
                "&7money from their checking accounts",
                "&7, for a small fee that you collect.", " ",
                "&7Cost: &a" + atmCost);

        ItemStack purchase = makeItem(Material.BELL, ColorTranslator.translateColorCodes("&#31d428&lPurchase ATM"), ColorTranslator.translateColorCodes("&7Cost: &a$" + atmCost));

        inventory.setItem(0, back);
        inventory.setItem(4, info);
        inventory.setItem(8, purchase);

        setFillerGlass();
    }

    public void purchaseATM(Player player) {
        Economy economy = LittyBank.getEconomy();

        if (economy.getBalance(player) >= atmCost){

            EconomyResponse response = economy.withdrawPlayer(player, atmCost);

            if (response.transactionSuccess()){

                //chicka chicka chicka! slim shady! hotter than a set of twin babies!!!
                player.getInventory().addItem(ATM.createATM(player));
                player.sendMessage(MessageUtils.message("The ATM has been purchased and placed in your inventory."));

            }else{
                player.sendMessage(MessageUtils.message("Transaction Error. Try again later."));
            }

        }else{
            player.sendMessage(MessageUtils.message("You cant afford it you poor bitch."));
        }
    }
}
