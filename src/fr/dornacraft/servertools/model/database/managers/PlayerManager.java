package fr.dornacraft.servertools.model.database.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.controller.commands.info.CmdSeen;
import fr.dornacraft.servertools.model.database.queries.PlayerSQL;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.dornacraft.servertools.utils.Utils;

public class PlayerManager {

    public static void checkDb(Player player) {
		if (PlayerSQL.isCreated(player)) {
			PlayerSQL.updateHostAdress(player);
		} else {
			PlayerSQL.persistPlayer(player);
			player.teleport(Utils.SPAWN_LOCATION);
			player.setBedSpawnLocation(Utils.SPAWN_LOCATION);
		}
	}

    public static OfflinePlayer getOfflinePlayerFromName(String name) {
		Player player = Bukkit.getPlayer(name);

		if (player != null) {
			return player;
		} else if (!name.contains(".")) {
            UUID uuid = UUID.fromString(PlayerSQL.getUUIDFromName(name));
			return uuid != null ? Bukkit.getOfflinePlayer(uuid) : null;
		} else {
			return null;
		}
	}

    public static String getHostAdress(OfflinePlayer player) {
        if (player.isOnline()) {
            return ((Player) player).getAddress().getAddress().getHostAddress();
        } else {
            return PlayerSQL.getHostAdress(player.getUniqueId().toString());
        }
    }

    public static List<String> getPlayersName(String hostAdress) {
        return PlayerSQL.getPlayersName(hostAdress);
    }

    public static String getCountry(InetSocketAddress adress) throws IOException {
		String unknowTag = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "normal_unknow_tag");
		
		if (adress == null) {
			return unknowTag;
		}
		URL url = new URL("http://ip-api.com/json/" + adress.getHostName());
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
}