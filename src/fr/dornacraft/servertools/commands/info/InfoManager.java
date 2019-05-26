package fr.dornacraft.servertools.commands.info;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.utils.Utils;

public class InfoManager {

	public static void checkDb(Player player) throws SQLException {
		if (SQLInfo.isCreated(player)) {
			SQLInfo.updateIp(player);
		} else {
			SQLInfo.insertTuplet(player);
			player.teleport(Utils.SPAWN_LOCATION);
			player.setBedSpawnLocation(Utils.SPAWN_LOCATION);
		}
	}

	public static OfflinePlayer getOfflinePlayerFromPseudo(String pseudo) throws SQLException {
		Player player = Bukkit.getPlayer(pseudo);

		if (player != null) {
			return player;
		} else if (!pseudo.contains(".")) {
			UUID uuid = SQLInfo.getUUIDFromPseudo(pseudo);
			return uuid != null ? Bukkit.getOfflinePlayer(uuid) : null;
		} else {
			return null;
		}
	}

	public static String getIp(OfflinePlayer target) throws SQLException {
		if (target.isOnline()) {
			return ((Player) target).getAddress().getAddress().getHostAddress();
		} else {
			return SQLInfo.getIp(target);
		}
	}

	public static ArrayList<String> getPlayers(String ip) throws SQLException {
		return SQLInfo.getPlayers(ip);
	}

	public static String getCountry(InetSocketAddress ip) throws Exception {
		if (ip == null) {
			return "Inconnue";
		}
		URL url = new URL("http://ip-api.com/json/" + ip.getHostName());
		BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuilder entirePage = new StringBuilder();
		String inputLine;

		while ((inputLine = stream.readLine()) != null) {
			entirePage.append(inputLine);
		}
		stream.close();

		if (!(entirePage.toString().contains("\"country\":\""))) {
			return "Inconnue";
		} else {
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		}
	}
}
