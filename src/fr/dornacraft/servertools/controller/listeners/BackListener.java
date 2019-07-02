package fr.dornacraft.servertools.controller.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.dornacraft.servertools.model.managers.BackManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe d'écoute des événements liés à la mort ou à la téléportation d'un
 * joueur
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class BackListener implements Listener {

	public static final String MSG_DELAY_WARNING = String.format(
			"Vous avez §6%s secondes §cpour retourner au lieu de votre mort en faisant §b/back§c.", BackManager.TIMER);

	public static final String PERM_BACK = "dornacraft.essentials.back";

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().hasPermission(PERM_BACK)) {
			BackManager.saveLastLocation(e.getEntity());
			UtilsAPI.sendSystemMessage(MessageLevel.WARNING, e.getEntity(), MSG_DELAY_WARNING);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (e.getPlayer().hasPermission(PERM_BACK)) {
			BackManager.saveLastLocation(e.getPlayer());
		}
	}
}
