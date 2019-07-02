package fr.dornacraft.servertools.controller.commands.info;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.dornacraft.servertools.model.managers.InfoManager;
import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.dornacraft.servertools.utils.Utils;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.cmds.CommandArgument;
import fr.voltariuss.simpledevapi.cmds.CommandArgumentType;
import fr.voltariuss.simpledevapi.cmds.CommandNode;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommand;
import fr.voltariuss.simpledevapi.cmds.DornacraftCommandExecutor;

public class CmdSeen extends DornacraftCommand {

	public static final String CMD_LABEL = "seen";

	public CmdSeen() {
		super(CMD_LABEL);

		DornacraftCommandExecutor executor = new DornacraftCommandExecutor() {

			@Override
			public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
				OfflinePlayer target = InfoManager.getOfflinePlayerFromPseudo(args[0]);

				if (target != null) {
					getInfoPlayer(sender, target);
				} else {
					getInfoIp(sender, args[0]);
				}
			}
		};

		getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument(CommandArgumentType.PLAYER, true),
				ServerToolsConfig.getCommandMessage(CMD_LABEL, "cmd_desc"), executor, null));
	}

	private void getInfoPlayer(CommandSender sender, OfflinePlayer target) throws Exception {
		String ip = InfoManager.getIp(target);
		sender.sendMessage(Utils.getHeader(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_header_info_ip")));
		sender.sendMessage(
				Utils.getNewLine(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_pseudo"), target.getName()));
		sender.sendMessage(Utils.getNewLine(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_last_ip_know"), ip));
		sender.sendMessage(Utils.getNewLine(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_location"),
				InfoManager.getCountry(target.isOnline() ? target.getPlayer().getAddress() : null)));

		String lastConnectionTime = Utils.convertTime(System.currentTimeMillis() - target.getLastPlayed());
		HashMap<String, String> values = new HashMap<>();
		values.put("Last_Connection_Time", lastConnectionTime);
		sender.sendMessage(Utils.getNewLine(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_last_connection"),
				target.isOnline() ? ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_connected")
						: ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_last_connection_time", values)));
		sender.sendMessage(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_list_players_with_same_ip"));

		for (String player : InfoManager.getPlayers(ip)) {
			sender.sendMessage(" §e- §b" + player);
		}
	}

	private void getInfoIp(CommandSender sender, String ip) throws SQLException {
		ArrayList<String> players = InfoManager.getPlayers(ip);

		if (!players.isEmpty()) {
			sender.sendMessage(Utils.getHeader(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_header_info_ip")));
			sender.sendMessage(Utils.getNewLine("IP", ip));
			sender.sendMessage(ServerToolsConfig.getCommandMessage(CMD_LABEL, "other_list_players_with_same_ip"));

			for (String player : players) {
				sender.sendMessage(" §e- §b" + player);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender,
					ServerToolsConfig.getCommandMessage(CMD_LABEL, "error_invalid_input_or_unkown_target"));
		}
	}
}
