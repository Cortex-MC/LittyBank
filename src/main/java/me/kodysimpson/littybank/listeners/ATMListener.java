package me.kodysimpson.littybank.listeners;

import me.kodysimpson.littybank.Database;
import me.kodysimpson.littybank.menu.Data;
import me.kodysimpson.littybank.menu.atm.ATMMenu;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ATMListener implements Listener {

    @EventHandler
    public void openATM(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException  {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ANVIL){

            if (!Database.isATMLocation(e.getClickedBlock().getLocation())) return;
            e.setCancelled(true);

            PlayerMenuUtility playerMenuUtility = MenuManager.getPlayerMenuUtility(e.getPlayer());
            playerMenuUtility.setData(Data.ATM_LOCATION, e.getClickedBlock().getLocation());

            MenuManager.openMenu(ATMMenu.class, e.getPlayer());
        }

    }

    @EventHandler
    public void placeATM(BlockPlaceEvent e) {

        if (ATM.isValidATM(e.getItemInHand())) {
            ATM atm = new ATM(e.getItemInHand());
            atm.setLocation(e.getBlockPlaced().getLocation());
            Database.addATM(atm);
        }
    }

    @EventHandler
    public void removeATM(BlockBreakEvent e) {

        if (e.getBlock().getType() == Material.ANVIL && Database.isATMLocation(e.getBlock().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(MessageUtils.message("You cannot break ATMs. Pick them up from their GUI. Change this message please."));
        }
    }

}
