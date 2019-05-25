package fr.dornacraft.servertools.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class KillAllManager {

	public static void killEntities(Player player, String type) {
		int nbEntities = 0;
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (!(entity.getType().equals(EntityType.PLAYER))) {
					switch (type.toLowerCase()) {
					case "all":
						entity.remove();
						nbEntities++;
						break;
					case "monsters":
						if (entity instanceof Monster) {
							entity.remove();
							nbEntities++;
						}
						break;
					case "animals":
						if (entity instanceof Animals) {
							entity.remove();
							nbEntities++;
						}
						break;
					case "ambient":
						if (entity instanceof Ambient) {
							entity.remove();
							nbEntities++;
						}
						break;
					case "drops":
						if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
							entity.remove();
							nbEntities++;
						}
						break;
					case "xp":
						if (entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
							entity.remove();
							nbEntities++;
						}
						break;
					case "arrows":
						if (entity.getType().equals(EntityType.ARROW)) {
							entity.remove();
							nbEntities++;
						}
						break;
					default:
						if (entity.getType().toString().toLowerCase().equalsIgnoreCase(type.toLowerCase())) {
							entity.remove();
							nbEntities++;
						}
						break;
					}
				}
			}
		}
		if (type.toLowerCase().equalsIgnoreCase("all")) {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
					"Vous avez supprimé " + nbEntities + " entitées entre tous les mondes");
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
					"Vous avez supprimé " + nbEntities + " " + type + " entre tous les mondes");
		}
	}

	public static void killEntities(Player player, String type, String radiusAndWorld) {
		String radiusString = radiusAndWorld.split(":")[0];
		String worldString = radiusAndWorld.split(":")[1];
		double radius = 0;
		World world = null;
		boolean paramsCorrect = false;

		try {
			radius = Double.parseDouble(radiusString);
			if (radius < 0) {
				paramsCorrect = false;
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						"Usage : §6/killall [all|monsters|animals|ambient|drops|xp|arrows|mobs:[MobType]] [radius=100|none:world=name|current]");
			} else {
				paramsCorrect = true;
			}
		} catch (NumberFormatException e) {
			if (radiusString.toLowerCase().equalsIgnoreCase("none")) {
				radius = -999;
				paramsCorrect = true;
			} else {
				paramsCorrect = false;
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						"Usage : §6/killall [all|monsters|animals|ambient|drops|xp|arrows|mobs:[MobType]] [radius=100|none:world=name|current]");
			}
		}

		if (paramsCorrect) {
			try {
				if (worldString.toLowerCase().equalsIgnoreCase("current")) {
					world = player.getWorld();
					paramsCorrect = true;
				} else {
					world = Bukkit.getWorld(worldString);
					world.getEntities();
				}
			} catch (Exception e) {
				paramsCorrect = false;
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						"Usage : §6/killall [all|monsters|animals|ambient|drops|xp|arrows|mobs:[MobType]] [radius=100|none:world=name|current]");
			}
		}

		if (paramsCorrect) {
			if (radius != -999 && (player.getWorld() != world)) {
				paramsCorrect = false;
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						"Usage : §6/killall [all|monsters|animals|ambient|drops|xp|arrows|mobs:[MobType]] [radius=100|none:world=name|current]");
			}
		}

		if (paramsCorrect) {
			int nbEntities = 0;
			if (radius == -999) {
				for (Entity entity : world.getEntities()) {
					if (!(entity.getType().equals(EntityType.PLAYER))) {
						switch (type.toLowerCase()) {
						case "all":
							entity.remove();
							nbEntities++;
							break;
						case "monsters":
							if (entity instanceof Monster) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "animals":
							if (entity instanceof Animals) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "ambient":
							if (entity instanceof Ambient) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "drops":
							if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "xp":
							if (entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "arrows":
							if (entity.getType().equals(EntityType.ARROW)) {
								entity.remove();
								nbEntities++;
							}
							break;
						default:
							if (entity.getType().toString().toLowerCase().equalsIgnoreCase(type.toLowerCase())) {
								entity.remove();
								nbEntities++;
							}
							break;
						}
					}
				}
				if (type.toLowerCase().equalsIgnoreCase("all")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
							"Vous avez supprimé " + nbEntities + " entitées dans le monde " + world.getName());
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
							"Vous avez supprimé " + nbEntities + " " + type + " dans le monde " + world.getName());
				}
			} else {
				for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
					if (!(entity.getType().equals(EntityType.PLAYER))) {
						switch (type.toLowerCase()) {
						case "all":
							entity.remove();
							nbEntities++;
							break;
						case "monsters":
							if (entity instanceof Monster) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "animals":
							if (entity instanceof Animals) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "ambient":
							if (entity instanceof Ambient) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "drops":
							if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "xp":
							if (entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
								entity.remove();
								nbEntities++;
							}
							break;
						case "arrows":
							if (entity.getType().equals(EntityType.ARROW)) {
								entity.remove();
								nbEntities++;
							}
							break;
						default:
							if (entity.getType().toString().toLowerCase().equalsIgnoreCase(type.toLowerCase())) {
								entity.remove();
								nbEntities++;
							}
							break;
						}
					}
				}
				if (type.toLowerCase().equalsIgnoreCase("all")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "Vous avez supprimé " + nbEntities
							+ " entitées dans le monde " + world.getName() + " dans un rayon de " + radius);
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "Vous avez supprimé " + nbEntities + " "
							+ type + " dans le monde " + world.getName() + " dans un rayon de " + radius);
				}
			}
		}
	}

}
