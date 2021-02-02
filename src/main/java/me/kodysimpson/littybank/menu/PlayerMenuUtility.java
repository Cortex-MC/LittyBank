package me.kodysimpson.littybank.menu;

import me.kodysimpson.simpapi.menu.AbstractPlayerMenuUtility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerMenuUtility extends AbstractPlayerMenuUtility {

    private Location atmLocation;

    public PlayerMenuUtility(Player p) {
        super(p);
    }

    public Location getAtmLocation() {
        return atmLocation;
    }

    public void setAtmLocation(Location atmLocation) {
        this.atmLocation = atmLocation;
    }

}
