package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.database.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.models.AccountTier;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Date;

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
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {


        if (e.getCurrentItem().getType() == Material.BELL){

            try {
                openAccount(AccountTier.matchTier(e.getInventory().getItem(0).getType()), playerMenuUtility.getOwner());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            playerMenuUtility.getOwner().closeInventory();

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            //go back
            MenuManager.openMenu(SavingsTierSelectionMenu.class, playerMenuUtility.getOwner());

        }

    }

    public void openAccount(AccountTier tier, Player player) throws SQLException {

        player.sendMessage(tier.getAsString());
        double fee = tier.getOpeningFee();
        Economy economy = LittyBank.getEconomy();

        if (economy.getBalance(player) >= fee){

            EconomyResponse response = economy.withdrawPlayer(player, fee);

            if (response.transactionSuccess()){

                SavingsAccount account = new SavingsAccount();
                account.setOwner(player.getUniqueId());
                account.setTier(tier);
                account.setBalance(0);
                account.setLastChecked(new Date());
                account.setLastUpdated(new Date());

                Database.getSavingsAccountDao().create(account);

                player.sendMessage(MessageUtils.message("You have successfully opened a Savings Account."));

            }else{

                player.sendMessage(MessageUtils.message("Transaction Error. Try again later."));

            }

        }else{

            player.sendMessage(MessageUtils.message("You cant afford it you poor bitch."));

        }
    }

    @Override
    public void setMenuItems() {

        ItemStack yes = makeItem(Material.BELL, "Yes");
        ItemStack no = makeItem(Material.BARRIER, "No");
        ItemStack tierItem = playerMenuUtility.getData(MenuData.TIER_ITEM, ItemStack.class);


        inventory.setItem(0, tierItem);
        inventory.setItem(3, no);
        inventory.setItem(5, yes);

        setFillerGlass();
    }
}
