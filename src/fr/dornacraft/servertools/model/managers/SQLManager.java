package fr.dornacraft.servertools.model.managers;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import fr.dornacraft.servertools.model.database.SQLPlayer;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.sql.SQLConnection;

public class SQLManager {

    private static boolean isDatabaseAvailable;

    public static void checkDatabase() {
        try {
            if (isDatabaseConnectionValid()) {
                setDatabaseAvailable(true);
                createTablesIfNotExists();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            setDatabaseAvailable(false);
            Bukkit.getLogger().log(Level.WARNING, UtilsAPI.SQL_NO_CONNECTION);
            e.printStackTrace();
        }
    }

    public static boolean isDatabaseConnectionValid() {
        return SQLConnection.getInstance().getConnection() != null;
    }

    public static void createTablesIfNotExists() throws SQLException {
        SQLPlayer.createTableIfNotExists();
    }

    public static boolean isDatabaseAvailable() {
        return isDatabaseAvailable;
    }

    public static void setDatabaseAvailable(boolean isDatabaseAvailable) {
        SQLManager.isDatabaseAvailable = isDatabaseAvailable;
    }
}