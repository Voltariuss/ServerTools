package fr.dornacraft.servertools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.dornacraft.servertools.utils.UtilsEssentials;

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
		if (UtilsEssentials.SPAWN_LOCATION != null) {
			event.setRespawnLocation(UtilsEssentials.SPAWN_LOCATION);			
		}
	}
}
