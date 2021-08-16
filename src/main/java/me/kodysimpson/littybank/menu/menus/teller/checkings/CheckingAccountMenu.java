package me.kodysimpson.littybank.menu.menus.teller.checkings;

import me.kodysimpson.littybank.database.AccountQueries;
import me.kodysimpson.littybank.menu.menus.teller.TellerMenu;
import me.kodysimpson.littybank.models.CheckingAccount;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CheckingAccountMenu extends Menu {

    private final CheckingAccount checkingAccount;

    public CheckingAccountMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        checkingAccount = AccountQueries.getAccountForPlayer(p);
    }

    @Override
    public String getMenuName() {
        return "Your Checking Account";
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

        switch (inventoryClickEvent.getCurrentItem().getType()) {
            case TRIPWIRE_HOOK -> MenuManager.openMenu(WireSelectionMenu.class, p);
            case BARRIER -> MenuManager.openMenu(TellerMenu.class, p);
        }

    }

    @Override
    public void setMenuItems() {
        ItemStack withdraw = makeItem(Material.GREEN_CONCRETE, ColorTranslator.translateColorCodes("&#23eb17&lWithdraw"),
                "&7Withdraw money from this", "&7account to your balance.");
        ItemStack deposit = makeItem(Material.BLUE_CONCRETE, ColorTranslator.translateColorCodes("&#6e54e3&lDeposit"),
                "&7Deposit money from your", "&7balance to this account.");
        ItemStack info = makeItem(Material.WRITABLE_BOOK, ColorTranslator.translateColorCodes("&e&lInformation"),
                " ", "&#1692fa&lBalance&7: &a$" + this.checkingAccount.getBalance());
        ItemStack wire = makeItem(Material.TRIPWIRE_HOOK, ColorTranslator.translateColorCodes("&#843897&lWire"),
                "&7Wire funds to another account.");
        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));

        inventory.setItem(0, withdraw);
        inventory.setItem(1, deposit);
        inventory.setItem(4, info);
        inventory.setItem(6, wire);
        inventory.setItem(8, back);

        setFillerGlass();
    }
}
