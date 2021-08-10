package me.kodysimpson.littybank.listeners;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.database.ATMQueries;
import me.kodysimpson.littybank.database.Database;
import me.kodysimpson.littybank.menu.MenuData;
import me.kodysimpson.littybank.menu.atm.ATMMenu;
import me.kodysimpson.littybank.models.ATM;
import me.kodysimpson.littybank.utils.MessageUtils;
import me.kodysimpson.littybank.utils.Serializer;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;

public class ATMListener implements Listener {

    @EventHandler
    public void openATM(PlayerInteractEvent e) throws MenuManagerNotSetupException, MenuManagerException, SQLException {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ANVIL){

            //Get the ATM from the DB from its location
            ATM atm = ATMQueries.getATMFromLocation(e.getClickedBlock().getLocation());

            if (atm != null){
                e.setCancelled(true);
                PlayerMenuUtility playerMenuUtility = MenuManager.getPlayerMenuUtility(e.getPlayer());
                playerMenuUtility.setData(MenuData.ATM, atm);

                MenuManager.openMenu(ATMMenu.class, e.getPlayer());
            }
        }

    }

    @EventHandler
    public void placeATM(BlockPlaceEvent e) throws SQLException {

        if (ATM.isValidATM(e.getItemInHand())) {

            Block block = e.getBlockPlaced();

            //Create a new ATM from the Item and location of the placed block
            ATM atm = new ATM(e.getItemInHand(), block.getLocation());

            Database.getAtmDao().create(atm);

            e.getPlayer().sendMessage(MessageUtils.message("You have placed your ATM on the ground. Holy shift and right click to access it."));
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
