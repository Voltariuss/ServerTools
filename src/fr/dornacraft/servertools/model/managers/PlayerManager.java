package fr.dornacraft.servertools.model.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.controller.commands.info.CmdSeen;
import fr.dornacraft.servertools.controller.commands.info.CmdWhois;
import fr.dornacraft.servertools.controller.commands.player.CmdFeed;
import fr.dornacraft.servertools.controller.commands.player.CmdFly;
import fr.dornacraft.servertools.controller.commands.player.CmdGamemode;
import fr.dornacraft.servertools.controller.commands.player.CmdHeal;
import fr.dornacraft.servertools.controller.commands.utils.CmdAdminExp;
import fr.dornacraft.servertools.controller.commands.utils.CmdClear;
import fr.dornacraft.servertools.controller.commands.utils.CmdEnderchest;
import fr.dornacraft.servertools.controller.commands.utils.CmdExp;
import fr.dornacraft.servertools.model.database.SQLPlayer;
import fr.dornacraft.servertools.model.utils.GameModeType;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.dornacraft.servertools.utils.Utils;
import fr.voltariuss.simpledevapi.ConfigManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class PlayerManager {

	public static void checkPlayerPersistence(Player player) throws SQLException {
		if (SQLPlayer.containsPlayer(player)) {
			SQLPlayer.updateHostAddress(player);
		} else {
			SQLPlayer.persistPlayer(player);
			player.teleport(GlobalManager.getSpawnLocation());
			player.setBedSpawnLocation(GlobalManager.getSpawnLocation());
		}
	}

	public static OfflinePlayer getOfflinePlayerFromName(String name) throws SQLException {
		Player player = Bukkit.getPlayer(name);

		if (player != null) {
			return player;
		} else if (!name.contains(".")) {
			UUID uuid = SQLPlayer.getUUIDFromName(name);
			return uuid != null ? Bukkit.getOfflinePlayer(uuid) : null;
		} else {
			return null;
		}
	}

	public static String getHostAddress(OfflinePlayer target) throws SQLException {
		if (target.isOnline()) {
			return ((Player) target).getAddress().getAddress().getHostAddress();
		} else {
			return SQLPlayer.getHostAddress(target);
		}
	}

	public static List<String> getPlayersName(String hostAddress) throws SQLException {
		return SQLPlayer.getPlayersName(hostAddress);
	}

	public static String getCountry(InetSocketAddress address) throws IOException {
		String unknowTag = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "normal_unknow_tag");

		if (address == null) {
			return unknowTag;
		}
		URL url = new URL("http://ip-api.com/json/" + address.getHostName());
		BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuilder entirePage = new StringBuilder();
		String inputLine;

		while ((inputLine = stream.readLine()) != null) {
			entirePage.append(inputLine);
		}
		stream.close();

		if (!(entirePage.toString().contains("\"country\":\""))) {
			return unknowTag;
		} else {
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		}
	}

	public static void displayPlayerHostAddressInformations(CommandSender sender, OfflinePlayer player)
			throws SQLException, IOException {
		String hostAdress = getHostAddress(player);
		String name = player.getName();
		String country = null;
		String lastConnectionTime = Utils.convertTime(System.currentTimeMillis() - player.getLastPlayed());
		List<String> playersName = getPlayersName(hostAdress);

		if (player.isOnline()) {
			country = getCountry(player.getPlayer().getAddress());
		}
		HashMap<String, String> values = new HashMap<>();
		values.put("Last_Connection_Time", lastConnectionTime);

		String header = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_header_info_ip");
		String prefixPseudo = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_pseudo");
		String prefixLastIpKnow = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_last_ip_know");
		String prefixLocation = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_location");
		String prefixLastConnection = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_last_connection");
		String connected = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_connected");
		String lastConnectionTimeStr = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL,
				"other_last_connection_time", values);
		String prefixListPlayersWithSameIp = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL,
				"other_list_players_with_same_ip");

		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, Utils.getHeader(header));
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, Utils.getNewLine(prefixPseudo, name));
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, Utils.getNewLine(prefixLastIpKnow, hostAdress));
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, Utils.getNewLine(prefixLocation, country));

		if (player.isOnline()) {
			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, Utils.getNewLine(prefixLastConnection, connected));
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender,
					Utils.getNewLine(prefixLastConnection, lastConnectionTimeStr));
		}
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, prefixListPlayersWithSameIp);

		for (String playerName : playersName) {
			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, " §e- §b" + playerName);
		}
	}

	public static void displayHostAddressInformations(CommandSender sender, String hostAdress) throws SQLException {
		List<String> players = getPlayersName(hostAdress);

		if (!players.isEmpty()) {
			String header = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_header_info_ip");
			String prefixHostAddress = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL, "other_host_address");
			String prefixListPlayersWithSameIp = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL,
					"other_list_players_with_same_ip");

			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, Utils.getHeader(header));
			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, Utils.getNewLine(prefixHostAddress, hostAdress));
			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, prefixListPlayersWithSameIp);

			for (String player : players) {
				UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, " §e- §b" + player);
			}
		} else {
			String errorMessage = ServerToolsConfig.getCommandMessage(CmdSeen.CMD_LABEL,
					"error_invalid_input_or_unkown_target");
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, errorMessage);
		}
	}

	public static void displayGlobalPlayerInformations(CommandSender sender, Player target) {
		String header = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL,
				"other_header_info_player");
		String prefixPseudo = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL, "other_pseudo");
		String prefixHealth = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL, "other_health");
		String prefixHungry = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL, "other_hungry");
		String prefixMinecraftLevel = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL,
				"other_minecraft_level");
		String prefixWorld = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL, "other_world");
		String prefixLocation = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL,
				"other_location");
		String prefixHostAddress = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL,
				"other_host_address");
		String prefixGamemode = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL,
				"other_gamemode");
		String prefixGodmode = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL, "other_godmode");
		String prefixFlymode = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL, "other_flymode");
		String prefixOp = ConfigManager.getCommandMessage(ServerTools.class, CmdWhois.CMD_LABEL, "other_op");

		sender.sendMessage(Utils.getHeader(header));
		sender.sendMessage(Utils.getNewLine(prefixPseudo, target.getDisplayName()));
		sender.sendMessage(
				Utils.getNewLine(prefixHealth, (int) target.getHealth() + "§7/§e" + (int) target.getHealthScale()));
		sender.sendMessage(
				Utils.getNewLine(prefixHungry, (int) (target.getFoodLevel() + target.getSaturation()) + "§7/§e20"));
		sender.sendMessage(Utils.getNewLine(prefixMinecraftLevel, Integer.toString(target.getLevel())));
		sender.sendMessage(Utils.getNewLine(prefixWorld, target.getWorld().getName()));
		sender.sendMessage(Utils.getNewLine(prefixLocation, Utils.getStrPosition(target.getLocation())));
		sender.sendMessage(Utils.getNewLine(prefixHostAddress, target.getAddress().getAddress().getHostAddress()));
		sender.sendMessage(Utils.getNewLine(prefixGamemode, target.getGameMode().name()));
		sender.sendMessage(
				Utils.getNewLine(prefixGodmode, Utils.displayState(GodPlayerManager.getInstance().isGod(target))));
		sender.sendMessage(Utils.getNewLine(prefixFlymode, Utils.displayState(target.getAllowFlight())));
		sender.sendMessage(Utils.getNewLine(prefixOp, Utils.displayState(target.isOp())));
	}

	public static String getPing(Player player) {
		int ping = ((CraftPlayer) player).getHandle().ping;
		ChatColor pingColor;

		if (ping < 40) {
			pingColor = ChatColor.DARK_GREEN;
		} else if (ping < 60) {
			pingColor = ChatColor.GREEN;
		} else if (ping < 100) {
			pingColor = ChatColor.YELLOW;
		} else if (ping < 200) {
			pingColor = ChatColor.RED;
		} else {
			pingColor = ChatColor.DARK_RED;
		}
		return pingColor.toString() + ping;
	}

	public static void heal(CommandSender sender, Player player, boolean silent) {
		player.setHealth(player.getHealthScale());
		player.setFireTicks(0);
		player.setRemainingAir(player.getMaximumAir());
		feed(sender, player, true);

		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
		Utils.displayFeedBackCommandAction(CmdHeal.CMD_LABEL, sender, player, "heal", silent);
	}

	public static void feed(CommandSender sender, Player player, boolean silent) {
		player.setExhaustion(0);
		player.setSaturation(5);
		player.setFoodLevel(20);
		Utils.displayFeedBackCommandAction(CmdFeed.CMD_LABEL, sender, player, "feed", silent);
	}

	public static void clearAll(CommandSender sender, Player player, boolean silent) {
		clearInventory(sender, player, true);
		clearEnderChest(sender, player, true);
		Utils.displayFeedBackCommandAction(CmdClear.CMD_LABEL, sender, player, "clear_all", silent);
	}

	public static void clearInventory(CommandSender sender, Player player, boolean silent) {
		clearInventoryContentOnly(sender, player, true);
		clearArmor(sender, player, true);
		Utils.displayFeedBackCommandAction(CmdClear.CMD_LABEL, sender, player, "clear_inventory", silent);
	}

	public static void clearInventoryContentOnly(CommandSender sender, Player player, boolean silent) {
		player.getInventory().clear();
		Utils.displayFeedBackCommandAction(CmdClear.CMD_LABEL, sender, player, "clear_inventory_content_only", silent);
	}

	public static void clearArmor(CommandSender sender, Player player, boolean silent) {
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		Utils.displayFeedBackCommandAction(CmdClear.CMD_LABEL, sender, player, "clear_armor", silent);
	}

	public static void clearEnderChest(CommandSender sender, Player player, boolean silent) {
		player.getEnderChest().clear();
		Utils.displayFeedBackCommandAction(CmdClear.CMD_LABEL, sender, player, "clear_enderchest", silent);
	}

	public static void displayExperience(CommandSender sender, Player player) {
		HashMap<String, String> values = new HashMap<>();
		values.put("Target", player.getName());
		values.put("Level", Integer.toString(player.getLevel()));
		values.put("Amount_Exp", Integer.toString(Math.round(player.getExp() * player.getExpToLevel())));
		values.put("Amount_Exp_To_LevelUp", Integer.toString(player.getExpToLevel()));
		values.put("Total_Amount_Exp", Integer.toString(ExperienceManager.getTotalExperience(player)));
		String messageId = "info_current_level";

		if (sender.getName().equalsIgnoreCase(player.getName())) {
			messageId = "info_current_level_himself";
		}
		String message = ServerToolsConfig.getCommandMessage(CmdExp.CMD_LABEL, messageId, values);
		UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, message);
	}

	public static void setExperience(CommandSender sender, Player player, int xp, boolean silent) {
		ExperienceManager.setTotalExperience(player, xp);
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

		if (!silent) {
			String messageId = null;
			HashMap<String, String> values = new HashMap<>();
			values.put("Amount", Integer.toString(xp));

			if (!sender.getName().equalsIgnoreCase(player.getName())) {
				values.put("Target", player.getName());
				String messageSender = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, "info_set_exp");
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, messageSender);
			}

			if (sender.getName().equalsIgnoreCase(player.getName()) || sender instanceof ConsoleCommandSender) {
				messageId = "info_change_exp";
			} else {
				values.put("Sender", sender.getName());
				messageId = "info_change_exp_by_other";
			}
			String message = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, messageId, values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, message);
		}
	}

	public static void giveExperience(CommandSender sender, Player player, int amount, boolean silent) {
		System.out.println(ExperienceManager.getTotalExperience(15));
		int newXp = ExperienceManager.getTotalExperience(player) + amount;
		setExperience(sender, player, newXp, true);

		if (!silent) {
			String messageId = null;
			HashMap<String, String> values = new HashMap<>();
			values.put("Amount", Integer.toString(amount));

			if (!sender.getName().equalsIgnoreCase(player.getName())) {
				values.put("Target", player.getName());
				String messageSender = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, "info_give_exp",
						values);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, messageSender);
			}

			if (sender.getName().equalsIgnoreCase(player.getName()) || sender instanceof ConsoleCommandSender) {
				messageId = "info_receive_exp";
			} else {
				values.put("Sender", sender.getName());
				messageId = "info_receive_exp_by_other";
			}
			String message = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, messageId, values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, message);
		}
	}

	public static void takeExperience(CommandSender sender, Player player, int amount, boolean silent) {
		int newXp = ExperienceManager.getTotalExperience(player) - amount;

		if (newXp < 0) {
			newXp = 0;
		}
		setExperience(sender, player, newXp, true);

		if (!silent) {
			String messageId = null;
			HashMap<String, String> values = new HashMap<>();
			values.put("Amount", Integer.toString(amount));

			if (!sender.getName().equalsIgnoreCase(player.getName())) {
				values.put("Target", player.getName());
				String messageSender = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, "info_take_exp",
						values);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, messageSender);
			}

			if (sender.getName().equalsIgnoreCase(player.getName()) || sender instanceof ConsoleCommandSender) {
				messageId = "info_lose_exp";
			} else {
				values.put("Sender", sender.getName());
				messageId = "info_lose_exp_by_other";
			}
			String message = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, messageId, values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, message);
		}
	}

	public static void setLevel(CommandSender sender, Player player, int level, boolean silent) {
		player.setLevel(level);
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

		if (!silent) {
			String messageId = null;
			HashMap<String, String> values = new HashMap<>();
			values.put("Amount", Integer.toString(level));

			if (!sender.getName().equalsIgnoreCase(player.getName())) {
				values.put("Target", player.getName());
				String messageSender = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, "info_set_level");
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, messageSender);
			}

			if (sender.getName().equalsIgnoreCase(player.getName()) || sender instanceof ConsoleCommandSender) {
				messageId = "info_change_level";
			} else {
				values.put("Sender", sender.getName());
				messageId = "info_change_level_by_other";
			}
			String message = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, messageId, values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, message);
		}
	}

	public static void giveLevel(CommandSender sender, Player player, int amount, boolean silent) {
		int playerLevel = player.getLevel();
		int newLevel = playerLevel + amount;
		setLevel(sender, player, newLevel, true);

		if (!silent) {
			String messageId = null;
			HashMap<String, String> values = new HashMap<>();
			values.put("Amount", Integer.toString(amount));

			if (!sender.getName().equalsIgnoreCase(player.getName())) {
				values.put("Target", player.getName());
				String messageSender = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, "info_give_level",
						values);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, messageSender);
			}

			if (sender.getName().equalsIgnoreCase(player.getName()) || sender instanceof ConsoleCommandSender) {
				messageId = "info_receive_level";
			} else {
				values.put("Sender", sender.getName());
				messageId = "info_receive_level_by_other";
			}
			String message = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, messageId, values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, message);
		}
	}

	public static void takeLevel(CommandSender sender, Player player, int amount, boolean silent) {
		int playerLevel = player.getLevel();
		int newLevel = playerLevel - amount;

		if (newLevel < 0) {
			newLevel = 0;
		}
		setLevel(sender, player, newLevel, true);

		if (!silent) {
			String messageId = null;
			HashMap<String, String> values = new HashMap<>();
			values.put("Amount", Integer.toString(amount));

			if (!sender.getName().equalsIgnoreCase(player.getName())) {
				values.put("Target", player.getName());
				String messageSender = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, "info_take_level",
						values);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, messageSender);
			}

			if (sender.getName().equalsIgnoreCase(player.getName()) || sender instanceof ConsoleCommandSender) {
				messageId = "info_lose_level";
			} else {
				values.put("Sender", sender.getName());
				messageId = "info_lose_level_by_other";
			}
			String message = ServerToolsConfig.getCommandMessage(CmdAdminExp.CMD_LABEL, messageId, values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, message);
		}
	}

	public static void setFly(CommandSender sender, Player player, boolean isAllowedFlight) {
		HashMap<String, String> values = new HashMap<>();
		values.put("Sender", sender.getName());
		values.put("Target", player.getName());

		String state = null;

		if (isAllowedFlight) {
			state = ServerToolsConfig.getCommandMessage(CmdFly.CMD_LABEL, "normal_fly_mode_state_enabled");
		} else {
			state = ServerToolsConfig.getCommandMessage(CmdFly.CMD_LABEL, "normal_fly_mode_state_disabled");
		}
		values.put("State", state + ChatColor.RESET.toString());

		String messageSenderId = null;
		MessageLevel messageSenderLevel = MessageLevel.INFO;

		if (player.getAllowFlight() != isAllowedFlight) {
			if (sender != player) {
				if (sender.hasPermission("dornacraft.essentials.fly.other")) {
					player.setAllowFlight(isAllowedFlight);
					String messageTargetId = null;

					if (sender instanceof ConsoleCommandSender) {
						messageTargetId = "info_fly_mode_activate";
					} else {
						messageTargetId = "info_fly_mode_activate_by_other";
					}
					String messageTarget = ServerToolsConfig.getCommandMessage(CmdFly.CMD_LABEL, messageTargetId,
							values);
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, messageTarget);
					messageSenderId = "info_fly_mode_activate_target";
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				player.setAllowFlight(isAllowedFlight);
				messageSenderId = "info_fly_mode_activate_by_himself";
			}
		} else {
			if (sender != player) {
				if (sender.hasPermission("dornacraft.essentials.fly.other")) {
					messageSenderLevel = MessageLevel.FAILURE;
					messageSenderId = "failure_fly_mode_already_in_this_state";
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				messageSenderLevel = MessageLevel.FAILURE;
				messageSenderId = "failure_fly_mode_already_in_this_state_himself";
			}
		}

		if (messageSenderId != null) {
			String messageSender = ServerToolsConfig.getCommandMessage(CmdFly.CMD_LABEL, messageSenderId, values);
			UtilsAPI.sendSystemMessage(messageSenderLevel, sender, messageSender);
		}
	}

	public static void toggleFly(CommandSender sender, Player player) {
		setFly(sender, player, !player.getAllowFlight());
	}

	public static void setGameMode(CommandSender sender, Player player, GameModeType gameModeType) {
		HashMap<String, String> values = new HashMap<>();
		values.put("Sender", sender.getName());
		values.put("Target", player.getName());
		values.put("Game_Mode", gameModeType.getName());

		String messageSenderId = null;
		MessageLevel messageSenderLevel = MessageLevel.INFO;

		if (player.getGameMode() != gameModeType.getGameMode()) {
			if (sender != player) {
				if (sender.hasPermission("dornacraft.essentials.gamemode.other")) {
					player.setGameMode(gameModeType.getGameMode());
					String messageTargetId = null;

					if (sender instanceof ConsoleCommandSender) {
						messageTargetId = "info_gamemode_defined";
					} else {
						messageTargetId = "info_gamemode_defined_by_other";
					}
					String messageTarget = ServerToolsConfig.getCommandMessage(CmdGamemode.CMD_LABEL, messageTargetId,
							values);
					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, messageTarget);
					messageSenderId = "info_gamemode_defined_target";
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				player.setGameMode(gameModeType.getGameMode());
				messageSenderId = "info_gamemode_defined_by_himself";
			}
		} else {
			if (sender != player) {
				if (sender.hasPermission("dornacraft.essentials.gamemode.other")) {
					messageSenderLevel = MessageLevel.FAILURE;
					messageSenderId = "failure_gamemode_already_in_this_mode";
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				messageSenderLevel = MessageLevel.FAILURE;
				messageSenderId = "failure_gamemode_already_in_this_mode_himself";
			}
		}

		if (messageSenderId != null) {
			String messageSender = ServerToolsConfig.getCommandMessage(CmdGamemode.CMD_LABEL, messageSenderId, values);
			UtilsAPI.sendSystemMessage(messageSenderLevel, sender, messageSender);
		}
	}

	public static void setGameMode(CommandSender sender, Player player, GameMode gameMode) {
		GameModeType gameModeType = GameModeType.getFromGameMode(gameMode);
		setGameMode(sender, player, gameModeType);
	}

	public static void openEnderChest(Player sender, Player player) {
		sender.openInventory(player.getEnderChest());
		HashMap<String, String> values = new HashMap<>();
		values.put("Target", player.getName());
		String messageId = null;

		if (sender == player) {
			messageId = "info_open_enderchest";
		} else {
			if (sender.hasPermission("dornacraft.essentials.enderchest.other")) {
				messageId = "info_open_enderchest_of_other";
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
			}
		}

		if (messageId != null) {
			String message = ServerToolsConfig.getCommandMessage(CmdEnderchest.CMD_LABEL, messageId, values);
			UtilsAPI.sendSystemMessage(MessageLevel.INFO, sender, message);
		}
	}
}
