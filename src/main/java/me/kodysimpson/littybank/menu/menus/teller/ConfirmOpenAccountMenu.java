package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.models.AccountTier;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmOpenAccountMenu extends Menu {

    public ConfirmOpenAccountMenu(AbstractPlayerMenuUtility playerMenuUtility) {
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

            openAccount(AccountTier.matchTier(e.getInventory().getItem(1).getType()), pmu.getOwner());
            pmu.getOwner().closeInventory();

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            //go back
            MenuManager.openMenu(SavingsTierSelectionMenu.class, pmu.getOwner());

        }

    }

    @Override
    public void setMenuItems() {
        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        ItemStack yes = makeItem(Material.BELL, "Yes");
        ItemStack no = makeItem(Material.BARRIER, "No");
        AccountTier tier = playerMenuUtility.getTier();

        ItemStack tierItem = makeItem(tier.getAsMaterial(), tier.getAsString() + " Tier");

        inventory.setItem(1, tierItem);
        inventory.setItem(3, no);
        inventory.setItem(5, yes);

        setFillerGlass();
    }

    public void openAccount(AccountTier tier, Player player) {

        double fee = tier.getOpeningFee();
        Economy economy = LittyBank.getEconomy();

        if (economy.getBalance(player) >= fee){

            EconomyResponse response = economy.withdrawPlayer(player, fee);

            if (response.transactionSuccess()){

                SavingsAccount account = new SavingsAccount();
                account.setAccountOwner(player.getUniqueId());
                account.setTier(tier);
                account.setBalance(0);

                Database.createAccount(account);

                player.sendMessage("You have succesfully opened a Savings Account.");

            }else{

                player.sendMessage("Transaction Error. Try again later.");

            }

        }else{

            player.sendMessage("You cant afford it you poor bitch.");

        }
    }
}
