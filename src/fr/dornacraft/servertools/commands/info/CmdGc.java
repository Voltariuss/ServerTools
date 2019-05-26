package fr.dornacraft.servertools.commands.info;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.ServerToolsConfig;
import fr.dornacraft.servertools.utils.Lag;
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

				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_header"));

				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
						ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_time_server_running") + CmdGc.getTimeExecution());

				DecimalFormat df = new DecimalFormat("#.##");
				double tps = Lag.getTPS();
				double lag = 100 - Math.round((1.0D - tps / 20.0D) * 100.0D);
				StringBuilder sb = new StringBuilder();
				System.out.println(ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_current_tps") + df.format(tps));
				sb.append(ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_current_tps") + df.format(tps));
				if (lag >= 99)
					sb.append(" §2");
				else if (lag < 99 && lag >= 80)
					sb.append(" §a");
				else if (lag < 79 && lag >= 60)
					sb.append(" §e");
				else if (lag < 59 && lag >= 40)
					sb.append(" §6");
				else if (lag < 39 && lag >= 20)
					sb.append(" §c");
				else
					sb.append(" §4");
				sb.append("[ " + lag + "%% ]");
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, sb.toString());

				int Xmx = (int) ((Runtime.getRuntime().maxMemory() / Math.pow(2, 10)) / Math.pow(2, 10));
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_max_memory"), Xmx);

				int Xmc = (int) ((Runtime.getRuntime().totalMemory() / Math.pow(2, 10)) / Math.pow(2, 10));
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_used_memory"), Xmc);

				int Xms = (int) ((Runtime.getRuntime().freeMemory() / Math.pow(2, 10)) / Math.pow(2, 10));
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, ServerToolsConfig.getCommandMessage(CmdGc.CMD_LABEL, "info_free_memory"), Xms);

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

	private static String getTimeExecution() {
		StringBuilder sb = new StringBuilder();
		sb.append("§c");
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
