package fr.dornacraft.servertools.controller.commands.utils;

import java.util.StringJoiner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.PrivateMessageManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class CmdReply implements CommandExecutor {

	public static final String CMD_LABEL = "reply";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// - /reply <message>
		if (sender.hasPermission(JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getPermission())) {
			if (args.length >= 1) {
				StringJoiner messageJoiner = new StringJoiner(" ");

				for (int i = 0; i < args.length; i++) {
					messageJoiner.add(args[i]);
				}
				String message = messageJoiner.toString();
				PrivateMessageManager.reply(sender, message);
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_NOT_ENOUGH_ARGUMENTS);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
		}
		return false;
	}
}
