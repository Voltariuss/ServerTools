package fr.dornacraft.servertools.controller.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.dornacraft.servertools.model.managers.GodPlayerManager;

/**
 * Classe d'écoute des événements de dégâts d'un joueur en god mode.
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class GodPlayerDamageListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			// si le joueur est en god mode, on bloque les dégâts
			if (GodPlayerManager.getInstance().isGod(player)) {
				event.setCancelled(true);
			}
		}
	}
}
