package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TellerMenu extends Menu {

    public TellerMenu(AbstractPlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "How can I help you today?";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (e.getCurrentItem().getType() == Material.PAPER){

            MenuManager.openMenu(SavingsMainMenu.class, pmu);

        }else if (e.getCurrentItem().getType() == Material.ANVIL){

            MenuManager.openMenu(PurchaseATMMenu.class, pmu);

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            playerMenuUtility.getOwner().closeInventory();

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack savings = makeItem(Material.PAPER, "Savings Accounts");

        ItemStack bankNotes = makeItem(Material.WRITTEN_BOOK, "Bank Notes");

        ItemStack ATM = makeItem(Material.ANVIL, "Purchase ATM");

        inventory.setItem(11, savings);
        inventory.setItem(13, bankNotes);
        inventory.setItem(15, ATM);

        setFillerGlass();

    }
}
