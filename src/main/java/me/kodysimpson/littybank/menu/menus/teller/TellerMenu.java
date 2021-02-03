package me.kodysimpson.littybank.menu.menus.teller;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.SelfCancelledMenu;
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

import java.util.ArrayList;
import java.util.List;

public class TellerMenu extends Menu implements SelfCancelledMenu {

    public TellerMenu(AbstractPlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "How can I help you today?";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        PlayerMenuUtility playerMenuUtility = PMUCaster(pmu, PlayerMenuUtility.class);

        if (e.getCurrentItem().getType() == Material.PAPER){

            MenuManager.openMenu(SavingsMainMenu.class, pmu);

        }else if (e.getCurrentItem().getType() == Material.ANVIL){

            MenuManager.openMenu(PurchaseATMMenu.class, pmu);

        }else if (e.getCurrentItem().getType() == Material.WRITTEN_BOOK) {

            playerMenuUtility.getOwner().closeInventory();
            bankNoteConversation(playerMenuUtility.getOwner());

        }else if (e.getCurrentItem().getType() == Material.BARRIER){

            playerMenuUtility.getOwner().closeInventory();

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack savings = makeItem(Material.PAPER, "Savings Accounts");

        ItemStack bankNotes = makeItem(Material.WRITTEN_BOOK, "Bank Notes");

        ItemStack ATM = makeItem(Material.ANVIL, "Purchase ATM");

        ItemStack close = makeItem(Material.BARRIER, "Close");

        inventory.setItem(11, savings);
        inventory.setItem(13, bankNotes);
        inventory.setItem(15, ATM);
        inventory.setItem(26, close);
        setFillerGlass();

    }

    public void bankNoteConversation(Player player) {
        List<String> list = new ArrayList<>();
        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try { Float.parseFloat(s); return true; }
                catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")) return true;
                    player.sendMessage("Please enter a valid amount");
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    player.sendMessage("Transaction has been cancelled");
                }else withdrawMoney(Float.parseFloat(s), player);
                BankNote.removePlayerInConversation(player);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return "\nEnter the amount to withdraw (Type \"cancel\" to cancel)";
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

    public void withdrawMoney(float value, Player player) {
        Economy economy = LittyBank.getEconomy();

        if (economy.getBalance(player) >= value){

            EconomyResponse response = economy.withdrawPlayer(player, value);

            if (response.transactionSuccess()){

                ItemStack ATM = BankNote.createBankNote(value);

                player.getInventory().addItem(ATM);
                player.sendMessage("A bank note of $" + value + " has been added to your inventory.");

            }else{

                player.sendMessage("Transaction Error. Try again later.");

            }

        }else{

            player.sendMessage("You cant afford it you poor bitch.");

        }
    }
}
