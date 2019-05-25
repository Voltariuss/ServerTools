package fr.dornacraft.servertools.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import fr.dornacraft.servertools.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class KillAllManager {

	public static final String ENTITY_OPTIONS = "[all|monsters|animals|ambient|drops|xp|arrows|mobs:[MobType]]";
	public static final String SCOPE_OPTIONS = "[radius=100|none:world=name|current]";
	public static final String USAGE = String.join(" ", "Usage : §6/killall", ENTITY_OPTIONS, SCOPE_OPTIONS);

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
		HashMap<String, String> values = new HashMap<>();
		values.put("Number_Entities", Integer.toString(nbEntities));

		if (type.toLowerCase().equalsIgnoreCase("all")) {
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
					ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "info_kill_all_on_server", values));
		} else {
			values.put("Entities_Type", type);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
					ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "info_kill_entities_on_server", values));
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
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, USAGE);
			} else {
				paramsCorrect = true;
			}
		} catch (NumberFormatException e) {
			if (radiusString.toLowerCase().equalsIgnoreCase("none")) {
				radius = -999;
				paramsCorrect = true;
			} else {
				paramsCorrect = false;
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, USAGE);
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
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, USAGE);
			}
		}

		if (paramsCorrect) {
			if (radius != -999 && (player.getWorld() != world)) {
				paramsCorrect = false;
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, USAGE);
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
				HashMap<String, String> values = new HashMap<>();
				values.put("Number_Entities", Integer.toString(nbEntities));
				values.put("World", world.getName());

				if (type.toLowerCase().equalsIgnoreCase("all")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig
							.getCommandMessage(CmdKillAll.CMD_LABEL, "info_kill_all_in_world", values));
				} else {
					values.put("Entities_Type", type);
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig
							.getCommandMessage(CmdKillAll.CMD_LABEL, "info_kill_entities_in_world", values));
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
				HashMap<String, String> values = new HashMap<>();
				values.put("Number_Entities", Integer.toString(nbEntities));
				values.put("World", world.getName());
				values.put("Radius", Double.toString(radius));

				if (type.toLowerCase().equalsIgnoreCase("all")) {
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig
							.getCommandMessage(CmdKillAll.CMD_LABEL, "info_kill_all_in_world_with_radius", values));
				} else {
					values.put("Entities_Type", type);
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig.getCommandMessage(
							CmdKillAll.CMD_LABEL, "info_kill_entities_in_world_with_radius", values));
				}
			}
		}
	}

}
