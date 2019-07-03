package fr.dornacraft.servertools.model.database.queries;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.bukkit.entity.Player;

import fr.dornacraft.servertools.model.database.managers.SQLManager;
import fr.dornacraft.servertools.model.database.tables.TPlayer;

public class PlayerSQL {

    public static final String TABLE_NAME = "Player";

    public static boolean isCreated(Player player) {
        EntityManagerFactory emf = SQLManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        System.out.println("CHECCCKKKKKKKKKKK");

        try {
            em.getReference(TPlayer.class, player.getUniqueId().toString());
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void persistPlayer(Player player) {
        EntityManagerFactory emf = SQLManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        TPlayer tPlayer = new TPlayer();
        tPlayer.setUuid(player.getUniqueId().toString());
        tPlayer.setName(player.getName());
        tPlayer.setHostAdress(player.getAddress().getAddress().getHostAddress());
        tPlayer.setLastLogin(new Date());
        em.persist(tPlayer);
        transaction.commit();
        em.close();
        emf.close();
    }
    
    public static String getUUIDFromName(String name) {
        EntityManagerFactory emf = SQLManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT uuid FROM " + TABLE_NAME + " WHERE name = :name");
        query.setParameter("name", name);
        String uuid = (String) query.getSingleResult();
        em.close();
        emf.close();
        return uuid;
    }

    public static String getHostAdress(String uuid) throws EntityNotFoundException {
        EntityManagerFactory emf = SQLManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        try {
            TPlayer tPlayer = em.getReference(TPlayer.class, uuid);
            return tPlayer.getHostAdress();
        } catch (EntityNotFoundException e) {
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
    
    public static List<String> getPlayersName(String hostAdress) {
        EntityManagerFactory emf = SQLManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        TypedQuery<String> query = em.createQuery("SELECT name FROM " + TABLE_NAME + " WHERE hostAdress = :hostAdress", String.class);
        query.setParameter("hostAdress", hostAdress);
        List<String> playersName = query.getResultList();
        em.close();
        emf.close();
        return playersName;
    }

    public static void updateHostAdress(Player player) {
        EntityManagerFactory emf = SQLManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        
        try {
            TPlayer tPlayer = em.getReference(TPlayer.class, player.getUniqueId().toString());
            tPlayer.setHostAdress(player.getAddress().getAddress().getHostAddress());
            em.flush();
            transaction.commit();
        } catch (EntityNotFoundException e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void deletePlayer(String uuid) {
        EntityManagerFactory emf = SQLManager.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        
        try {
            TPlayer tPlayer = em.getReference(TPlayer.class, uuid);
            em.remove(tPlayer);
            transaction.commit();
        } catch (EntityNotFoundException e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        } 
    }
}