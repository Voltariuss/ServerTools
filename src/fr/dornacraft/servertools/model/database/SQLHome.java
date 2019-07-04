package fr.dornacraft.servertools.model.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.utils.Home;
import fr.voltariuss.simpledevapi.sql.SQLConnection;

/**
 * Classe contenant les requêtes SQL relatives à la gestion des résidences d'un
 * joueur
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class SQLHome {

	public static final String TABLE_NAME_HOME = "F1_Essentials_Homes";

	/**
	 * Récupère et retourne la liste des résidences du joueur.
	 * 
	 * @param player Le joueur ciblé, non null
	 * @return La liste des résidences du joueur, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static ArrayList<Home> selectHomes(Player player) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT * FROM " + TABLE_NAME_HOME + " WHERE uuid = ?");
		query.setString(1, player.getUniqueId().toString());

		ResultSet result = query.executeQuery();
		final ArrayList<Home> homes = new ArrayList<>();

		while (result.next()) {
			homes.add(new Home(result.getString("name"), result.getString("world_name"), result.getDouble("x"),
					result.getDouble("y"), result.getDouble("z"), result.getFloat("yaw"), result.getFloat("pitch")));
		}
		query.close();
		return homes;
	}

	/**
	 * Récupère et retourne la résidence correspondante au nom spécifié pour le
	 * joueur en question.
	 * 
	 * @param player   Le joueur ciblé, non null
	 * @param homeName Le nom de la résidence du joueur, non null
	 * @return La résidence du joueur correspondant au nom spécifié, peut être null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static Home selectHome(Player player, String homeName) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("SELECT * FROM " + TABLE_NAME_HOME + " WHERE uuid = ? AND name = ?");
		query.setString(1, player.getUniqueId().toString());
		query.setString(2, homeName);

		ResultSet result = query.executeQuery();
		Home home = null;

		if (result.next()) {
			home = new Home(result.getString("name"), result.getString("world_name"), result.getDouble("x"),
					result.getDouble("y"), result.getDouble("z"), result.getFloat("yaw"), result.getFloat("pitch"));
		}
		query.close();
		return home;
	}

	/**
	 * Insère une nouvelle résidence à partir de la localisation du joueur.
	 * 
	 * @param player   Le joueur ciblé, non null
	 * @param homeName Le nom de la résidence à créer, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static void insertHome(Player player, String homeName) throws SQLException {
		Location location = player.getLocation();
		PreparedStatement query = SQLConnection.getInstance().getConnection().prepareStatement("INSERT INTO "
				+ TABLE_NAME_HOME + "(uuid, name, world_name, x, y, z, yaw, pitch) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		query.setString(1, player.getUniqueId().toString());
		query.setString(2, homeName);
		query.setString(3, location.getWorld().getName());
		query.setDouble(4, location.getX());
		query.setDouble(5, location.getY());
		query.setDouble(6, location.getZ());
		query.setFloat(7, location.getYaw());
		query.setFloat(8, location.getPitch());
		query.execute();
		query.close();
	}

	/**
	 * Met à jour la localisation de la résidence du joueur.
	 * 
	 * @param player   Le joueur ciblé, non null
	 * @param homeName Le nom de la résidence, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static void updateHome(Player player, String homeName) throws SQLException {
		Location location = player.getLocation();
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("UPDATE " + TABLE_NAME_HOME
						+ " SET world_name = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE uuid = ? AND name = ?");
		query.setString(1, location.getWorld().getName());
		query.setDouble(2, location.getX());
		query.setDouble(3, location.getY());
		query.setDouble(4, location.getZ());
		query.setFloat(5, location.getYaw());
		query.setFloat(6, location.getPitch());
		query.setString(7, player.getUniqueId().toString());
		query.setString(8, homeName);
		query.execute();
		query.close();
	}

	/**
	 * Supprime la résidence du joueur correspondante.
	 * 
	 * @param player   Le joueur ciblé, non null
	 * @param homeName Le nom de la résidence à supprimer, non null
	 * @throws SQLException Si une erreur avec la base de données est détectée
	 */
	public static void deleteHome(Player player, String homeName) throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection()
				.prepareStatement("DELETE FROM " + TABLE_NAME_HOME + " WHERE uuid = ? AND name = ?");
		query.setString(1, player.getUniqueId().toString());
		query.setString(2, homeName);
		query.execute();
		query.close();
	}
}
