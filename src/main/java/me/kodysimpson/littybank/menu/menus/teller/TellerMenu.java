package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.database.AccountQueries;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.littybank.models.CheckingAccount;
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
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TellerMenu extends Menu {

    private final CheckingAccount checkingAccount;

    public TellerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        checkingAccount = AccountQueries.getAccountForPlayer(p);
    }

    @Override
    public String getMenuName() {
        return "How can I help you today?";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        if (e.getCurrentItem().getType() == Material.SNOW_BLOCK){

            MenuManager.openMenu(SavingsMainMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.ANVIL){

            MenuManager.openMenu(PurchaseATMMenu.class, playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.PAPER) {

            playerMenuUtility.getOwner().closeInventory();
            bankNoteConversation(playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            playerMenuUtility.getOwner().closeInventory();

        }else if (e.getCurrentItem().getType() == Material.CLOCK){

            MenuManager.openMenu(CheckingAccountMenu.class, p);

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack savings = makeItem(Material.SNOW_BLOCK, ColorTranslator.translateColorCodes("&#818de3&lSavings Accounts"),
                ColorTranslator.translateColorCodes("&7Create and manage"), ColorTranslator.translateColorCodes("&7your savings accounts."));

        ItemStack checkingAccount = makeItem(Material.CLOCK, ColorTranslator.translateColorCodes("&#d11919&lChecking Account"),
                "&7Your checking account is",
                "&7the primary method of",
                "&7storing money.", " ",
                "&7Any money here is safe,",
                "&7even when you die.",
                " ", "&#1692fa&lBalance&7: &a$" + this.checkingAccount.getBalance());

        ItemStack bankNotes = makeItem(Material.PAPER, ColorTranslator.translateColorCodes("&#66de2a&lBank Notes"),
                ColorTranslator.translateColorCodes("&7Convert your money to"), ColorTranslator.translateColorCodes("&7bank notes."));

        ItemStack ATM = makeItem(Material.ANVIL, ColorTranslator.translateColorCodes("&#f261b6&lPurchase ATM"),
                ColorTranslator.translateColorCodes("&7Get your very own portable"), ColorTranslator.translateColorCodes("&7ATM for easy access to your"), ColorTranslator.translateColorCodes("&7savings accounts."));

        ItemStack close = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&4&lClose"));

        inventory.setItem(11, savings);
        inventory.setItem(13, checkingAccount);
        inventory.setItem(15, ATM);
        inventory.setItem(31, bankNotes);
        inventory.setItem(49, close);
        setFillerGlass();

    }

    public void bankNoteConversation(Player player) {

        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try { Double.parseDouble(s); return true; }
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
                }else withdrawMoney(Double.parseDouble(s), player);
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

    public void withdrawMoney(double value, Player player) {
        Economy economy = LittyBank.getEconomy();

        if (economy.getBalance(player) >= value){

            EconomyResponse response = economy.withdrawPlayer(player, value);

            if (response.transactionSuccess()){

                ItemStack ATM = BankNote.createBankNote(value);

                player.getInventory().addItem(ATM);
                player.sendMessage(MessageUtils.message("A bank note of $" + value + " has been added to your inventory."));

            }else{

                player.sendMessage(MessageUtils.message(LittyBank.getPlugin().getMessageConfig().getTransactionError()));

            }

        }else{
            player.sendMessage(MessageUtils.message(LittyBank.getPlugin().getMessageConfig().getCantAfford()));
        }
    }
}
