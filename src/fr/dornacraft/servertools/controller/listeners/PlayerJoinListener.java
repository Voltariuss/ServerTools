package fr.dornacraft.servertools.controller.listeners;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.dornacraft.servertools.model.managers.PlayerManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        try {
            PlayerManager.checkPlayerPersistence(player);
        } catch (SQLException e) {
            e.printStackTrace();
            UtilsAPI.sendSystemMessage(MessageLevel.ERROR, player, UtilsAPI.INTERNAL_EXCEPTION);
        }
    }
}