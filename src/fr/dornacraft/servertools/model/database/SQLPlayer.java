package fr.dornacraft.servertools.model.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.ServerTools;
import fr.voltariuss.simpledevapi.sql.SQLConnection;

public class SQLPlayer {

	public static final String TABLE_NAME = ServerTools.class.getSimpleName() + "_Players";

	public static void createTableIfNotExists() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
		+ "("
		+ " 	uuid VARCHAR(36) PRIMARY KEY NOT NULL,"
		+ " 	name VARCHAR(50) NOT NULL,"
		+ " 	host_address VARCHAR(255)"
		+ ")";
		Statement statement = SQLConnection.getInstance().getConnection().createStatement();
		statement.executeUpdate(query);
		statement.close();
	}

	public static boolean containsPlayer(Player player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?");
		query.setString(1, player.getUniqueId().toString());

		ResultSet result = query.executeQuery();
		boolean isCreated = result.next();
		query.close();
		return isCreated;
	}

	public static void persistPlayer(Player player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?)");
		query.setString(1, player.getUniqueId().toString());
		query.setString(2, player.getName());
		query.setString(3, player.getAddress().getAddress().getHostAddress());
		query.execute();
		query.close();
	}

	public static void updateHostAddress(Player player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("UPDATE " + TABLE_NAME + " SET hostAddress = ? WHERE uuid = ?");
		query.setString(1, player.getAddress().getAddress().getHostAddress());
		query.setString(2, player.getUniqueId().toString());
		query.executeUpdate();
		query.close();
	}

	public static UUID getUUIDFromName(String name) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT uuid FROM " + TABLE_NAME + " WHERE name = ?");
		query.setString(1, name);

		ResultSet result = query.executeQuery();
		UUID uuid = null;

		if (result.next()) {
			uuid = UUID.fromString(result.getString("uuid"));
		}
		query.close();
		return uuid;
	}

	public static String getHostAddress(OfflinePlayer player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT hostAddress FROM " + TABLE_NAME + " WHERE name = ?");
		query.setString(1, player.getName());

		ResultSet result = query.executeQuery();
		String hostAddress = null;

		while (result.next()) {
			hostAddress = result.getString("hostAddress");
		}
		query.close();
		return hostAddress;
	}

	public static List<String> getPlayersName(String hostAddress) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT name FROM " + TABLE_NAME + " WHERE hostAddress = ?");
		query.setString(1, hostAddress);

		ResultSet result = query.executeQuery();
		List<String> players = new ArrayList<>();

		while (result.next()) {
			players.add(result.getString("name"));
		}
		query.close();
		return players;
	}
}
