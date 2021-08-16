package me.kodysimpson.littybank.menu.menus.teller.savings;

import me.kodysimpson.littybank.database.AccountQueries;
import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.models.AccountTier;
import me.kodysimpson.littybank.utils.AccountUtils;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

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

        if (inventoryClickEvent.getCurrentItem().getType() == Material.BELL){
            p.closeInventory();
            try {
                if (AccountQueries.upgradeSavingsAccount(p, upgradeTier)){
                    MessageUtils.message(p, "&aYour savings account has been upgraded to " + AccountUtils.getAccountTierName(upgradeTier) + "!");
                    MessageUtils.message(p, "&7You were charged " + AccountUtils.getTierCost(upgradeTier) + ".");
                }else{
                    MessageUtils.message(p, "&4There was a problem upgrading the account, try again later.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                MessageUtils.message(p, "&4There was a problem upgrading the account, try again later.");
            }
        }

    }

    @Override
    public void setMenuItems() {
        ItemStack yes = makeItem(Material.BELL, ColorTranslator.translateColorCodes("&#54d13f&lYes"),
                "&7Upgrade your Savings Account",
                "&7tier to " + AccountUtils.getAccountTierName(upgradeTier));
        ItemStack info = makeItem(Material.CHEST, ColorTranslator.translateColorCodes("&6&lInfo"),
                "&7Current Tier: " + AccountUtils.getAccountTierName(AccountUtils.getPreviousTier(upgradeTier)),
                "&7Next Tier: " + AccountUtils.getAccountTierName(upgradeTier),
                "&7Cost: &a" + AccountUtils.getTierCost(upgradeTier),
                " ", "&7Upgrading to the next tier",
                "&7will provide a &#1692fa" + AccountUtils.getAccountInterest(upgradeTier) + "%",
                "&7interest rate.");
        ItemStack no = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lNo"), "&7Go back");

        inventory.setItem(0, no);
        inventory.setItem(4, info);
        inventory.setItem(8, yes);

        setFillerGlass();
    }
}
