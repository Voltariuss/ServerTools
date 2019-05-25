package fr.dornacraft.servertools.commands.info;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.sql.SQLConnection;

public class SQLInfo {

	public static final String TABLE_NAME = "G_Players";

	static boolean isCreated(Player player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?");
		query.setString(1, player.getUniqueId().toString());

		ResultSet result = query.executeQuery();
		boolean isCreated = result.next();
		query.close();
		return isCreated;
	}

	static void insertTuplet(Player player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?)");
		query.setString(1, player.getUniqueId().toString());
		query.setString(2, player.getName());
		query.setString(3, player.getAddress().getAddress().getHostAddress());
		query.execute();
		query.close();
	}

	static void updateIp(Player player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("UPDATE " + TABLE_NAME + " SET ip = ? WHERE uuid = ?");
		query.setString(1, player.getAddress().getAddress().getHostAddress());
		query.setString(2, player.getUniqueId().toString());
		query.executeUpdate();
		query.close();
	}

	static UUID getUUIDFromPseudo(String pseudo) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT uuid FROM " + TABLE_NAME + " WHERE pseudo = ?");
		query.setString(1, pseudo);

		ResultSet result = query.executeQuery();
		UUID uuid = null;

		if (result.next()) {
			uuid = UUID.fromString(result.getString("uuid"));
		}
		query.close();
		return uuid;
	}

	static String getIp(OfflinePlayer target) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT ip FROM " + TABLE_NAME + " WHERE pseudo = ?");
		query.setString(1, target.getName());

		ResultSet result = query.executeQuery();
		String ip = null;

		while (result.next()) {
			ip = result.getString("ip");
		}
		query.close();
		return ip;
	}

	static ArrayList<String> getPlayers(String ip) throws SQLException {
		ArrayList<String> players = new ArrayList<>();
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT pseudo FROM " + TABLE_NAME + " WHERE ip = ?");
		query.setString(1, ip);

		ResultSet result = query.executeQuery();

		while (result.next()) {
			players.add(result.getString("pseudo"));
		}
		query.close();
		return players;
	}
}
