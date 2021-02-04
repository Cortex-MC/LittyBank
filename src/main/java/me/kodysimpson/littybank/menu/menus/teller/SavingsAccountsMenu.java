package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SavingsAccountsMenu extends Menu {
    public SavingsAccountsMenu(AbstractPlayerMenuUtility pmu) {
        super(pmu);
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

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (e.getCurrentItem().getType() == Material.BARRIER) {

            MenuManager.openMenu(SavingsMainMenu.class, pmu.getOwner());

        }else if (SavingsAccount.isValidAccount(e.getCurrentItem())) {

            playerMenuUtility.setAccountName(e.getCurrentItem().getItemMeta().getDisplayName());
            MenuManager.openMenu(AccountOptionsMenu.class, pmu.getOwner());
        }
    }

    @Override
    public void setMenuItems() {
        List<SavingsAccount> accounts = Database.getAccounts(pmu.getOwner());

        accounts.forEach(account -> {
            ItemStack item = makeItem(account.getTier().getAsMaterial(),
                    "Account Number: " + SavingsAccount.formatId(account.getId()),
                    "Balance: " + account.getBalance());
            inventory.addItem(item);
        });


        ItemStack back = makeItem(Material.BARRIER, "Back");
        inventory.setItem(8, back);
    }
}
