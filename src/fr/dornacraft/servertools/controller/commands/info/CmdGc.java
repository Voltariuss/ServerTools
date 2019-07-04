package fr.dornacraft.servertools.controller.commands.info;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.utils.Lag;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdGc extends DornacraftCommand {

	public static final String CMD_LABEL = "gc";

	private static int timer;

	public CmdGc() {
		super(CMD_LABEL);
		timer = 0;
		this.runTimer();

		getCmdTreeExecutor().getRoot().setExecutor(new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				Player player = (Player) sender;

				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_header"));

				// Time server running
				String timeRunning = CmdGc.getTimeExecution();
				HashMap<String, String> valuesTimeRunning = new HashMap<>();
				valuesTimeRunning.put("Time_Running", timeRunning);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig
						.getCommandMessage(CmdGc.CMD_LABEL, "info_time_server_running", valuesTimeRunning));

				// Tps
				DecimalFormat df = new DecimalFormat("#.##");
				double tps = Lag.getTPS();
				double lag = 100 - Math.round((1.0D - tps / 20.0D) * 100.0D);
				HashMap<String, String> valuesTps = new HashMap<>();
				valuesTps.put("Tps_Value", df.format(tps));
				valuesTps.put("Tps_Indicator", getTpsIndicator(lag));
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_current_tps", valuesTps));

				// Memory
				String Xmx = Integer
						.toString((int) ((Runtime.getRuntime().maxMemory() / Math.pow(2, 10)) / Math.pow(2, 10)));
				HashMap<String, String> valuesXmx = new HashMap<>();
				valuesXmx.put("Max_Memory", Xmx);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_max_memory", valuesXmx));

				String Xmc = Integer
						.toString((int) ((Runtime.getRuntime().totalMemory() / Math.pow(2, 10)) / Math.pow(2, 10)));
				HashMap<String, String> valuesXmc = new HashMap<>();
				valuesXmc.put("Used_Memory", Xmc);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_used_memory", valuesXmc));

				String Xms = Integer
						.toString((int) ((Runtime.getRuntime().freeMemory() / Math.pow(2, 10)) / Math.pow(2, 10)));
				HashMap<String, String> valuesXms = new HashMap<>();
				valuesXms.put("Max_Memory", Xms);
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_free_memory", valuesXms));

				// Worlds
				for (World world : Bukkit.getWorlds()) {
					String worldType = world.getEnvironment().toString();
					String worldNameFile = world.getName();
					int worldChunks = world.getLoadedChunks().length;
					int worldEntities = world.getEntities().size();
					int tileEntities = 0;

					for (Chunk chunk : world.getLoadedChunks()) {
						tileEntities += chunk.getTileEntities().length;
					}

					HashMap<String, String> values = new HashMap<>();
					values.put("Name", worldNameFile);
					values.put("Type", worldType);
					values.put("Number_Chunks_Loaded", Integer.toString(worldChunks));
					values.put("Number_Entities", Integer.toString(worldEntities));
					values.put("Number_Tiles", Integer.toString(tileEntities));

					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
							ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_world_template", values));
				}
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_footer"));
			}
		});
	}

	private void runTimer() {
		Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(ServerTools.class), new Runnable() {

			@Override
			public void run() {
				timer++;
			}
		}, 0, 20);
	}

	private String getTpsIndicator(double lag) {
		ChatColor tpsIndicatorColor;

		if (lag == 100) {
			tpsIndicatorColor = ChatColor.DARK_GREEN;
		} else if (lag < 100 && lag >= 80) {
			tpsIndicatorColor = ChatColor.GREEN;
		} else if (lag < 80 && lag >= 60) {
			tpsIndicatorColor = ChatColor.YELLOW;
		} else if (lag < 60 && lag >= 40) {
			tpsIndicatorColor = ChatColor.GOLD;
		} else if (lag < 40 && lag >= 20) {
			tpsIndicatorColor = ChatColor.RED;
		} else {
			tpsIndicatorColor = ChatColor.DARK_RED;
		}
		return tpsIndicatorColor.toString() + "[" + lag + "%%]";
	}

	private static String getTimeExecution() {
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.RED.toString());
		int secondes = timer;
		int minutes = 0;
		int heures = 0;
		int jours = 0;

		int refMinute = 60;
		int refHeure = refMinute * 60;
		int refJour = refHeure * 24;

		while (secondes >= refJour) {
			secondes -= refJour;
			jours++;
		}
		while (secondes >= refHeure) {
			secondes -= refHeure;
			heures++;
		}
		while (secondes >= refMinute) {
			secondes -= refMinute;
			minutes++;
		}

		if (jours != 0)
			sb.append(jours + " jour(s) ");
		if (heures != 0 || jours != 0)
			sb.append(heures + " heure(s) ");
		if (minutes != 0 || heures != 0 || jours != 0)
			sb.append(minutes + " minute(s) ");
		sb.append(secondes + " seconde(s)");

		return sb.toString();
	}
}
