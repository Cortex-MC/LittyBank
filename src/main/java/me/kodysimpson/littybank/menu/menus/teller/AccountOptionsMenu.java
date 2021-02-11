package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AccountOptionsMenu extends Menu {

    private double balance;
    private PlayerMenuUtility playerMenuUtility;
    private String accountName;
    private int id;
    private Economy economy;
    private Player player;

    public AccountOptionsMenu(AbstractPlayerMenuUtility pmu) {
        super(pmu);
        playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);
        accountName = playerMenuUtility.getAccountName();
        id = Integer.parseInt(accountName.substring(accountName.indexOf("#")+1));
        balance = Database.getBalance(id);
        economy = LittyBank.getEconomy();
        player = pmu.getOwner();
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
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        if (e.getCurrentItem().getType() == Material.TOTEM_OF_UNDYING) {

            player.closeInventory();
            deleteAccount();

        }else if (e.getCurrentItem().getType() == Material.GREEN_CONCRETE) {

            player.closeInventory();
            startWithdrawConversation();

        }else if (e.getCurrentItem().getType() == Material.BLUE_CONCRETE) {

            player.closeInventory();
            startDepositConversation();

        }else if (e.getCurrentItem().getType() == Material.BARRIER) {

            MenuManager.openMenu(SavingsAccountsMenu.class, player);

        }

    }

    @Override
    public void setMenuItems() {
        ItemStack withdraw = makeItem(Material.GREEN_CONCRETE, ColorTranslator.translateColorCodes("&#23eb17&lWithdraw"),
                ColorTranslator.translateColorCodes("&7Withdraw money from this"), ColorTranslator.translateColorCodes("&7account to your balance."));
        ItemStack deposit = makeItem(Material.BLUE_CONCRETE, ColorTranslator.translateColorCodes("&#6e54e3&lDeposit"),
                ColorTranslator.translateColorCodes("&7Deposit money from your"), ColorTranslator.translateColorCodes("&7balance to this account."));
        ItemStack delete = makeItem(Material.TOTEM_OF_UNDYING, ColorTranslator.translateColorCodes("&#e35954&lDelete Account"),
                ColorTranslator.translateColorCodes("&7Permanently delete this account."), ColorTranslator.translateColorCodes("&7Available balance will be"), ColorTranslator.translateColorCodes("&7deposited to your balance."));
        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));

        inventory.setItem(0, withdraw);
        inventory.setItem(2, deposit);
        inventory.setItem(5, delete);
        inventory.setItem(8, back);
    }

    public void deleteAccount() {

        EconomyResponse response = economy.depositPlayer(player, balance);

        if (response.transactionSuccess()){

            Database.deleteAccount(id);

            player.sendMessage(MessageUtils.message("Your account has been successfully deleted and $" + balance + " has been added to your balance.\n" +
                    "Will add a confirmation menu later"));

        }else{

            player.sendMessage(MessageUtils.message("Transaction Error. Try again later."));

        }
    }

    public void startWithdrawConversation() {

        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try {
                    double amount = Double.parseDouble(s);

                    if (balance >= amount) return true;

                    player.sendMessage(MessageUtils.message("Not enough balance in bank"));
                    return false;

                }
                catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")) return true;
                    player.sendMessage(MessageUtils.message("Please enter a valid amount"));
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    player.sendMessage(MessageUtils.message("Transaction has been cancelled"));
                }else withdrawMoney(Double.parseDouble(s));
                BankNote.removePlayerInConversation(player);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the amount to withdraw (Type \"cancel\" to cancel)");
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

    public void withdrawMoney(double value) {
        ItemStack note = BankNote.createBankNote(value);

        if (player.getInventory().addItem(note).size() > 0) player.sendMessage(MessageUtils.message("Transaction unsuccessful. No empty slot in inventory"));
        else {
            Database.setBalance(id, balance-value);
            player.sendMessage(MessageUtils.message("Successfully withdrawn a $" + value + " note from the bank"));
        }

    }

    public void startDepositConversation() {

        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try {
                    double amount = Double.parseDouble(s);

                    if (economy.getBalance(player) >= amount) return true;

                    player.sendMessage(MessageUtils.message("You don't have enough money"));
                    return false;

                }
                catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")) return true;
                    player.sendMessage(MessageUtils.message("Please enter a valid amount"));
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    player.sendMessage(MessageUtils.message("Transaction has been cancelled"));
                }else depositMoney(Double.parseDouble(s));
                BankNote.removePlayerInConversation(player);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the amount to withdraw (Type \"cancel\" to cancel)");
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

    public void depositMoney(double value) {

        EconomyResponse response = economy.withdrawPlayer(player, value);

        if (response.transactionSuccess()){

            Database.setBalance(id, balance + value);

            player.sendMessage(MessageUtils.message("Successfully deposited $" + value + " in the bank"));

        }else{

            player.sendMessage(MessageUtils.message("Transaction Error. Try again later."));

        }

    }



}
