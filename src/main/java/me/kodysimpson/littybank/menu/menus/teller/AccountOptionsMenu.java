package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.database.Database;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.littybank.models.SavingsAccount;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class AccountOptionsMenu extends Menu {

    private final SavingsAccount account;
    private final Economy economy;

    public AccountOptionsMenu(PlayerMenuUtility pmu) throws SQLException {
        super(pmu);
        account = Database.getSavingsDao().queryForId(playerMenuUtility.getData(MenuData.ACCOUNT_ID, Integer.class));
        economy = LittyBank.getEconomy();
    }


    @Override
    public String getMenuName() {
        return ColorTranslator.translateColorCodes("&#e64764&lAccount &#d42847" + SavingsAccount.formatId(this.account.getId()));
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

        switch (e.getCurrentItem().getType()) {
            case LAVA_BUCKET -> {
                p.closeInventory();
                MenuManager.openMenu(ConfirmDeleteMenu.class, p);
            }
            case GREEN_CONCRETE -> {
                p.closeInventory();
                startWithdrawConversation();
            }
            case BLUE_CONCRETE -> {
                p.closeInventory();
                startDepositConversation();
            }
            case BARRIER -> MenuManager.openMenu(AccountsListMenu.class, p);
        }

    }

    @Override
    public void setMenuItems() {
        ItemStack withdraw = makeItem(Material.GREEN_CONCRETE, ColorTranslator.translateColorCodes("&#23eb17&lWithdraw"),
                ColorTranslator.translateColorCodes("&7Withdraw money from this"), ColorTranslator.translateColorCodes("&7account to your balance."));
        ItemStack deposit = makeItem(Material.BLUE_CONCRETE, ColorTranslator.translateColorCodes("&#6e54e3&lDeposit"),
                ColorTranslator.translateColorCodes("&7Deposit money from your"), ColorTranslator.translateColorCodes("&7balance to this account."));
        ItemStack info = makeItem(Material.WRITABLE_BOOK, ColorTranslator.translateColorCodes("Information"));
        ItemStack delete = makeItem(Material.LAVA_BUCKET, ColorTranslator.translateColorCodes("&#e35954&lDelete Account"),
                ColorTranslator.translateColorCodes("&7Permanently delete this account."), ColorTranslator.translateColorCodes("&7Available balance will be"), ColorTranslator.translateColorCodes("&7deposited to your balance."));
        ItemStack back = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lBack"));

        inventory.setItem(0, withdraw);
        inventory.setItem(2, deposit);
        inventory.setItem(4, info);
        inventory.setItem(6, delete);
        inventory.setItem(8, back);

        setFillerGlass();
    }

    public void startWithdrawConversation() {

        double balance = account.getBalance();

        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try {
                    double amount = Double.parseDouble(s);

                    if (balance >= amount) return true;

                    p.sendMessage(MessageUtils.message("Not enough balance in bank"));
                    return false;

                }
                catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")) return true;
                    p.sendMessage(MessageUtils.message("Please enter a valid amount"));
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    p.sendMessage(MessageUtils.message("Transaction has been cancelled"));
                }else {
                    try {
                        withdrawMoney(Double.parseDouble(s));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                BankNote.removePlayerInConversation(p);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the amount to withdraw (Type \"cancel\" to cancel)");
            }
        };

        BankNote.addPlayerInConversation(p);
        new ConversationFactory(LittyBank.getPlugin())
                .withModality(false)
                .withTimeout(15)
                .withFirstPrompt(enterAmount)
                .buildConversation(p)
                .begin();
    }

    public void withdrawMoney(double value) throws SQLException {
        ItemStack note = BankNote.createBankNote(value);

        if (p.getInventory().addItem(note).size() > 0) p.sendMessage(MessageUtils.message("Transaction unsuccessful. No empty slot in inventory"));
        else {

            account.setBalance(account.getBalance() - value);
            Database.getSavingsDao().update(account);

            p.sendMessage(MessageUtils.message("Successfully withdrawn a $" + value + " note from the bank"));
        }

    }

    public void startDepositConversation() {

        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try {
                    double amount = Double.parseDouble(s);

                    if (economy.getBalance(p) >= amount) return true;

                    p.sendMessage(MessageUtils.message("You don't have enough money"));
                    return false;

                }
                catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")) return true;
                    p.sendMessage(MessageUtils.message("Please enter a valid amount"));
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    p.sendMessage(MessageUtils.message("Transaction has been cancelled"));
                }else {
                    try {
                        depositMoney(Double.parseDouble(s));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                BankNote.removePlayerInConversation(p);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the amount to withdraw (Type \"cancel\" to cancel)");
            }
        };

        BankNote.addPlayerInConversation(p);
        new ConversationFactory(LittyBank.getPlugin())
                .withModality(false)
                .withTimeout(15)
                .withFirstPrompt(enterAmount)
                .buildConversation(p)
                .begin();
    }

    public void depositMoney(double value) throws SQLException {

        EconomyResponse response = economy.withdrawPlayer(p, value);

        if (response.transactionSuccess()){

            account.setBalance(account.getBalance() + value);
            Database.getSavingsDao().update(account);

            p.sendMessage(MessageUtils.message("Successfully deposited $" + value + " in the bank"));

        }else{

            p.sendMessage(MessageUtils.message("Transaction Error. Try again later."));

        }

    }



}
