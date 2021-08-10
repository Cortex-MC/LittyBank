package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SavingsMainMenu extends Menu {

    public SavingsMainMenu(PlayerMenuUtility playerMenuUtility) {
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

        if (e.getCurrentItem().getType() == Material.GREEN_GLAZED_TERRACOTTA){

//            if (Database.getAccounts(playerMenuUtility.getOwner()).size() >= 3) {
//                playerMenuUtility.getOwner().closeInventory();
//                playerMenuUtility.getOwner().sendMessage(MessageUtils.message("You cannot have more than 3 open accounts."));
//                return;
//            }

            MenuManager.openMenu(SavingsTierSelectionMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.BLUE_GLAZED_TERRACOTTA) {

//            if (Database.getAccounts(playerMenuUtility.getOwner()).isEmpty()) {
//                playerMenuUtility.getOwner().closeInventory();
//                playerMenuUtility.getOwner().sendMessage(MessageUtils.message("You don't have any open savings accounts."));
//                return;
//            }

            MenuManager.openMenu(AccountsListMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            MenuManager.openMenu(TellerMenu.class, playerMenuUtility.getOwner());

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack createAccount = makeItem(Material.GREEN_GLAZED_TERRACOTTA, ColorTranslator.translateColorCodes("&#54d13f&lCreate Savings Account"),
                ColorTranslator.translateColorCodes("&7Savings account can be"), ColorTranslator.translateColorCodes("&7used to gain money passively"), ColorTranslator.translateColorCodes("&7over time."));
        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));
        ItemStack yourAccounts = makeItem(Material.BLUE_GLAZED_TERRACOTTA, ColorTranslator.translateColorCodes("&#3fa8d1&lYour Savings Accounts"),
                ColorTranslator.translateColorCodes("&7View and manage all"), ColorTranslator.translateColorCodes("&7of your accounts."));
        inventory.setItem(11, yourAccounts);
        inventory.setItem(15, createAccount);
        inventory.setItem(22, back);
        setFillerGlass();

    }
}
