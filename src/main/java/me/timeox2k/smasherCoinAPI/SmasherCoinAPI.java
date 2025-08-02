package me.timeox2k.smasherCoinAPI;

import me.timeox2k.smasherCoinAPI.manager.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmasherCoinAPI extends JavaPlugin {

    private static SmasherCoinAPI instance;
    private DatabaseManager databaseManager;
    @Override
    public void onEnable() {
        instance = this;
        getConfig().addDefault("MySQL.host", "jdbc:mysql://localhost:3306/smashercoin");
        getConfig().addDefault("MySQL.username", "root");
        getConfig().addDefault("MySQL.password", "");
        getConfig().options().copyDefaults(true);
        saveConfig();

        databaseManager = new DatabaseManager();
    }

    @Override
    public void onDisable() {
       getDatabaseManager().getDataSource().close();
    }

    public static SmasherCoinAPI getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
