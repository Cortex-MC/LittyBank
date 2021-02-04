package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SavingsMainMenu extends Menu {

    public SavingsMainMenu(AbstractPlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Savings Department";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (e.getCurrentItem().getType() == Material.GREEN_GLAZED_TERRACOTTA){

            if (Database.getAccounts(pmu.getOwner()).size() >= 3) {
                pmu.getOwner().closeInventory();
                pmu.getOwner().sendMessage("You cannot have more than 3 open accounts.");
                return;
            }

            MenuManager.openMenu(SavingsTierSelectionMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.BLUE_GLAZED_TERRACOTTA) {

            if (Database.getAccounts(pmu.getOwner()).isEmpty()) {
                pmu.getOwner().closeInventory();
                pmu.getOwner().sendMessage("You don't have any open savings accounts.");
                return;
            }

            MenuManager.openMenu(SavingsAccountsMenu.class, pmu.getOwner());

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            MenuManager.openMenu(TellerMenu.class, playerMenuUtility.getOwner());

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack createAccount = makeItem(Material.GREEN_GLAZED_TERRACOTTA, "Create Savings Account");
        ItemStack back = makeItem(Material.BARRIER, "Back");
        ItemStack yourAccounts = makeItem(Material.BLUE_GLAZED_TERRACOTTA, "Your Savings Accounts");
        inventory.setItem(11, yourAccounts);
        inventory.setItem(15, createAccount);
        inventory.setItem(26, back);
        setFillerGlass();

    }
}
