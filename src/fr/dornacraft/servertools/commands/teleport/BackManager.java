package fr.dornacraft.servertools.commands.teleport;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;

/**
 * Classe de gestion de la position précédente d'un joueur
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class BackManager {

	public static final int TIMER = 60;

	private static final HashMap<Player, Location> lastLocations = new HashMap<>();

	/**
	 * @return La liste des dernières positions des joueurs, non null
	 */
	private static HashMap<Player, Location> getLastLocations() {
		return lastLocations;
	}

	/**
	 * @param player Le joueur concerné, non null
	 * @return La dernière localisation du joueur, peut être null
	 */
	public static Location getLastLocation(Player player) {
		return getLastLocations().get(player);
	}

	/**
	 * Stocke la dernière localisation du joueur en mémoire.
	 * 
	 * @param player Le joueur concerné, non null
	 */
	private static void putLastLocation(Player player) {
		getLastLocations().put(player, player.getLocation().clone());
	}

	/**
	 * Retire de la mémoire la dernière localisation du joueur.
	 * 
	 * @param player Le joueur concerné, non null
	 */
	private static void removeLastLocation(Player player) {
		getLastLocations().remove(player);
	}

	/**
	 * @param player Le joueur concerné, non null
	 * @return True si le joueur peut retourner à une position précédente, false
	 *         sinon
	 */
	public static boolean canBack(Player player) {
		return getLastLocations().containsKey(player);
	}

	/**
	 * Sauvegarde la dernière localisation du joueur.
	 * 
	 * @param player Le joueur concerné, non null
	 */
	public static void saveLastLocation(Player player) {
		putLastLocation(player);

		Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(ServerTools.class), new Runnable() {

			@Override
			public void run() {
				removeLastLocation(player);
			}
		}, 20 * TIMER);
	}

	/**
	 * Téléporte le joueur à sa dernière position.
	 * 
	 * @param player Le joueur concerné, non null
	 */
	public static void goBack(Player player) {
		TeleportManager.teleportPlayerTo(player, player, getLastLocation(player),
				"Retour à votre emplacement précédent.");
	}
}
