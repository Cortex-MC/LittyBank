package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.database.Database;
import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountsListMenu extends Menu {

    public AccountsListMenu(PlayerMenuUtility playerMenuUtility) {
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
            MenuManager.openMenu(SavingsMainMenu.class, p);
        }else {

            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            if (container.has(new NamespacedKey(LittyBank.getPlugin(), "accountid"), PersistentDataType.INTEGER)){
                playerMenuUtility.setData(MenuData.ACCOUNT_ID, container.get(new NamespacedKey(LittyBank.getPlugin(), "accountid"), PersistentDataType.INTEGER));
                MenuManager.openMenu(AccountOptionsMenu.class, playerMenuUtility.getOwner());
            }
        }
    }

    @Override
    public void setMenuItems() {

        List<SavingsAccount> accounts = new ArrayList<>();
        try {
            accounts = Database.getSavingsDao().queryForEq("owner", p.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        accounts.forEach(account -> {
            ItemStack item = makeItem(account.getTier().getAsMaterial(),
                    ColorTranslator.translateColorCodes("&#e64764&lAccount &#d42847" + SavingsAccount.formatId(account.getId())),
                    ColorTranslator.translateColorCodes("&7Balance: &a" + BankNote.formatBalance(account.getBalance())));

            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(new NamespacedKey(LittyBank.getPlugin(), "accountid"), PersistentDataType.INTEGER, account.getId());
            item.setItemMeta(itemMeta);

            inventory.addItem(item);
        });


        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));
        inventory.setItem(8, back);

        setFillerGlass();
    }
}
