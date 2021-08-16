package me.kodysimpson.littybank.menu.menus.teller.savings;

import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.models.AccountTier;
import me.kodysimpson.littybank.utils.AccountUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmUpgradeMenu extends Menu {

    private final AccountTier upgradeTier;

    public ConfirmUpgradeMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        upgradeTier = playerMenuUtility.getData(MenuData.UPGRADE_TIER, AccountTier.class);
    }

    @Override
    public String getMenuName() {
        return "Confirm: Upgrade Account Tier";
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
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws MenuManagerNotSetupException, MenuManagerException {



    }

    @Override
    public void setMenuItems() {
        ItemStack yes = makeItem(Material.BELL, ColorTranslator.translateColorCodes("&#54d13f&lYes"),
                "&7Upgrade your Savings Account",
                "&7tier to " + AccountUtils.getAccountTierName(upgradeTier));
        ItemStack no = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lNo"), "&7Go back");

        inventory.setItem(0, no);
        inventory.setItem(8, yes);

        setFillerGlass();
    }
}
