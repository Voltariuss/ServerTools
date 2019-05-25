package fr.dornacraft.servertools.commands.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class CmdMsg implements CommandExecutor {

	public static final String CMD_LABEL = "msg";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("dornacraft.essentials.msg")) {
			if (args.length > 1) {
				Player target = Bukkit.getPlayer(args[0]);

				if (target != null) {
					StringBuilder sb = new StringBuilder();

					for (int i = 1; i < args.length; i++) {
						sb.append(args[i] + " ");
					}
					PrivateMessageManager.sendPrivateMessage(sender, target, sb.toString());
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_UNKNOW);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_NOT_ENOUGH_ARGUMENTS);
			}
		} else {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
		}
		return false;
	}
}
