package me.kodysimpson.littybank.menu.atm;

import me.kodysimpson.littybank.database.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.md_5.bungee.api.ChatMessageType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class ATMMenu extends Menu {

    private final ATM atm;
    private Economy economy;

    public ATMMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        atm = playerMenuUtility.getData(MenuData.ATM, ATM.class);
        economy = LittyBank.getEconomy();
    }

    @Override
    public String getMenuName() {
        if (atm.getOwner().equals(p.getUniqueId())){
            return "Your ATM : $" + atm.getBalance();
        }
        return "Automated Teller Machine";
    }

    @Override
    public int getSlots() {
        if (atm.getOwner().equals(p.getUniqueId())){
            return 54;
        }
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

            //Make sure the ATM has money
            double atmBalance = atm.getBalance();
            if (atmBalance > 0){
                withdrawalConversation(p, atmBalance);
            }else{
                p.sendMessage("This ATM has no money! Contact " + Bukkit.getPlayer(atm.getOwner()).getDisplayName() + " and give them a heads up.");
            }


//            if (Database.getAccounts(playerMenuUtility.getOwner()).isEmpty()) {
//                playerMenuUtility.getOwner().closeInventory();
//                playerMenuUtility.getOwner().sendMessage(MessageUtils.message("You don't have any open savings accounts."));
//                return;
//            }

            //MenuManager.openMenu(AccountsListMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.ANVIL) {
            pickUpATM(playerMenuUtility.getOwner());
            playerMenuUtility.getOwner().closeInventory();
        }else if (e.getCurrentItem().getType() == Material.BUCKET){

            //See if its a right click or left click
            if (e.getClick() == ClickType.LEFT){
                p.closeInventory();
                depositConversation((Player) e.getWhoClicked());
            }else if(e.getClick() == ClickType.RIGHT){
                //Get the earnings from the ATM and give it to the player
                double atmBalance = atm.getBalance();
                if (atmBalance > 0){
                    if (economy.depositPlayer(p, atmBalance).transactionSuccess()){
                        p.sendMessage(atmBalance + " has been removed from the ATM and deposited to your checking account.");
                    }else{
                        p.sendMessage("Transaction failure.");
                    }
                }else{
                    p.sendMessage("This ATM does not have any money in it.");
                }
            }

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack close = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lClose"));
        ItemStack balance = makeItem(Material.BIRCH_SIGN, ColorTranslator.translateColorCodes("&a&lCheck Balance"));
        ItemStack viewSavings = makeItem(Material.QUARTZ_BLOCK, ColorTranslator.translateColorCodes("&#bd883e&lManage Savings Accounts"));

        inventory.setItem(13, balance);
        inventory.setItem(15, viewSavings);

        if (p.getUniqueId().equals(atm.getOwner())){
            ItemStack pickup = makeItem(Material.ANVIL, ColorTranslator.translateColorCodes("&4&lPick up"));
            inventory.setItem(11, pickup);

            ItemStack atmBalance = makeItem(Material.BUCKET, ColorTranslator.translateColorCodes("&e&lATM Balance"),
                    "&7:: &a$" + atm.getBalance(),
                    "&#1692faSupply the ATM with money ",
                    "&#1692faso withdrawals can be made&7. ",
                    "&#1692faEach withdrawal earns you a ",
                    "&#1692fapercentage of the amount ",
                    "&#1692fataken&7.",
                    " ", "&cEarnings&7: &a$" + atm.getEarnings(),
                    " ", "&e* &7Left Click to Deposit", "&e* &7Right Click to Withdraw");
            inventory.setItem(31, atmBalance);
            inventory.setItem(49, close);
        }else{
            ItemStack pickup = makeItem(Material.PAPER, ColorTranslator.translateColorCodes("&#1692fa&lWithdraw"));
            inventory.setItem(11, pickup);
            inventory.setItem(31, close);
        }

        setFillerGlass();
    }

    private void withdrawalConversation(Player player, double atmBalance) {

        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try {
                    Double.parseDouble(s);
                    return true;
                } catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")){
                        return true;
                    }
                    player.sendMessage(MessageUtils.message("Please enter a valid amount."));
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    player.sendMessage(MessageUtils.message("Transaction cancelled."));
                }else{
                    //do the deposit
                    try {

                        double amount = Double.parseDouble(s);
                        Economy economy = LittyBank.getEconomy();

                        //Make sure both the ATM and the player's checking account can afford this
                        if (economy.getBalance(player) >= amount && atmBalance >= amount){

                            if (economy.withdrawPlayer(player, amount).transactionSuccess()){
                                atm.setBalance(atm.getBalance() + amount);
                                Database.getAtmDao().update(atm);
                                player.sendMessage(amount + " has been put into the ATM. Any withdrawals will give you earnings.");
                            }else{
                                player.sendMessage(MessageUtils.message("The transaction could not be completed. Try again later."));
                            }

                        }else{
                            player.sendMessage("You cannot afford this.");
                        }

                    } catch (SQLException | NumberFormatException e) {
                        e.printStackTrace();
                        player.sendMessage(MessageUtils.message("The transaction could not be completed. Try again later."));
                    }
                }
                BankNote.removePlayerInConversation(player);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the amount to withdraw from the ATM. Your limit is " + atmBalance + " \n(Type \"cancel\" to cancel)");
            }
        };

        BankNote.addPlayerInConversation(player);
        new ConversationFactory(LittyBank.getPlugin())
                .withModality(false)
                .withTimeout(15)
                .withFirstPrompt(enterAmount)
                .buildConversation(player)
                .begin();

    }

    public void depositConversation(Player player) {

        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try {
                    Double.parseDouble(s);
                    return true;
                } catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")){
                        return true;
                    }
                    player.sendMessage(MessageUtils.message("Please enter a valid amount"));
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    player.sendMessage(MessageUtils.message("Transaction has been cancelled"));
                }else{
                    //do the deposit
                    try {

                        double amount = Double.parseDouble(s);
                        Economy economy = LittyBank.getEconomy();

                        if (economy.getBalance(player) >= amount){

                            if (economy.withdrawPlayer(player, amount).transactionSuccess()){
                                atm.setBalance(atm.getBalance() + amount);
                                Database.getAtmDao().update(atm);
                                player.sendMessage(amount + " has been put into the ATM. Any withdrawals will give you earnings.");
                            }else{
                                player.sendMessage(MessageUtils.message("The transaction could not be completed. Try again later."));
                            }

                        }else{
                            player.sendMessage("You cannot afford this.");
                        }

                    } catch (SQLException | NumberFormatException e) {
                        e.printStackTrace();
                        player.sendMessage(MessageUtils.message("The transaction could not be completed. Try again later."));
                    }
                }
                BankNote.removePlayerInConversation(player);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the amount to deposit (Type \"cancel\" to cancel)");
            }
        };

        BankNote.addPlayerInConversation(player);
        new ConversationFactory(LittyBank.getPlugin())
                .withModality(false)
                .withTimeout(15)
                .withFirstPrompt(enterAmount)
                .buildConversation(player)
                .begin();

    }

    public void displayCheckingAccountBalance(Player player) {

        Economy economy = LittyBank.getEconomy();
        double balance = economy.getBalance(player);

        player.sendMessage("   ");
        player.sendMessage(ColorTranslator.translateColorCodes("&c&lChecking Account Balance&7: &a$" + balance));
        player.sendMessage("   ");

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ColorTranslator.translateColorCodesToTextComponent("&eBalance&7: &a$" + balance));

    }

    public void pickUpATM(Player currentPlayer) {

        if (!currentPlayer.getUniqueId().equals(atm.getOwner())) {
            currentPlayer.sendMessage(MessageUtils.message("You cannot pickup other players' ATMs."));
            return;
        }

        ItemStack atmItem = ATM.createATM(currentPlayer);

        if (currentPlayer.getInventory().addItem(atmItem).size() > 0) {
            currentPlayer.sendMessage(MessageUtils.message("No space in inventory."));
        }else{
            currentPlayer.sendMessage(MessageUtils.message("Successfully picked up ATM. (Maybe add some particles when this happens)"));
            currentPlayer.getWorld().getBlockAt(atm.getLocation()).setType(Material.AIR);
        }
    }
}
