package fr.dornacraft.servertools.commands.teleport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import net.md_5.bungee.api.ChatColor;

/**
 * Classe de gestion des résidences d'un joueur
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class HomeManager {

	public static final int HOME_MAX = 2;

	/**
	 * Récupère si elle existe la résidence du joueur ayant pour nom celui précise
	 * depuis la liste spécifiée.
	 * 
	 * @param player   Le joueur concerné, non null
	 * @param homeName Le nom de la résidence, non null
	 * @return La résidence du joueur si elle existe, peut être null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static Home getHome(Player player, ArrayList<Home> homes, String homeName) throws SQLException {
		Home home = null;
		Iterator<Home> iterator = homes.iterator();

		while (home == null && iterator.hasNext()) {
			// Tant qu'on a pas trouvé la résidence et que l'on peut avancer
			Home h = iterator.next();

			if (h.getName().equalsIgnoreCase(homeName)) { // Si on a trouvé
				home = h;
			}
		} // La résidence peut être null
		return home;
	}

	/**
	 * Définit ou redéfinit la résidence du joueur de nom celui spécifié.
	 * 
	 * @param player   Le joueur concerné, non null
	 * @param homeName Le nom de la résidence à définir, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static void setHome(Player player, String homeName) throws SQLException {
		ArrayList<Home> homes = SQLHome.selectHomes(player);
		Home home = getHome(player, homes, homeName);

		if (home != null) { // Si la résidence existe déjà, on fait un update
			SQLHome.updateHome(player, homeName);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "La résidence §c%s §ra été mise à jour.", homeName);
		} else if (homes.size() < HOME_MAX) {
			// Sinon si le joueur peut avoir une résidence supplémentaire, on
			// lui ajoute une nouvelle résidence en faisant un insert.
			SQLHome.insertHome(player, homeName);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "La résidence §c%s §ra été définie.", homeName);
		} else { // Sinon on envoie un message d'erreur
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player,
					"Impossible de créer une nouvelle résidence : nombre maximum atteint.");
		}
	}

	/**
	 * Supprime la résidence du joueur de nom celui spécifié.
	 * 
	 * @param player   Le joueur concerné, non null
	 * @param homeName Le nom de la résidence à supprimer, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static void deleteHome(Player player, String homeName) throws SQLException {
		Home home = getHome(player, SQLHome.selectHomes(player), homeName);

		if (home != null) {
			SQLHome.deleteHome(player, homeName);
			UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, player, "Vous avez supprimé la résidence §c%s§e.",
					homeName);
			return;
		}
		UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player, "La résidence spécifiée n'existe pas.");
	}

	/**
	 * Affiche au joueur sa liste des résidences si il y en possède.
	 * 
	 * @param player Le joueur concerné, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static void sendHomeList(Player player) throws SQLException {
		final ArrayList<Home> homes = SQLHome.selectHomes(player);

		if (homes.size() > 0) {
			// Si le joueur possède au moins une résidence On construit la liste
			// des homes qu'il possède séparés par une virgule
			StringBuilder homeList = new StringBuilder(ChatColor.AQUA + homes.get(0).getName());

			for (int i = 1; i < homes.size(); i++) {
				homeList.append(ChatColor.GRAY + ", " + ChatColor.AQUA + homes.get(i).getName());
			}
			// Puis on l'envoie au joueur avec une aide sur la commande
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "Usage : §6/home §b<nom>");
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "Voici la liste de vos résidences : %s",
					homeList.toString());
			return;
		}
		UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player, "Vous n'avez aucune résidence.");
	}

	/**
	 * Téléporte le joueur à la résidence spécifiée si elle existe, envoie un
	 * message d'erreur dans le cas contraire
	 * 
	 * @param player Le joueur concerné, non null
	 * @param home   La résidence ciblée, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static void teleportToHome(Player player, String homeName) throws SQLException {
		Home home = SQLHome.selectHome(player, homeName);

		if (home != null) {
			// Si la résidence spécifiée existe, on téléporte le joueur
			TeleportManager.teleportPlayerTo(player, player, home.getLocation(), "Téléportation à votre résidence.");
			return;
		}
		UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player, "La résidence spécifiée n'existe pas.");
	}

}
