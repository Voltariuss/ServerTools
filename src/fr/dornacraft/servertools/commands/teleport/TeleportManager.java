package fr.dornacraft.servertools.commands.teleport;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.listeners.PlayerMoveListener;
import fr.dornacraft.servertools.utils.UtilsEssentials;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe de gestion des téléportations
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class TeleportManager {

	public static final String NO_REQUEST = "Vous n'avez aucune requête en attente de réponse.";

	private static final HashMap<Player, TPRequest> requests = new HashMap<>();

	/**
	 * @return La liste des requêtes en cours, non null
	 */
	private static HashMap<Player, TPRequest> getRequests() {
		return requests;
	}

	/**
	 * @param receiver Le récepteur de la requête, non null
	 * @return La requête en cours pour le joueur ciblé, peut être null
	 */
	public static TPRequest getTPRequest(Player receiver) {
		return getRequests().get(receiver);
	}

	/**
	 * Stocke la requête en cours en mémoire.
	 * 
	 * @param receiver  Le récepteur de la requête, non null
	 * @param tpRequest La requête de téléportation, non null
	 */
	public static void addRequest(Player receiver, TPRequest tpRequest) {
		getRequests().put(receiver, tpRequest);
	}

	/**
	 * Retire la requête associée au récepteur de la mémoire.
	 * 
	 * @param receiver Le récepteur de la requête, non null
	 */
	public static void removeRequest(Player receiver) {
		getRequests().remove(receiver);
	}

	/**
	 * @param receiver Le récepteur de la requête, non null
	 * @return True si le joueur a une requête de téléportation en cours, false
	 *         sinon
	 */
	public static boolean hasRequest(Player receiver) {
		return getRequests().containsKey(receiver);
	}

	public static final String PERMISSION_TELEPORT_OTHERS = "dornacraft.essentials.teleport.others";
	public static final String PERMISSION_TELEPORT_INSTANT = "dornacraft.essentials.teleport.instant";

	/**
	 * Téléporte le joueur ciblé à la localisation spécifiée.
	 * 
	 * @param sender                 L'émetteur de la requête, non null
	 * @param player                 Le joueur ciblé, non null
	 * @param location               La localisation du point de téléportation, non
	 *                               null
	 * @param teleportSuccessMessage Le message de succès de la téléportation du
	 *                               joueur, non null
	 */
	public static void teleportPlayerTo(CommandSender sender, Player player, Location location,
			String teleportSuccessMessage) {
		boolean isSeparatedSender = sender != null && !player.getName().equals(sender.getName());

		if (!player.hasPermission(PERMISSION_TELEPORT_INSTANT)) {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "Téléportation dans §65 secondes§r...");

			if (PlayerMoveListener.isInTeleportation(player)) {
				PlayerMoveListener.getTask(player).cancel();
				PlayerMoveListener.removePlayer(player);
			}
			PlayerMoveListener.addPlayer(player,
					Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(ServerTools.class), new Runnable() {

						@Override
						public void run() {
							PlayerMoveListener.removePlayer(player);
							UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, teleportSuccessMessage);

							if (!player.getAllowFlight()) {
								int y = location.getBlockY() - 1;
								Material material = Bukkit.getWorld(location.getWorld().getName())
										.getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType();

								while ((material == null || material == Material.AIR) && location.getBlockY() > 0) {
									location.setY(y);
									y--;
									material = Bukkit.getWorld(location.getWorld().getName())
											.getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType();
								}

								if (location.getY() <= 0) {
									UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player,
											"Téléportation annulée : zone de destination non-sécurisée.");

									if (isSeparatedSender) {
										UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender,
												"Téléportation impossible du joueur §b%s§r.", player.getName());
									}
									return;
								}
							}
							player.teleport(location);
							if (isSeparatedSender) {
								UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, sender,
										"Le joueur §b%s §ra bien été téléporté.", player.getName());
							}
						}

					}, 100));
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, teleportSuccessMessage);
			player.teleport(location);

			if (isSeparatedSender) {
				UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, sender, "Le joueur §b%s §ra bien été téléporté.",
						player.getName());
			}
		}
	}

	/**
	 * Envoie une requête de téléportation au joueur récepteur.
	 * 
	 * @param transmitter Le transmetteur de la requête, non null
	 * @param receiver    Le récepteur de la requête, non null
	 * @param typeRequest Le type de requête, non null
	 */
	public static void sendRequest(Player transmitter, Player receiver, TypeRequest typeRequest) {
		addRequest(receiver, new TPRequest(transmitter, receiver, typeRequest));

		if (typeRequest != TypeRequest.ALL_TELEPORT_TO_TRANSMITTER) {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, transmitter, "Requête de téléportation envoyée à §b%s§r.",
					receiver.getName());
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, transmitter, "Requête de téléportation générale envoyée.");
		}
		receiver.sendMessage(UtilsEssentials.getHeader("Requête de téléportation"));
		receiver.sendMessage(UtilsEssentials.getNewLine("Émetteur", transmitter.getName()));

		if (typeRequest == TypeRequest.TRANSMITTER_TELEPORT_TO_RECEIVER) {
			receiver.sendMessage(
					UtilsEssentials.getNewLine("Description", "Le joueur souhaite §c§ose téléporter vers vous"));
		} else {
			receiver.sendMessage(
					UtilsEssentials.getNewLine("Description", "Le joueur demande §c§oà vous téléporter vers lui"));
		}
		receiver.sendMessage("§7Pour accepter la demande, §a/tpaccept§7, sinon §c/tpdeny§7.");
		receiver.sendMessage("§7Cette demande de téléportation expirera dans 60 secondes.");
		receiver.sendMessage("§4§lAttention : §cLe Tp-kill est autorisé.\n");
	}

	/**
	 * Accepte la requête de téléportation reçue.
	 * 
	 * @param receiver Le récepteur de la requête, non null
	 */
	public static void acceptRequest(Player receiver) {
		if (hasRequest(receiver)) {
			TPRequest request = getTPRequest(receiver);
			Player transmitter = request.getTransmitter();
			request.getTimeout().cancel();
			removeRequest(receiver);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, transmitter, "Le joueur §b%s §ra §aaccepté §evotre requête.",
					receiver.getName());
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, receiver,
					"Vous avez §aaccepté §rla requête du joueur §b%s§r.", transmitter.getName());
			request.getLocation().setPitch(0);

			if (request.getTypeRequest() == TypeRequest.TRANSMITTER_TELEPORT_TO_RECEIVER) {
				teleportPlayerTo(null, transmitter, request.getLocation(), "Début de la téléportation...");
			} else {
				teleportPlayerTo(null, receiver, request.getLocation(), "Début de la téléportation...");
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, receiver, NO_REQUEST);
		}
	}

	/**
	 * Refuse la requête de téléportation reçue.
	 * 
	 * @param receiver Le récepteur de la requête, non null
	 */
	public static void denyRequest(Player receiver) {
		if (hasRequest(receiver)) {
			TPRequest request = getTPRequest(receiver);
			request.getTimeout().cancel();
			removeRequest(receiver);
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, request.getTransmitter(),
					"Le joueur §b%s §ra §crefusé §rvotre requête.", receiver.getName());
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, receiver, "Vous avez refusé la requête du joueur §b%s§r.",
					request.getTransmitter().getName());
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, receiver, NO_REQUEST);
		}
	}
}
