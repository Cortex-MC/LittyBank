package me.kodysimpson.littybank.listeners;

import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.menu.atm.ATMMenu;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;

public class ATMListener implements Listener {

    @EventHandler
    public void openATM(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException  {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ANVIL){
            if (!ATM.getAtmLocations().contains(e.getClickedBlock().getLocation())) return;
            e.setCancelled(true);

            PlayerMenuUtility playerMenuUtility = MenuManager.getPlayerMenuUtility(e.getPlayer(), PlayerMenuUtility.class);
            playerMenuUtility.setAtmLocation(e.getClickedBlock().getLocation());

            MenuManager.openMenu(ATMMenu.class, e.getPlayer());
        }

    }

    @EventHandler
    public void setATM(BlockPlaceEvent e) {

        if (ATM.isValidATM(e.getItemInHand())) {
            ATM.getAtmLocations().add(e.getBlockPlaced().getLocation());
        }
    }

//    @EventHandler
//    public void onAnvilOpen(InventoryOpenEvent e) throws MenuManagerNotSetupException, MenuManagerException {
//
//        if (e.getInventory().getType() == InventoryType.ANVIL) e.setCancelled(true);
//
//    }

}
