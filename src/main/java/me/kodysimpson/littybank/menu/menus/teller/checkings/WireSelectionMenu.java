package me.kodysimpson.littybank.menu.menus.teller.checkings;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.database.AccountQueries;
import me.kodysimpson.littybank.menu.menus.teller.checkings.CheckingAccountMenu;
import me.kodysimpson.littybank.models.BankNote;
import me.kodysimpson.littybank.models.CheckingAccount;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PaginatedMenu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WireSelectionMenu extends PaginatedMenu {

    public WireSelectionMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public List<?> getData() {
        return Bukkit.getOnlinePlayers().stream().filter(player -> !player.getUniqueId().equals(p.getUniqueId())).collect(Collectors.toList());
    }

    @Override
    public void loopCode(Object o) {
        Player p = (Player) o;
        ItemStack item = makeItem(Material.PLAYER_HEAD, p.getDisplayName(), "&7#" + p.getUniqueId(),
                " ", "&b* &#843897Left click to wire money");
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(LittyBank.getPlugin(), "uuid"), PersistentDataType.STRING, p.getUniqueId().toString());
        item.setItemMeta(itemMeta);
        inventory.addItem(item);
    }

    @Override
    public @Nullable HashMap<Integer, ItemStack> getCustomMenuBorderItems() {
        return null;
    }

    @Override
    public String getMenuName() {
        return "Choose an Active Account";
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
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws MenuManagerNotSetupException, MenuManagerException {

        if (inventoryClickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD){
            wireTransferConversation(p, Bukkit.getPlayer(UUID.fromString(inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(LittyBank.getPlugin(), "uuid"), PersistentDataType.STRING))));
        }else if (inventoryClickEvent.getCurrentItem().getType() == Material.BARRIER){
            MenuManager.openMenu(CheckingAccountMenu.class, p);
        }

    }

    private void wireTransferConversation(Player player, Player target) {

        CheckingAccount checkingAccount = AccountQueries.getAccountForPlayer(player);
        CheckingAccount targetAccount = AccountQueries.getAccountForPlayer(target);

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

                        //Make sure the player has enough money in their checking account to wire
                        if (checkingAccount.getBalance() >= amount){

                            targetAccount.setBalance(targetAccount.getBalance() + amount);
                            checkingAccount.setBalance(checkingAccount.getBalance() - amount);

                            player.sendMessage("$" + amount + " has been wired from your checking account to " + target.getDisplayName() + " 's account.");
                            target.sendMessage(player.getDisplayName() + " has sent you $" + amount + "! This will be available in your checking account.");

                        }else{
                            player.sendMessage("You cannot afford this. Your current checking account balance is " + checkingAccount.getBalance());
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        player.sendMessage(MessageUtils.message("The transaction could not be completed. Try again later."));
                    }
                }
                BankNote.removePlayerInConversation(player);
                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the amount of money to wire to " + target.getDisplayName() + ". Your limit is " + checkingAccount.getBalance() + " \n(Type \"cancel\" to cancel)");
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
}
