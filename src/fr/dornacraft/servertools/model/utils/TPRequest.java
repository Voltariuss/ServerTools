package fr.dornacraft.servertools.model.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.TeleportManager;

/**
 * Classe de gestion d'une requête de téléportation.
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class TPRequest {

	private Player transmitter;
	private TypeRequest typeRequest;
	private BukkitTask timeout;
	private Location location;

	/**
	 * Constructeur
	 * 
	 * @param transmitter Le transmetteur de la requête, non null
	 * @param receiver    Le récepteur de la requête, non null
	 * @param typeRequest Le type de requête, non null
	 */
	public TPRequest(Player transmitter, Player receiver, TypeRequest typeRequest) {
		setTransmitter(transmitter);
		setTypeRequest(typeRequest);
		setLocation(typeRequest == TypeRequest.TRANSMITTER_TELEPORT_TO_RECEIVER ? receiver.getLocation()
				: transmitter.getLocation());

		// Lancement du timeout de la requête
		setTimeout(Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(ServerTools.class), new Runnable() {

			@Override
			public void run() {
				TeleportManager.removeRequest(receiver);
			}
		}, 1200)); // 60 secondes
	}

	/**
	 * @return Le transmetteur de la requête, non null
	 */
	public Player getTransmitter() {
		return transmitter;
	}

	/**
	 * Définit le transmetteur de la requête.
	 * 
	 * @param transmitter Le transmetteur de la requête, non null
	 */
	private void setTransmitter(Player transmitter) {
		this.transmitter = transmitter;
	}

	/**
	 * @return Le type de la requête, non null
	 */
	public TypeRequest getTypeRequest() {
		return typeRequest;
	}

	/**
	 * Définit le type de la requête.
	 * 
	 * @param typeRequest Le type de la requête, non null
	 */
	private void setTypeRequest(TypeRequest typeRequest) {
		this.typeRequest = typeRequest;
	}

	/**
	 * @return Le timeout de la requête, non null
	 */
	public BukkitTask getTimeout() {
		return timeout;
	}

	/**
	 * Définit le timeout de la requête.
	 * 
	 * @param task Le timeout de la requête, non null
	 */
	private void setTimeout(BukkitTask timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return La localisation du lieu de téléportation, non null
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Définit la localisation du lieu de téléportation.
	 * 
	 * @param location La localisation du lieu de téléportation, non null
	 */
	private void setLocation(Location location) {
		this.location = location;
	}
}
