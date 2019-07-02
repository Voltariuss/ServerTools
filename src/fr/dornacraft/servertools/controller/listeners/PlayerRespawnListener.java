package fr.dornacraft.servertools.controller.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.utils.Utils;

/**
 * Classe d'écoute des événements de respawn d'un joueur
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class PlayerRespawnListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (ServerTools.getInstance().getConfig().getBoolean("respawn_to_spawn_location")) {
			event.setRespawnLocation(Utils.SPAWN_LOCATION);			
		}
	}
}
