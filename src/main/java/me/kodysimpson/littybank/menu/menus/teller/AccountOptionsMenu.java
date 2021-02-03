package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.SelfCancelledMenu;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AccountOptionsMenu extends Menu implements SelfCancelledMenu {
    public AccountOptionsMenu(AbstractPlayerMenuUtility pmu) {
        super(pmu);
    }

    @Override
    public String getMenuName() {
        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);
        return playerMenuUtility.getAccountName();
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (e.getCurrentItem().getType() == Material.TOTEM_OF_UNDYING) {

            pmu.getOwner().closeInventory();
            deleteAccount(pmu.getOwner(), playerMenuUtility.getAccountName());

        }else if (e.getCurrentItem().getType() == Material.BARRIER) {

            MenuManager.openMenu(SavingsAccountsMenu.class, pmu.getOwner());

        }

    }

    @Override
    public void setMenuItems() {
        ItemStack withdraw = makeItem(Material.GREEN_CONCRETE, "Withdraw");
        ItemStack deposit = makeItem(Material.BLUE_CONCRETE, "Deposit");
        ItemStack delete = makeItem(Material.TOTEM_OF_UNDYING, "Delete Account");
        ItemStack back = makeItem(Material.BARRIER, "Back");

        inventory.setItem(0, withdraw);
        inventory.setItem(2, deposit);
        inventory.setItem(5, delete);
        inventory.setItem(8, back);
    }

    public void deleteAccount(Player player, String accountName) {
        int id = Integer.parseInt(accountName.substring(accountName.indexOf("#")+1));
        double balance = Database.getBalance(id);

        Economy economy = LittyBank.getEconomy();

        if (economy.getBalance(player) >= balance){

            EconomyResponse response = economy.depositPlayer(player, balance);

            if (response.transactionSuccess()){

                Database.deleteAccount(id);

                player.sendMessage("Your account has been successfully deleted and $" + balance + " has been added to your account.\n" +
                        "Will add a confirmation menu later");

            }else{

                player.sendMessage("Transaction Error. Try again later.");

            }

        }else{

            player.sendMessage("You cant afford it you poor bitch.");

        }
    }
}
