package fr.dornacraft.servertools.controller.listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe d'écoute des mouvements d'un joueur lors d'une téléportation
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class PlayerMoveListener implements Listener {

	private static final HashMap<Player, BukkitTask> players = new HashMap<>();

	/**
	 * @return La liste des joueurs en cours de téléportation, non null
	 */
	private static HashMap<Player, BukkitTask> getPlayers() {
		return players;
	}

	/**
	 * @param player Le joueur en cours de téléportation, non null
	 * @return L'exécuteur de la téléportation après le délais correspondant, peut
	 *         être null
	 */
	public static BukkitTask getTask(Player player) {
		return getPlayers().get(player);
	}

	/**
	 * Associe le joueur ciblé à une téléportation en cours.
	 * 
	 * @param player Le joueur ciblé, non null
	 * @param task   La téléportation avec un délais, non null
	 */
	public static void addPlayer(Player player, BukkitTask task) {
		getPlayers().put(player, task);
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 250, 1, true, false));
	}

	/**
	 * On supprime le joueur et la téléportation.
	 * 
	 * @param player Le joueur ciblé, non null
	 */
	public static void removePlayer(Player player) {
		getPlayers().remove(player);
		player.removePotionEffect(PotionEffectType.CONFUSION);
	}

	/**
	 * @param player Le joueur ciblé, non null
	 * @return Vrai si le joueur est en cours de téléportation, faux sinon
	 */
	public static boolean isInTeleportation(Player player) {
		return getPlayers().containsKey(player);
	}

	/**
	 * Annule la téléportation en cours du joueur ciblé.
	 * 
	 * @param player Le joueur ciblé, non null
	 */
	public static void cancelTeleportation(Player player) {
		// on annule le délais de téléportation
		getTask(player).cancel();
		// et on supprime la téléportation du joueur de la liste
		removePlayer(player);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();

		if (players.containsKey(player) && (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY()
				|| from.getBlockZ() != to.getBlockZ())) {
			cancelTeleportation(player);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "§cTéléportation annulée suite à un déplacement.");
		}
	}
}
