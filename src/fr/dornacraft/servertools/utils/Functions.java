package fr.dornacraft.servertools.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.sql.SQLConnection;

public class Functions {

	public static final String HOME_DATABASE = "F1_Essentials_Home";
	public static final String HOME_DATABASE_FIELDS = "(name, x, y, z, uuid)";
	public static final int HOME_MAX = 5;

	public static boolean canSetHome(Player p) {
		try {
			PreparedStatement query = SQLConnection.getInstance().getConnection().prepareStatement(
					"SELECT count(*) FROM " + HOME_DATABASE + " WHERE uuid = '" + p.getUniqueId() + "';");
			ResultSet result = query.executeQuery();
			if (result.next()) {
				if (result.getInt(1) < HOME_MAX) {
					query.close();
					return true;
				} else {
					query.close();
					return false;
				}
			}
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void delHome(Player p, String home) {
		try {
			PreparedStatement query = SQLConnection.getInstance().getConnection().prepareStatement("DELETE FROM "
					+ HOME_DATABASE + " WHERE name = '" + home + "' and uuid = '" + p.getUniqueId() + "';");
			query.execute();
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Home getHome(Player p, String home) {
		Home theHome = new Home();
		try {
			PreparedStatement query = SQLConnection.getInstance().getConnection().prepareStatement("SELECT * FROM "
					+ HOME_DATABASE + " WHERE name = '" + home + "' and uuid = '" + p.getUniqueId() + "';");
			ResultSet result = query.executeQuery();
			if (result.next()) {
				theHome = new Home(result.getString(2), result.getDouble(3), result.getDouble(4), result.getDouble(5),
						result.getString(6));
				query.close();
				return theHome;
			}
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return theHome;
	}

	public static ArrayList<Home> getHomes(Player p) {
		ArrayList<Home> theHomes = new ArrayList<Home>();
		try {
			PreparedStatement query = SQLConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM " + HOME_DATABASE + " WHERE uuid = '" + p.getUniqueId() + "';");
			ResultSet result = query.executeQuery();
			while (result.next()) {
				theHomes.add(new Home(result.getString(2), result.getDouble(3), result.getDouble(4),
						result.getDouble(5), result.getString(6)));
			}
			query.close();
			return theHomes;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return theHomes;
	}

	public static boolean isAHome(Player p, String home) {
		try {
			PreparedStatement query = SQLConnection.getInstance().getConnection()
					.prepareStatement("SELECT count(*) FROM " + HOME_DATABASE + " WHERE name = '" + home
							+ "' and uuid = '" + p.getUniqueId() + "';");
			ResultSet result = query.executeQuery();
			if (result.next()) {
				if (result.getInt(1) > 0) {
					query.close();
					return true;
				} else {
					query.close();
					return false;
				}
			}
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void setHome(Player p, String home) {
		Location pLoc = p.getLocation();
		try {
			PreparedStatement query = SQLConnection.getInstance().getConnection()
					.prepareStatement("INSERT INTO " + HOME_DATABASE + " " + HOME_DATABASE_FIELDS + " VALUES ('" + home
							+ "', " + pLoc.getX() + ", " + pLoc.getY() + ", " + pLoc.getZ() + ", '" + p.getUniqueId()
							+ "');");
			query.execute();
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateHome(Player p, String home) {
		Location pLoc = p.getLocation();
		try {
			PreparedStatement query = SQLConnection.getInstance().getConnection()
					.prepareStatement("UPDATE " + HOME_DATABASE + " SET x = " + pLoc.getX() + ", y = " + pLoc.getY()
							+ ", z = " + pLoc.getZ() + " WHERE name = '" + home + "' and uuid = '" + p.getUniqueId()
							+ "';");
			query.execute();
			query.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
