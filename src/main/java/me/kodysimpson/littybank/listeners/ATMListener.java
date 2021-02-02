package me.kodysimpson.littybank.listeners;

import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.menu.atm.ATMMenu;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

public class ATMListener implements Listener {

    @EventHandler
    public void openATM(PlayerInteractEvent e) throws MenuManagerException {

        if (e.getClickedBlock() != null){
            PlayerMenuUtility playerMenuUtility = MenuManager.getPlayerMenuUtility(e.getPlayer(), PlayerMenuUtility.class);
            playerMenuUtility.setAtmLocation(e.getClickedBlock().getLocation());
        }


    }

    @EventHandler
    public void onAnvilOpen(InventoryOpenEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        if (e.getInventory().getType() == InventoryType.ANVIL){

            MenuManager.openMenu(ATMMenu.class, (Player) e.getPlayer());

            //new ATMMenu(MenuManager.getPlayerMenuUtility((Player) e.getPlayer(), PlayerMenuUtility.class)).open();
            e.setCancelled(true);

        }

    }

}
