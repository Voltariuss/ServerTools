package fr.dornacraft.servertools.controller.commands.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dornacraft.servertools.model.managers.PrivateMessageManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class CmdReply implements CommandExecutor {

	public static final String CMD_LABEL = "reply";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// - /reply <message>
		if (sender.hasPermission("dornacraft.essentials.reply")) {
			if (args.length >= 1) {
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				PrivateMessageManager.reply(sender, sb.toString());
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_NOT_ENOUGH_ARGUMENTS);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
		}
		return false;
	}
}
