package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.utils.ColorUtils;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
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
    public void handleMenu(InventoryClickEvent e) {

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (e.getCurrentItem().getType() == Material.BARRIER){
            new TellerMenu(playerMenuUtility).open();
        }else if (e.getCurrentItem().getType() == Material.BELL){

            Economy economy = LittyBank.getEconomy();

            if (economy.getBalance(playerMenuUtility.getOwner()) >= 50.0){

                EconomyResponse response = economy.withdrawPlayer(playerMenuUtility.getOwner(), 50);

                if (response.transactionSuccess()){

                    //chicka chicka chicka! slim shady! hotter than a set of twin babies!!!
                    ItemStack ATM = makeItem(Material.ANVIL, "ATM");

                    playerMenuUtility.getOwner().getInventory().addItem(ATM);
                    playerMenuUtility.getOwner().closeInventory();
                    playerMenuUtility.getOwner().sendMessage("Purchased ATM; given.");

                }else{

                    playerMenuUtility.getOwner().closeInventory();
                    playerMenuUtility.getOwner().sendMessage("Transaction Error. Try again later.");

                }

            }else{
                playerMenuUtility.getOwner().closeInventory();
                playerMenuUtility.getOwner().sendMessage("You cant afford it you poor bitch.");
            }

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack back = makeItem(Material.BARRIER, "Back");

        ItemStack info = makeItem(Material.ANVIL, ColorUtils.translateColorCodes("&6&lATM"),
                "An ATM is a portable machine you can use to withdraw money from.",
                "Cost: $100000000000000");

        ItemStack purchase = makeItem(Material.BELL, "Purchase ATM");

        inventory.setItem(2, back);
        inventory.setItem(5, info);
        inventory.setItem(7, purchase);

    }
}
