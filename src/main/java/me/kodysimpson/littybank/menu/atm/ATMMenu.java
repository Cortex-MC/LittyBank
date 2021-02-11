package me.kodysimpson.littybank.menu.atm;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.menu.menus.teller.SavingsAccountsMenu;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ATMMenu extends Menu {

    public ATMMenu(AbstractPlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Automated Teller Machine";
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

        PlayerMenuUtility playerMenuUtility = (PlayerMenuUtility) pmu;

        if (e.getCurrentItem().getType() == Material.BARRIER){

            playerMenuUtility.getOwner().closeInventory();

        }else if (e.getCurrentItem().getType() == Material.BIRCH_SIGN){

            playerMenuUtility.getOwner().closeInventory();
            displayBalance(playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.PAPER){

            if (Database.getAccounts(pmu.getOwner()).isEmpty()) {
                pmu.getOwner().closeInventory();
                pmu.getOwner().sendMessage(MessageUtils.message("You don't have any open savings accounts."));
                return;
            }

            playerMenuUtility.setLastMenu(this);
            MenuManager.openMenu(SavingsAccountsMenu.class, pmu.getOwner());

        }else if (e.getCurrentItem().getType() == Material.QUARTZ_BLOCK) {

            pickUpATM(playerMenuUtility.getOwner(), playerMenuUtility.getAtmLocation());
            playerMenuUtility.getOwner().closeInventory();
        }

    }

    @Override
    public void setMenuItems() {

        ItemStack pickup = makeItem(Material.QUARTZ_BLOCK, ColorTranslator.translateColorCodes("&4&lPick up"));

        ItemStack close = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lClose"));

        ItemStack balance = makeItem(Material.BIRCH_SIGN, ColorTranslator.translateColorCodes("&a&lCheck Balance"));

        ItemStack withdraw = makeItem(Material.PAPER, ColorTranslator.translateColorCodes("&#bd883e&lManage Savings Accounts"));

        inventory.setItem(0, pickup);
        inventory.setItem(2, close);
        inventory.setItem(4, balance);
        inventory.setItem(6, withdraw);

    }

    public void displayBalance(Player player) {
        PlayerMenuUtility playerMenuUtility = (PlayerMenuUtility) pmu;

        Economy economy = LittyBank.getEconomy();
        double balance = economy.getBalance(player);

        ArmorStand balanceH = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        balanceH.setCollidable(false);
        balanceH.setCustomNameVisible(true);
        balanceH.setCustomName("Checking Balance: $" + balance);
        balanceH.setVisible(false);
        balanceH.setInvulnerable(true);
        balanceH.teleport(playerMenuUtility.getAtmLocation().add(0.5, -0.25, 0.5));
    }

    public void pickUpATM(Player currentPlayer, Location atmLocation) {

        ATM atm = Database.getATM(atmLocation);

        if (!currentPlayer.equals(atm.getOwner())) {
            currentPlayer.sendMessage(MessageUtils.message("You cannot pickup other players' ATMs."));
            return;
        }

        ItemStack atmItem = ATM.createATM(currentPlayer);

        if (currentPlayer.getInventory().addItem(atmItem).size() > 0) {
            currentPlayer.sendMessage(MessageUtils.message("No space in inventory."));
        }else{
            currentPlayer.sendMessage(MessageUtils.message("Successfully picked up ATM. (Maybe add some particles when this happens)"));
            Database.deleteATM(atm);
            currentPlayer.getWorld().getBlockAt(atm.getLocation()).setType(Material.AIR);
        }
    }
}
