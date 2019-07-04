package fr.dornacraft.servertools.model.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.controller.commands.info.CmdSeen;
import fr.dornacraft.servertools.model.database.SQLPlayer;
import fr.dornacraft.servertools.utils.ServerToolsConfig;

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
