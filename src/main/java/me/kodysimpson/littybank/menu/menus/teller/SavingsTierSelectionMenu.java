package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.models.AccountTier;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SavingsTierSelectionMenu extends Menu {

    public SavingsTierSelectionMenu(AbstractPlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Select an Account Tier";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (AccountTier.isValidTier(e.getCurrentItem())){

            playerMenuUtility.setTier(AccountTier.matchTier(e.getCurrentItem().getType()));
            MenuManager.openMenu(ConfirmOpenAccountMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            MenuManager.openMenu(SavingsMainMenu.class, playerMenuUtility.getOwner());

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack silver = makeItem(AccountTier.SILVER.getAsMaterial(), AccountTier.SILVER.getAsString() + " Tier");
        ItemStack gold = makeItem(AccountTier.GOLD.getAsMaterial(), AccountTier.GOLD.getAsString() + " Tier");
        ItemStack plat = makeItem(AccountTier.PLATINUM.getAsMaterial(), AccountTier.PLATINUM.getAsString() + " Tier");

        ItemStack back = makeItem(Material.BARRIER, "Back");

        inventory.setItem(11, silver);
        inventory.setItem(13, gold);
        inventory.setItem(15, plat);
        inventory.setItem(31, back);
    }
}
