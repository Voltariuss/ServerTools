package fr.dornacraft.servertools.controller.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dornacraft.servertools.utils.ServerToolsConfig;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class CmdBroadcast implements CommandExecutor {

	public static final String CMD_LABEL = "broadcast";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("dornacraft.essentials.broadcast")) {
			if (args.length >= 1) {
				StringBuilder bc = new StringBuilder();
				bc.append(ServerToolsConfig.getCommandMessage(CMD_LABEL, "normal_prefix"));

				for (String part : args) {
					part = part.replace("&", "§").replace("§§", "&");
					bc.append(part + " ");
				}
				Bukkit.broadcastMessage(bc.toString());
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.COMMAND_NOT_ENOUGH_ARGUMENTS);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.FAILURE, sender, UtilsAPI.PERMISSION_MISSING);
		}
		return false;
	}
}
