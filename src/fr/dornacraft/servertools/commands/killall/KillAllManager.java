package fr.dornacraft.servertools.commands.killall;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class KillAllManager {

	public static final String ENTITY_OPTIONS = "all|monsters|animals|ambient|villagers|drops|xp|arrows|mobs:<MobType>";
	public static final String WORLD_NAME_OPTIONS = "world=current";
	public static final String RADIUS_OPTIONS = "radius=-1";
	public static final String USAGE = String.format("Usage : §6/killall <%s> [%s] [%s]", ENTITY_OPTIONS,
			WORLD_NAME_OPTIONS, RADIUS_OPTIONS);

	public static void checkEntityTypeArgValidity(String entityType) throws KillAllException {
		boolean all = entityType.equalsIgnoreCase("all");
		boolean monsters = entityType.equalsIgnoreCase("monsters");
		boolean animals = entityType.equalsIgnoreCase("animals");
		boolean ambient = entityType.equalsIgnoreCase("ambient");
		boolean villagers = entityType.equalsIgnoreCase("villagers");
		boolean drops = entityType.equalsIgnoreCase("drops");
		boolean xp = entityType.equalsIgnoreCase("xp");
		boolean arrows = entityType.equalsIgnoreCase("arrows");

		boolean isValidInput = all | monsters | animals | ambient | villagers | drops | xp | arrows;

		if (!isValidInput) {
			String mobPatternRegex = "^mobs:.*$";

			if (entityType.matches(mobPatternRegex)) {
				String mobTypeName = entityType.replace("mobs:", "").toUpperCase();

				try {
					EntityType.valueOf(mobTypeName);
				} catch (IllegalArgumentException e) {
					throw new KillAllException(
							ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "error_invalid_entity_type"));
				}
			} else {
				throw new KillAllException(
						ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "error_invalid_entity_type"));
			}
		}
	}

	public static void checkWorldArgValidity(String worldArg) throws KillAllException {
		if (!worldArg.isEmpty() && !worldArg.equalsIgnoreCase("current") && !worldArg.equalsIgnoreCase("all")
				&& Bukkit.getWorld(worldArg) == null) {
			throw new KillAllException(
					ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "error_invalid_world"));
		}
	}

	public static String getWorldName(Player player, String worldArg) {
		String worldName = worldArg;

		if (worldName.isEmpty() || worldName.equalsIgnoreCase("current")) {
			worldName = player.getWorld().getName();
		}
		return worldName;
	}

	public static String getEntityTypeName(String entityType) {
		switch (entityType.toLowerCase()) {
		case "all":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_all_type_name");
		case "monsters":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_monsters_type_name");
		case "animals":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_animals_type_name");
		case "ambient":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_ambient_type_name");
		case "villagers":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_villagers_type_name");
		case "drops":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_drops_type_name");
		case "xp":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_xp_type_name");
		case "arrows":
			return ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, "normal_arrows_type_name");
		default:
			String mobTypeName = entityType.replace("mobs:", "").toUpperCase();
			return mobTypeName;
		}
	}

	public static boolean isCurrentPlayerWorld(Player player, World world) {
		return world.getName().equalsIgnoreCase(player.getWorld().getName());
	}

	public static boolean isTargetedWorld(Player player, World world, String worldName) {
		return worldName.equalsIgnoreCase("all")
				|| worldName.isEmpty() && isCurrentPlayerWorld(player, world)
				|| worldName.equalsIgnoreCase(world.getName());
	}

	public static boolean isTargetedEntity(Entity selectedEntity, String entityTypeTargeted, int radius) {
		if (!(selectedEntity.getType().equals(EntityType.PLAYER))) {
			switch (entityTypeTargeted) {
			case "all":
				return true;
			case "monsters":
				if (selectedEntity instanceof Monster) {
					return true;
				}
				break;
			case "animals":
				if (selectedEntity instanceof Animals) {
					return true;
				}
				break;
			case "ambient":
				if (selectedEntity instanceof Ambient) {
					return true;
				}
				break;
			case "villagers":
				if (selectedEntity instanceof Villager) {
					return true;
				}
				break;
			case "drops":
				if (selectedEntity.getType().equals(EntityType.DROPPED_ITEM)) {
					return true;
				}
				break;
			case "xp":
				if (selectedEntity.getType().equals(EntityType.EXPERIENCE_ORB)) {
					return true;
				}
				break;
			case "arrows":
				if (selectedEntity.getType().equals(EntityType.ARROW)) {
					return true;
				}
				break;
			default:
				if (selectedEntity.getType().toString().equalsIgnoreCase(entityTypeTargeted.replace("mobs:", ""))) {
					return true;
				}
				break;
			}
		}
		return false;
	}

	public static void killEntities(Player player, String entityType, int radius, String worldName) {
		int nbEntities = 0;
		String entityTypeName = getEntityTypeName(entityType);

		for (World world : Bukkit.getWorlds()) {
			if (isTargetedWorld(player, world, worldName)) {
				List<Entity> entities = world.getEntities();

				if (radius >= 0) {
					entities = player.getNearbyEntities(radius, radius, radius);
				}

				for (Entity entity : entities) {
					if (isTargetedEntity(entity, entityType, radius)) {
						entity.remove();
						nbEntities++;
					}
				}
			}
		}

		HashMap<String, String> values = new HashMap<>();
		values.put("Number_Entities", Integer.toString(nbEntities));
		values.put("Entity_Type_Name", entityTypeName);

		boolean isAllWorldTargeted = worldName.equalsIgnoreCase("all");
		String configKey = "info_killall_on_server";

		if (!isAllWorldTargeted) {
			values.put("World", worldName);

			if (radius >= 0) {
				values.put("Radius", Integer.toString(radius));
				configKey = "info_kill_all_in_world_with_radius";
			} else {
				configKey = "info_kill_all_in_world";
			}
		}
		UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
				ServerToolsConfig.getCommandMessage(CmdKillAll.CMD_LABEL, configKey, values));
	}
}
