package fr.dornacraft.servertools.commands.info;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
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

				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "§e+------------- ETAT SERVEUR -------------+");

				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "§6Durée de fonctionnement : §c%s",
						CmdGc.getTimeExecution());

				DecimalFormat df = new DecimalFormat("#.##");
				double tps = Lag.getTPS();
				double lag = 100 - Math.round((1.0D - tps / 20.0D) * 100.0D);
				StringBuilder sb = new StringBuilder();
				sb.append("§6TPS actuels : §c" + df.format(tps));
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
				sb.append("[ " + lag + "% ]");
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "%s", sb.toString());

				int Xmx = (int) ((Runtime.getRuntime().maxMemory() / Math.pow(2, 10)) / Math.pow(2, 10));
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "§6Mémoire maximale : §c" + Xmx + "§6 Mo");

				int Xmc = (int) ((Runtime.getRuntime().totalMemory() / Math.pow(2, 10)) / Math.pow(2, 10));
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "§6Mémoire utilisée : §c" + Xmc + "§6 Mo");

				int Xms = (int) ((Runtime.getRuntime().freeMemory() / Math.pow(2, 10)) / Math.pow(2, 10));
				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "§6Mémoire libre : §c" + Xms + "§6 Mo");

				for (World world : Bukkit.getWorlds()) {
					String worldName = world.getEnvironment().toString();
					String worldNameFile = world.getName();
					int worldChunks = world.getLoadedChunks().length;
					int worldEntities = world.getEntities().size();
					int tileEntities = 0;
					for (Chunk chunk : world.getLoadedChunks()) {
						tileEntities += chunk.getTileEntities().length;
					}

					UtilsAPI.sendSystemMessage(MessageLevel.INFO, player,
							"§6" + worldNameFile + " \"§c" + worldName + "§6\" : §c" + worldChunks + "§6 chunks, §c"
									+ worldEntities + "§6 entities, §c" + tileEntities + "§6 tiles");
				}

				UtilsAPI.sendSystemMessage(MessageLevel.INFO, player, "§e+----------------------------------------+");
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
		sb.append("�c");
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
			sb.append(jours + " jours ");
		if (heures != 0 || jours != 0)
			sb.append(heures + " heures ");
		if (minutes != 0 || heures != 0 || jours != 0)
			sb.append(minutes + " minutes ");
		sb.append(secondes + " secondes");

		return sb.toString();
	}
}
