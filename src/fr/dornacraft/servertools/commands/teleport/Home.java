package fr.dornacraft.servertools.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Classe de gestion d'une résidence
 * 
 * @author Voltariuss
 * @version 1.5.3
 *
 */
public class Home {

	private String name;
	private Location location;

	/**
	 * Constructeur
	 * 
	 * @param name      Le nom de la résidence, non null
	 * @param worldName Le nom du monde dans lequel se trouve la résidence, non null
	 * @param x         La localisation de la résidence sur x
	 * @param y         La localisation de la résidence sur y
	 * @param z         La localisation de la résidence sur z
	 * @param yaw       L'orientation verticale
	 * @param pitch     L'orientation horizontale
	 */
	public Home(String name, String worldName, double x, double y, double z, float yaw, float pitch) {
		setName(name);
		setLocation(new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch));
	}

	/**
	 * @return Le nom de la résidence, non null
	 */
	public String getName() {
		return name;
	}

	/**
	 * Définit le nom de la résidence.
	 * 
	 * @param name Le nom de la résidence, non null
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return La localisation de la résidence, non null
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Définit la localisation de la résidence.
	 * 
	 * @param location La localisation de la résidence, non null
	 */
	private void setLocation(Location location) {
		this.location = location;
	}
}
