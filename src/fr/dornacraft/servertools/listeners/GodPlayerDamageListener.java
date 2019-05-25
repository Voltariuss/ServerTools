package fr.dornacraft.servertools.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Classe d'écoute des événements de dégâts d'un joueur en god mode.
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class GodPlayerDamageListener implements Listener {

	private static HashMap<UUID, Player> godsList = new HashMap<>();

	/**
	 * @return La liste des joueurs en god mode, non null
	 */
	private static HashMap<UUID, Player> getGodsList() {
		return godsList;
	}

	/**
	 * Supprime le joueur de la liste des joueurs en god mode.
	 * 
	 * @param player Le joueur ciblé, non null
	 */
	public static void removeGod(Player player) {
		getGodsList().remove(player.getUniqueId());
	}

	/**
	 * Ajoute le joueur à la liste des joueurs en god mode.
	 * 
	 * @param player Le joueur ciblé, non null
	 */
	public static void addGod(Player player) {
		getGodsList().put(player.getUniqueId(), player);
	}

	/**
	 * @param player Le joueur ciblé, non null
	 * @return Vrai si le joueur est en god mode, faux sinon
	 */
	public static boolean isGod(Player player) {
		return getGodsList().containsKey(player.getUniqueId());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			// si le joueur est en god mode, on bloque les dégâts
			if (godsList.containsKey(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}
}
