package me.kodysimpson.littybank.menu.atm;

import me.kodysimpson.littybank.LittyBank;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import me.kodysimpson.simpapi.menu.Menu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ATMMenu extends Menu {

    public ATMMenu(AbstractPlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Automated Teller Machine";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        PlayerMenuUtility playerMenuUtility = (PlayerMenuUtility) pmu;

        if (e.getCurrentItem().getType() == Material.BARRIER){

            playerMenuUtility.getOwner().closeInventory();

        }else if (e.getCurrentItem().getType() == Material.BIRCH_SIGN){

            playerMenuUtility.getOwner().closeInventory();

            Economy economy = LittyBank.getEconomy();
            double balance = economy.getBalance(playerMenuUtility.getOwner());

            ArmorStand balanceH = (ArmorStand) playerMenuUtility.getOwner().getWorld().spawnEntity(playerMenuUtility.getOwner().getLocation(), EntityType.ARMOR_STAND);
            balanceH.setCustomNameVisible(true);
            balanceH.setCustomName("Checking Balance: $" + balance);
            balanceH.setVisible(false);
            balanceH.setInvulnerable(true);
            balanceH.teleport(playerMenuUtility.getAtmLocation().add(0.5, -0.25, 0.5));

        }else if(e.getCurrentItem().getType() == Material.PAPER){

            System.out.println("ewr");

        }

    }

    @Override
    public void setMenuItems() {

        ItemStack close = makeItem(Material.BARRIER, "Close");

        ItemStack balance = makeItem(Material.BIRCH_SIGN, "See Balance");

        ItemStack withdraw = makeItem(Material.PAPER, "Withdraw Funds");

        inventory.setItem(2, close);
        inventory.setItem(4, balance);
        inventory.setItem(6, withdraw);

    }
}
