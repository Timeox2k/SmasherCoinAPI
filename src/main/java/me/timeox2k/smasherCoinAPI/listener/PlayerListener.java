package me.timeox2k.smasherCoinAPI.listener;

import me.timeox2k.smasherCoinAPI.SmasherCoinAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        SmasherCoinAPI.getInstance().getDatabaseManager().createPlayer(event.getPlayer());
    }

}
