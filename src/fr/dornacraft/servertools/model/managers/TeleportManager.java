package fr.dornacraft.servertools.model.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpa;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpaccept;
import fr.dornacraft.servertools.controller.commands.teleport.CmdTpdeny;
import fr.dornacraft.servertools.controller.listeners.PlayerMoveListener;
import fr.dornacraft.servertools.model.utils.TPRequest;
import fr.dornacraft.servertools.model.utils.TypeRequest;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.dornacraft.servertools.utils.Utils;
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

	public static final String NO_REQUEST = ServerToolsConfig.getCommandMessage(CmdTpa.CMD_LABEL, "failure_no_request");

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
			HashMap<String, String> values = new HashMap<>();
			values.put("Delay", Integer
					.toString(JavaPlugin.getPlugin(ServerTools.class).getConfig().getInt("delay_teleportation")));
			String messageTeleportDelay = ServerToolsConfig.getString("messages.info_teleport_begin", values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, messageTeleportDelay);

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
									String messageTeleportCancel = ServerToolsConfig
											.getString("messages.failure_teleport_cancel");
									UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, player, messageTeleportCancel);

									if (isSeparatedSender) {
										HashMap<String, String> valuesCancel = new HashMap<>();
										valuesCancel.put("Target", player.getName());
										String messageTeleportPlayerCancel = ServerToolsConfig
												.getString("messages.failure_teleport_player_cancel", valuesCancel);
										UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender,
												messageTeleportPlayerCancel);
									}
									return;
								}
							}
							player.teleport(location);
							if (isSeparatedSender) {
								HashMap<String, String> valuesSuccess = new HashMap<>();
								valuesSuccess.put("Target", player.getName());
								String messageSuccessTeleportPlayer = ServerToolsConfig
										.getString("messages.success_teleport_player", valuesSuccess);
								UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, sender, messageSuccessTeleportPlayer);
							}
						}
					}, 100));
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, teleportSuccessMessage);
			player.teleport(location);

			if (isSeparatedSender) {
				HashMap<String, String> valuesSuccess = new HashMap<>();
				valuesSuccess.put("Target", player.getName());
				String messageSuccessTeleportPlayer = ServerToolsConfig.getString("messages.success_teleport_player",
						valuesSuccess);
				UtilsAPI.sendSystemMessage(MessageLevel.SUCCESS, sender, messageSuccessTeleportPlayer);
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
		HashMap<String, String> values = new HashMap<>();
		values.put("Target", receiver.getName());
		String messageRequestPlayerId = null;

		if (typeRequest != TypeRequest.ALL_TELEPORT_TO_TRANSMITTER) {
			messageRequestPlayerId = "info_request_send_to_player";
		} else {
			messageRequestPlayerId = "info_request_send_to_global";
		}
		String messageRequestPlayer = ServerToolsConfig.getCommandMessage(CmdTpa.CMD_LABEL, messageRequestPlayerId,
				values);
		UtilsAPI.sendSystemMessage(MessageLevel.INFO, transmitter, messageRequestPlayer);

		String requestHeader = Utils
				.getHeader(ServerToolsConfig.getCommandMessage(CmdTpa.CMD_LABEL, "normal_request_header"));
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, receiver, requestHeader);
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, receiver,
				Utils.getNewLine(
						ServerToolsConfig.getCommandMessage(CmdTpa.CMD_LABEL, "normal_request_transmitter_prefix"),
						transmitter.getName()));
		String messageDescriptionId = null;

		if (typeRequest == TypeRequest.TRANSMITTER_TELEPORT_TO_RECEIVER) {
			messageDescriptionId = "normal_request_transmitter_to_receiver_description";
		} else {
			messageDescriptionId = "normal_request_receiver_to_transmitter_description";
		}
		String messageDescription = ServerToolsConfig.getCommandMessage(CmdTpa.CMD_LABEL, messageDescriptionId);
		String descriptionPrefix = ServerToolsConfig.getCommandMessage(CmdTpa.CMD_LABEL,
				"normal_request_descripion_prefix");
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, receiver,
				Utils.getNewLine(descriptionPrefix, messageDescription));
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, receiver,
				ServerToolsConfig.getCommandMessage(CmdTpa.CMD_LABEL, "normal_request_description"));
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

			HashMap<String, String> values = new HashMap<>();
			values.put("Receiver", receiver.getName());
			values.put("Transmitter", transmitter.getName());
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, transmitter, ServerToolsConfig
					.getCommandMessage(CmdTpaccept.CMD_LABEL, "info_request_accepted_by_receiver", values));
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, receiver,
					ServerToolsConfig.getCommandMessage(CmdTpaccept.CMD_LABEL, "info_request_accepted", values));
			request.getLocation().setPitch(0);
			String messageTeleportStart = ServerToolsConfig.getString("messages.info_teleport_start");

			if (request.getTypeRequest() == TypeRequest.TRANSMITTER_TELEPORT_TO_RECEIVER) {
				teleportPlayerTo(null, transmitter, request.getLocation(), messageTeleportStart);
			} else {
				teleportPlayerTo(null, receiver, request.getLocation(), messageTeleportStart);
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

			HashMap<String, String> values = new HashMap<>();
			values.put("Receiver", receiver.getName());
			values.put("Transmitter", request.getTransmitter().getName());
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, request.getTransmitter(), ServerToolsConfig
					.getCommandMessage(CmdTpdeny.CMD_LABEL, "info_request_refused_by_receiver", values));
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, receiver,
					ServerToolsConfig.getCommandMessage(CmdTpdeny.CMD_LABEL, "info_request_refused", values));
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, receiver, NO_REQUEST);
		}
	}
}
