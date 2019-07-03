package fr.dornacraft.servertools.model.database.managers;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fr.voltariuss.simpledevapi.sql.SQLConnection;

public class SQLManager {

    public static EntityManagerFactory getEntityManagerFactory() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        properties.put("javax.persistence.jdbc.url", SQLConnection.getInstance().getJdbcTypeRoot()
                + SQLConnection.getInstance().getHost() + "/" + SQLConnection.getInstance().getDatabase());
        properties.put("javax.persistence.jdbc.user", SQLConnection.getInstance().getUser());
        properties.put("javax.persistence.jdbc.password", SQLConnection.getInstance().getPassword());
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default", properties);
        return entityManagerFactory;
    }
}