package fr.dornacraft.servertools.model.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.controller.commands.info.CmdSeen;
import fr.dornacraft.servertools.model.database.SQLPlayer;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.dornacraft.servertools.utils.Utils;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class PlayerManager {

	public static void checkPlayerPersistence(Player player) throws SQLException {
		if (SQLPlayer.containsPlayer(player)) {
			SQLPlayer.updateHostAddress(player);
		} else {
			SQLPlayer.persistPlayer(player);
			player.teleport(GlobalManager.getSpawnLocation());
			player.setBedSpawnLocation(GlobalManager.getSpawnLocation());
		}
	}

	public static OfflinePlayer getOfflinePlayerFromName(String name) throws SQLException {
		Player player = Bukkit.getPlayer(name);

		if (player != null) {
			return player;
		} else if (!name.contains(".")) {
			UUID uuid = SQLPlayer.getUUIDFromName(name);
			return uuid != null ? Bukkit.getOfflinePlayer(uuid) : null;
		} else {
			return null;
		}
	}

	public static String getHostAddress(OfflinePlayer target) throws SQLException {
		if (target.isOnline()) {
			return ((Player) target).getAddress().getAddress().getHostAddress();
		} else {
			return SQLPlayer.getHostAddress(target);
		}
	}

	public static List<String> getPlayersName(String hostAddress) throws SQLException {
		return SQLPlayer.getPlayersName(hostAddress);
	}

	public static String getCountry(InetSocketAddress address) throws IOException {
		String unknowTag = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "normal_unknow_tag");

		if (address == null) {
			return unknowTag;
		}
		URL url = new URL("http://ip-api.com/json/" + address.getHostName());
		BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuilder entirePage = new StringBuilder();
		String inputLine;

		while ((inputLine = stream.readLine()) != null) {
			entirePage.append(inputLine);
		}
		stream.close();

		if (!(entirePage.toString().contains("\"country\":\""))) {
			return unknowTag;
		} else {
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		}
	}

	public static void getInfoPlayer(CommandSender sender, OfflinePlayer player) throws SQLException, IOException {
		String hostAdress = getHostAddress(player);
		String name = player.getName();
		String country = null;
		String lastConnectionTime = Utils.convertTime(System.currentTimeMillis() - player.getLastPlayed());
		List<String> playersName = getPlayersName(hostAdress);

		if (player.isOnline()) {
			country = getCountry(player.getPlayer().getAddress());
		}
		sender.sendMessage(
				Utils.getHeader(ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_header_info_ip")));
		sender.sendMessage(
				Utils.getNewLine(ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_pseudo"), name));
		sender.sendMessage(Utils
				.getNewLine(ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_last_ip_know"), hostAdress));
		sender.sendMessage(
				Utils.getNewLine(ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_location"), country));

		HashMap<String, String> values = new HashMap<>();
		values.put("Last_Connection_Time", lastConnectionTime);
		sender.sendMessage(
				Utils.getNewLine(ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_last_connection"),
						player.isOnline() ? ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_connected")
								: ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_last_connection_time",
										values)));
		sender.sendMessage(ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_list_players_with_same_ip"));

		for (String playerName : playersName) {
			sender.sendMessage(" §e- §b" + playerName);
		}
	}

	public static void getInfoHostAddress(CommandSender sender, String hostAdress) throws SQLException {
		List<String> players = getPlayersName(hostAdress);

		if (!players.isEmpty()) {
			sender.sendMessage(
					Utils.getHeader(ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_header_info_ip")));
			sender.sendMessage(Utils.getNewLine("IP", hostAdress));
			sender.sendMessage(
					ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_list_players_with_same_ip"));

			for (String player : players) {
				sender.sendMessage(" §e- §b" + player);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender,
					ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "error_invalid_input_or_unkown_target"));
		}
	}

	public static String getPing(Player player) {
		int ping = ((CraftPlayer) player).getHandle().ping;
		ChatColor pingColor;

		if (ping < 40) {
			pingColor = ChatColor.DARK_GREEN;
		} else if (ping < 60) {
			pingColor = ChatColor.GREEN;
		} else if (ping < 100) {
			pingColor = ChatColor.YELLOW;
		} else if (ping < 200) {
			pingColor = ChatColor.RED;
		} else {
			pingColor = ChatColor.DARK_RED;
		}
		return pingColor.toString() + ping;
	}
}
