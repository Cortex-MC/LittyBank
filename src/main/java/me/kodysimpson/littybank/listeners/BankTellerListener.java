package me.kodysimpson.littybank.listeners;

import me.kodysimpson.littybank.menu.menus.teller.TellerMenu;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;

public class BankTellerListener implements Listener {

    @EventHandler
    public void onEntityInteract(NPCRightClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        new TellerMenu(MenuManager.getPlayerMenuUtility(e.getClicker())).open();

    }

}
