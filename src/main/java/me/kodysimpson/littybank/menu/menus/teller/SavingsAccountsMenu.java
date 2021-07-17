package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.menu.Data;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SavingsAccountsMenu extends Menu {

    public SavingsAccountsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Your Savings Accounts";
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
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        if (e.getCurrentItem().getType() == Material.BARRIER) {

            back();

        }else if (SavingsAccount.isValidAccount(e.getCurrentItem())) {

            playerMenuUtility.setData(Data.ACCOUNT_NAME, e.getCurrentItem().getItemMeta().getDisplayName());
            MenuManager.openMenu(AccountOptionsMenu.class, playerMenuUtility.getOwner());
        }
    }

    @Override
    public void setMenuItems() {
        List<SavingsAccount> accounts = Database.getAccounts(playerMenuUtility.getOwner());

        accounts.forEach(account -> {
            ItemStack item = makeItem(account.getTier().getAsMaterial(),
                    ColorTranslator.translateColorCodes("&#e64764&lAccount &#d42847" + SavingsAccount.formatId(account.getId())),
                    ColorTranslator.translateColorCodes("&7Balance: &a" + BankNote.formatBalance(account.getBalance())));
            inventory.addItem(item);
        });


        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));
        inventory.setItem(8, back);
    }
}
