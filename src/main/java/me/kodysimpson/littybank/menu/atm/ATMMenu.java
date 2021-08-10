package me.kodysimpson.littybank.menu.atm;

import me.kodysimpson.littybank.database.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.menu.menus.teller.AccountsListMenu;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.md_5.bungee.api.ChatMessageType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ATMMenu extends Menu {

    private final ATM atm;

    public ATMMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        atm = playerMenuUtility.getData(MenuData.ATM, ATM.class);
    }

    @Override
    public String getMenuName() {
        return "Automated Teller Machine";
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        if (e.getCurrentItem().getType() == Material.BARRIER){

            p.closeInventory();

        }else if (e.getCurrentItem().getType() == Material.BIRCH_SIGN){

            p.closeInventory();
            displayCheckingAccountBalance(playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.PAPER){

//            if (Database.getAccounts(playerMenuUtility.getOwner()).isEmpty()) {
//                playerMenuUtility.getOwner().closeInventory();
//                playerMenuUtility.getOwner().sendMessage(MessageUtils.message("You don't have any open savings accounts."));
//                return;
//            }

            MenuManager.openMenu(AccountsListMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.QUARTZ_BLOCK) {

            pickUpATM(playerMenuUtility.getOwner(), atm.getLocation());
            playerMenuUtility.getOwner().closeInventory();
        }

    }

    @Override
    public void setMenuItems() {

        ItemStack pickup = makeItem(Material.QUARTZ_BLOCK, ColorTranslator.translateColorCodes("&4&lPick up"));
        ItemStack close = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lClose"));
        ItemStack balance = makeItem(Material.BIRCH_SIGN, ColorTranslator.translateColorCodes("&a&lCheck Balance"));
        ItemStack withdraw = makeItem(Material.PAPER, ColorTranslator.translateColorCodes("&#bd883e&lManage Savings Accounts"));

        inventory.setItem(11, pickup);
        inventory.setItem(13, balance);
        inventory.setItem(15, withdraw);
        inventory.setItem(31, close);

        setFillerGlass();
    }

    public void displayCheckingAccountBalance(Player player) {

        Economy economy = LittyBank.getEconomy();
        double balance = economy.getBalance(player);

        player.sendMessage("   ");
        player.sendMessage(ColorTranslator.translateColorCodes("&c&lChecking Account Balance&7: &a$" + balance));
        player.sendMessage("   ");

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ColorTranslator.translateColorCodesToTextComponent("&eBalance&7: &a$" + balance));

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
