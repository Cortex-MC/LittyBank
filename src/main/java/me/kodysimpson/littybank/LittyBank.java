package me.kodysimpson.littybank;

import me.kodysimpson.littybank.commands.CreateTellerCommand;
import me.kodysimpson.littybank.listeners.ATMListener;
import me.kodysimpson.littybank.listeners.BankTellerListener;
import me.kodysimpson.littybank.menu.PlayerMenuUtility;
import me.kodysimpson.littybank.tasks.InterestTask;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LittyBank extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static LittyBank plugin;

    private static String url;

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {

        if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;

        //Setup Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Create database tables if not already generated
        url = "jdbc:h2:" + getDataFolder().getAbsolutePath() + "/data/littybank";
        Database.initializeDatabase();

        getServer().getPluginManager().registerEvents(new BankTellerListener(), this);
        getServer().getPluginManager().registerEvents(new ATMListener(), this);

        MenuManager.setup(getServer(), this, PlayerMenuUtility.class);

        try {
            CommandManager.createCoreCommand(this, "litty", "litty bank core", "/litty", Arrays.asList("pickle"),  CreateTellerCommand.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        new InterestTask().runTaskTimerAsynchronously(this, 20, 1200);

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static LittyBank getPlugin() {
        return plugin;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static String getConnectionUrl() {
        return url;
    }
}
