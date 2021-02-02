package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmOpenAccountMenu extends Menu {

    public ConfirmOpenAccountMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Confirm: Open Savings Account?";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        if (e.getCurrentItem().getType() == Material.BELL){

            //create account

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            //go back
            MenuManager.openMenu(SavingsTierSelectionMenu.class, pmu.getOwner());

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack yes = makeItem(Material.BELL, "Yes");
        ItemStack no = makeItem(Material.BARRIER, "No");

        inventory.setItem(3, no);
        inventory.setItem(5, yes);

    }
}
