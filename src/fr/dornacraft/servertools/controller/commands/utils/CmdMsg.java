package fr.dornacraft.servertools.controller.commands.utils;

import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.servertools.ServerTools;
import fr.dornacraft.servertools.model.managers.PrivateMessageManager;
import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

public class CmdMsg implements CommandExecutor {

	public static final String CMD_LABEL = "msg";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// - /msg <player> <message>
		if (sender.hasPermission(JavaPlugin.getPlugin(ServerTools.class).getCommand(CMD_LABEL).getPermission())) {
			if (args.length > 1) {
				Player target = Bukkit.getPlayer(args[0]);

				if (target != null) {
					StringJoiner messageJoiner = new StringJoiner(" ");

					for (int i = 1; i < args.length; i++) {
						messageJoiner.add(args[i]);
					}
					String message = messageJoiner.toString();
					PrivateMessageManager.sendPrivateMessage(sender, target, message);
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PLAYER_NOT_FOUND);
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
